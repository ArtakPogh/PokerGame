const API_URL = "/api/game";

let playerId = localStorage.getItem("playerId");
let tableId = localStorage.getItem("tableId");

if (!playerId) {

    playerId = crypto.randomUUID();

    localStorage.setItem("playerId", playerId);
}

let gameState = {};

let timerInterval;

const communityCards = document.getElementById("communityCards");
const potElement = document.getElementById("pot");
const timerElement = document.getElementById("timer");
const turnIndicator = document.getElementById("turnIndicator");
const playersContainer = document.getElementById("playersContainer");
const playerCards = document.getElementById("playerCards");

const foldBtn = document.getElementById("foldBtn");
const checkBtn = document.getElementById("checkBtn");
const callBtn = document.getElementById("callBtn");
const raiseBtn = document.getElementById("raiseBtn");

const buttons = document.querySelectorAll(".actions button");

const raisePanel = document.getElementById("raisePanel");
const raiseSlider = document.getElementById("raiseSlider");
const raiseValue = document.getElementById("raiseValue");
const confirmRaise = document.getElementById("confirmRaise");

async function joinTable() {

    try {

        const response = await fetch(`${API_URL}/join`, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                playerId: playerId
            })
        });

        const data = await response.json();

        tableId = data.tableId;

        localStorage.setItem("tableId", tableId);

        await loadGame();

        startPolling();
    } catch (error) {

        console.error("Failed to join table:", error);
    }
}

async function loadGame() {

    try {

        const response = await fetch(
            `${API_URL}/state?tableId=${tableId}&playerId=${playerId}`
        );

        gameState = await response.json();

        renderGame();
    } catch (error) {

        console.error("Failed to load game:", error);
    }
}

async function sendAction(action, amount = 0) {

    try {

        disableButtons();

        const response = await fetch(`${API_URL}/action`, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                tableId,
                playerId,
                action,
                amount
            })
        });

        gameState = await response.json();

        renderGame();
    } catch (error) {

        console.error("Failed to send action:", error);
    }
}

function renderGame() {

    renderCommunityCards();

    renderPlayers();

    renderPlayerCards();

    renderPot();

    renderTurn();

    updateButtons();
}

function renderCommunityCards() {

    communityCards.innerHTML = "";

    gameState.communityCards.forEach(card => {

        communityCards.innerHTML += `
            <div class="card">${card}</div>
        `;
    });
}

function renderPlayers() {

    playersContainer.innerHTML = "";

    gameState.players.forEach(player => {

        const activeClass =
            player.id === gameState.currentPlayerId
                ? "active-player"
                : "";

        const foldedClass =
            player.folded
                ? "folded-player"
                : "";

        playersContainer.innerHTML += `

            <div class="player ${activeClass} ${foldedClass}">

                <div class="player-name">
                    ${player.name}
                </div>

                <div class="player-money">
                    $${player.money}
                </div>

                <div class="player-status">
                    ${player.folded ? "Folded" : "Playing"}
                </div>

            </div>
        `;
    });
}

function renderPlayerCards() {

    playerCards.innerHTML = "";

    gameState.playerCards.forEach(card => {

        playerCards.innerHTML += `
            <div class="card">${card}</div>
        `;
    });
}

function renderPot() {

    potElement.innerText = `Pot: $${gameState.pot}`;
}

function renderTurn() {

    const isMyTurn =
        gameState.currentPlayerId === playerId;

    if (isMyTurn) {

        turnIndicator.innerText = "Your Turn";

        enableButtons();

        startTimer();
    } else {

        turnIndicator.innerText =
            `${gameState.currentPlayerName}'s Turn`;

        disableButtons();

        clearInterval(timerInterval);
    }
}

foldBtn.addEventListener("click", () => {
    sendAction("FOLD");
});

checkBtn.addEventListener("click", () => {
    sendAction("CHECK");
});

callBtn.addEventListener("click", () => {

    sendAction("CALL");
});

raiseBtn.addEventListener("click", () => {

    raiseSlider.min = gameState.minimumRaise;

    raiseSlider.max = gameState.playerMoney;

    raiseSlider.value = gameState.minimumRaise;

    raiseValue.innerText = gameState.minimumRaise;

    raisePanel.classList.remove("hidden");
});

raiseSlider.addEventListener("input", () => {

    raiseValue.innerText = raiseSlider.value;
});

confirmRaise.addEventListener("click", () => {

    const amount = parseInt(raiseSlider.value);

    raisePanel.classList.add("hidden");

    sendAction("RAISE", amount);
});

function startTimer() {

    clearInterval(timerInterval);

    let timeLeft = gameState.remainingTime || 15;

    timerElement.innerText = timeLeft;

    timerInterval = setInterval(() => {

        timeLeft--;

        timerElement.innerText = timeLeft;

        if (timeLeft <= 0) {

            clearInterval(timerInterval);

            sendAction("FOLD");
        }

    }, 1000);
}

function enableButtons() {

    buttons.forEach(button => {

        button.disabled = false;
    });
}

function disableButtons() {

    buttons.forEach(button => {

        button.disabled = true;
    });
}

function startPolling() {

    setInterval(async () => {

        await loadGame();

    }, 2000);
}