/**
 * logic.js – Texas Hold'em Poker Frontend
 *
 * Screen flow:
 *   startScreen  →  (click Start)
 *   waitingScreen →  (2nd player joins, polling detects gameStarted)
 *   tableWrapper  →  (play until eliminated or session over)
 *   eliminatedScreen / sessionOverScreen
 */

const API_URL = "/api/game";

let playerId = localStorage.getItem("playerId");
if (!playerId) {
    playerId = crypto.randomUUID();
    localStorage.setItem("playerId", playerId);
}
let tableId = localStorage.getItem("tableId");

let gameState = {};
let timerInterval = null;
let pollingInterval = null;
let hasJoined = false;

const startScreen      = document.getElementById("startScreen");
const waitingScreen    = document.getElementById("waitingScreen");
const eliminatedScreen = document.getElementById("eliminatedScreen");
const sessionOverScreen = document.getElementById("sessionOverScreen");
const tableWrapper     = document.getElementById("tableWrapper");
const waitingMsg       = document.getElementById("waitingMsg");
const winnerMsg        = document.getElementById("winnerMsg");

const playerNameInput  = document.getElementById("playerNameInput");
const startBtn         = document.getElementById("startBtn");
const playAgainBtn     = document.getElementById("playAgainBtn");
const newGameBtn       = document.getElementById("newGameBtn");

const communityCards   = document.getElementById("communityCards");
const potElement       = document.getElementById("pot");
const timerElement     = document.getElementById("timer");
const turnIndicator    = document.getElementById("turnIndicator");
const phaseLabel       = document.getElementById("phaseLabel");
const playersContainer = document.getElementById("playersContainer");
const playerCards      = document.getElementById("playerCards");

const foldBtn    = document.getElementById("foldBtn");
const checkBtn   = document.getElementById("checkBtn");
const callBtn    = document.getElementById("callBtn");
const raiseBtn   = document.getElementById("raiseBtn");
const buttons    = document.querySelectorAll(".actions button");

const raisePanel   = document.getElementById("raisePanel");
const raiseSlider  = document.getElementById("raiseSlider");
const raiseValue   = document.getElementById("raiseValue");
const confirmRaise = document.getElementById("confirmRaise");

window.addEventListener("load", () => {
    if (tableId) {
        showScreen("table");
        loadGame();
        startPolling();
    } else {
        showScreen("start");
    }
});

startBtn.addEventListener("click", async () => {
    const name = playerNameInput.value.trim();
    if (!name) {
        playerNameInput.focus();
        playerNameInput.placeholder = "Please enter your name!";
        return;
    }
    startBtn.disabled = true;
    startBtn.textContent = "Joining...";
    await joinTable(name);
});

function resetAndReload() {
    localStorage.removeItem("tableId");
    localStorage.removeItem("playerId");
    location.reload();
}

playAgainBtn.addEventListener("click", resetAndReload);
newGameBtn.addEventListener("click", resetAndReload);

async function joinTable(name) {
    try {
        const response = await fetch(`${API_URL}/join`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: playerId, name })
        });

        if (!response.ok) {
            console.error("Join failed:", response.status, await response.text());
            startBtn.disabled = false;
            startBtn.textContent = "Start Game";
            return;
        }

        const data = await response.json();
        tableId = data.tableId;
        localStorage.setItem("tableId", tableId);
        gameState = data;
        hasJoined = true;

        applyScreenTransition();
        startPolling();

    } catch (err) {
        console.error("Failed to join:", err);
        startBtn.disabled = false;
        startBtn.textContent = "Start Game";
    }
}

async function loadGame() {
    if (!tableId) return;
    try {
        const response = await fetch(
            `${API_URL}/state?tableId=${tableId}&playerId=${playerId}`
        );

        // FIX Bug 1: if the server restarted and the session is gone (404),
        // automatically wipe stale localStorage and return to the start screen.
        if (response.status === 404) {
            console.warn("Session not found on server — clearing stale local data.");
            stopPolling();
            localStorage.removeItem("tableId");
            localStorage.removeItem("playerId");
            tableId = null;
            showScreen("start");
            // Re-generate a fresh playerId for the next session
            playerId = crypto.randomUUID();
            localStorage.setItem("playerId", playerId);
            startBtn.disabled = false;
            startBtn.textContent = "Start Game";
            return;
        }

        if (!response.ok) return;

        gameState = await response.json();
        applyScreenTransition();
    } catch (err) {
        console.error("loadGame error:", err);
    }
}

async function sendAction(type, amount = 0) {
    try {
        disableButtons();
        const response = await fetch(`${API_URL}/action`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ tableId, playerId, type, amount })
        });

        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            console.error("Action error:", err.error || response.status);
            // Re-enable buttons only if it's still our turn
            if (gameState.currentPlayerId === playerId) enableButtons();
            return;
        }

        gameState = await response.json();
        applyScreenTransition();
    } catch (err) {
        console.error("sendAction error:", err);
    }
}

function applyScreenTransition() {
    if (gameState.sessionOver) {
        stopPolling();
        clearInterval(timerInterval);
        const me = (gameState.players || []).find(p => p.id === playerId);
        const isWinner = me && gameState.sessionWinnerName && me.name === gameState.sessionWinnerName;
        winnerMsg.textContent = isWinner
            ? `🏆 You won the whole game! Congratulations!`
            : `🏆 ${gameState.sessionWinnerName} wins the game!`;
        showScreen("sessionOver");
        return;
    }

    if (gameState.eliminated) {
        clearInterval(timerInterval);
        showScreen("eliminated");
        return;
    }

    if (gameState.waitingForPlayers) {
        waitingMsg.textContent =
            `You're seated. Waiting for another player to join... (${gameState.players?.length || 1} / 2 minimum)`;
        showScreen("waiting");
        return;
    }

    showScreen("table");
    renderGame();
}

function showScreen(name) {
    startScreen.classList.add("hidden");
    waitingScreen.classList.add("hidden");
    eliminatedScreen.classList.add("hidden");
    sessionOverScreen.classList.add("hidden");
    tableWrapper.classList.add("hidden");

    switch (name) {
        case "start":      startScreen.classList.remove("hidden");       break;
        case "waiting":    waitingScreen.classList.remove("hidden");     break;
        case "eliminated": eliminatedScreen.classList.remove("hidden");  break;
        case "sessionOver": sessionOverScreen.classList.remove("hidden"); break;
        case "table":      tableWrapper.classList.remove("hidden");      break;
    }
}

function renderGame() {
    renderCommunityCards();
    renderPlayers();
    renderPlayerCards();
    renderPot();
    renderPhase();
    renderTurn();
    updateButtons();
}

function renderCommunityCards() {
    communityCards.innerHTML = "";
    (gameState.communityCards || []).forEach(card => {
        const div = document.createElement("div");
        div.className = "card";
        div.textContent = formatCard(card);
        applyCardColor(div, card);
        communityCards.appendChild(div);
    });
}

function renderPlayers() {
    playersContainer.innerHTML = "";
    (gameState.players || []).forEach(player => {
        if (player.id === playerId) return; // shown separately at the bottom
        const activeClass = player.id === gameState.currentPlayerId ? "active-player" : "";
        const foldedClass = player.folded ? "folded-player" : "";
        // FIX Bug 3: show all-in status clearly
        const allIn = !player.folded && player.chips === 0;
        const statusText = player.folded ? "Folded" : (allIn ? "All-In" : "In");
        playersContainer.innerHTML += `
            <div class="player ${activeClass} ${foldedClass}">
                <div class="player-name">${player.name}</div>
                <div class="player-money">$${player.chips}</div>
                <div class="player-status">${statusText}</div>
            </div>`;
    });
}

function renderPlayerCards() {
    playerCards.innerHTML = "";
    (gameState.playerCards || []).forEach(card => {
        const div = document.createElement("div");
        div.className = "card";
        div.textContent = formatCard(card);
        applyCardColor(div, card);
        playerCards.appendChild(div);
    });
}

function renderPot() {
    potElement.textContent = `Pot: $${gameState.pot || 0}`;
}

function renderPhase() {
    const labels = {
        PRE_FLOP: "Pre-Flop",
        FLOP:     "Flop",
        TURN:     "Turn",
        RIVER:    "River",
        SHOWDOWN: "Showdown"
    };
    phaseLabel.textContent = labels[gameState.phase] || "";
}

function renderTurn() {
    const isMyTurn = gameState.currentPlayerId === playerId;

    if (gameState.phase === "SHOWDOWN") {
        turnIndicator.textContent = "Showdown — next hand starting...";
        disableButtons();
        clearInterval(timerInterval);
        timerElement.textContent = "";
        return;
    }

    if (isMyTurn) {
        turnIndicator.textContent = "Your Turn";
        enableButtons();
        startTimer();
    } else {
        turnIndicator.textContent = `${gameState.currentPlayerName || "..."}'s Turn`;
        disableButtons();
        clearInterval(timerInterval);
        timerElement.textContent = "";
    }
}

function updateButtons() {
    const me = (gameState.players || []).find(p => p.id === playerId);
    if (!me) return;

    const facingBet = (gameState.currentBet || 0) > (me.currentBet || 0);
    checkBtn.style.display = facingBet ? "none"         : "inline-block";
    callBtn.style.display  = facingBet ? "inline-block" : "none";

    // FIX Bug 3: hide Raise if player is all-in or has no chips to raise with
    const canRaise = me.chips > 0 && me.chips > (gameState.currentBet - (me.currentBet || 0));
    raiseBtn.style.display = canRaise ? "inline-block" : "none";
}

// ─── Card formatting ─────────────────────────────────────────────────────────

const RANK_SYMBOLS = {
    TWO: "2", THREE: "3", FOUR: "4", FIVE: "5", SIX: "6",
    SEVEN: "7", EIGHT: "8", NINE: "9", TEN: "10",
    JACK: "J", QUEEN: "Q", KING: "K", ACE: "A"
};
const SUIT_SYMBOLS = { HEARTS: "♥", DIAMONDS: "♦", CLUBS: "♣", SPADES: "♠" };
const RED_SUITS    = new Set(["HEARTS", "DIAMONDS"]);

function formatCard(cardStr) {
    const parts = cardStr.split(" of ");
    if (parts.length !== 2) return cardStr;
    const rank = RANK_SYMBOLS[parts[0].trim()] || parts[0];
    const suit = SUIT_SYMBOLS[parts[1].trim()] || parts[1];
    return rank + suit;
}

function applyCardColor(div, cardStr) {
    const parts = cardStr.split(" of ");
    if (parts.length === 2 && RED_SUITS.has(parts[1].trim())) {
        div.classList.add("red");
    }
}

// ─── Action handlers ──────────────────────────────────────────────────────────

foldBtn.addEventListener("click",  () => sendAction("FOLD"));
checkBtn.addEventListener("click", () => sendAction("CHECK"));
callBtn.addEventListener("click",  () => sendAction("CALL"));

raiseBtn.addEventListener("click", () => {
    const me = (gameState.players || []).find(p => p.id === playerId);
    const myChips = me ? me.chips : 0;
    const min = gameState.minimumRaise || 50;
    const max = Math.max(min, myChips);  // can't raise more than you have
    raiseSlider.min   = min;
    raiseSlider.max   = max;
    raiseSlider.value = min;
    raiseValue.textContent = min;
    raisePanel.classList.remove("hidden");
});

raiseSlider.addEventListener("input", () => {
    raiseValue.textContent = raiseSlider.value;
});

confirmRaise.addEventListener("click", () => {
    const amount = parseInt(raiseSlider.value, 10);
    raisePanel.classList.add("hidden");
    sendAction("RAISE", amount);
});

// ─── Timer ────────────────────────────────────────────────────────────────────

function startTimer() {
    clearInterval(timerInterval);
    let timeLeft = gameState.remainingTime ?? 30;
    timerElement.textContent = timeLeft;

    timerInterval = setInterval(() => {
        timeLeft--;
        timerElement.textContent = timeLeft;
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            sendAction("FOLD");
        }
    }, 1000);
}

// ─── Button helpers ───────────────────────────────────────────────────────────

function enableButtons() {
    buttons.forEach(b => { b.disabled = false; });
}

function disableButtons() {
    buttons.forEach(b => { b.disabled = true; });
}

// ─── Polling ──────────────────────────────────────────────────────────────────

function startPolling() {
    if (pollingInterval) return;   // don't double-start
    pollingInterval = setInterval(loadGame, 2000);
}

function stopPolling() {
    clearInterval(pollingInterval);
    pollingInterval = null;
}