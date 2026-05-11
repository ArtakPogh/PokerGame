package poker.session;

import poker.game.PokerGame;
import poker.domain.Player;
import poker.domain.enums.GamePhase;
import poker.actions.Action;
import poker.domain.Deck;

import java.util.*;

public class GameSession {
    private final String sessionId;
    private final List<Player> players = new ArrayList<>();
    private final Set<String> eliminatedPlayerIds = new HashSet<>();
    private final int maxPlayers = 6;
    private PokerGame game;
    private boolean gameStarted = false;
    private boolean sessionOver = false;
    private String sessionWinnerId = null;
    private String sessionWinnerName = null;
    private long turnStartTime = System.currentTimeMillis();

    public GameSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public boolean isSessionOver() {
        return sessionOver;
    }

    public String getSessionWinnerName() {
        return sessionWinnerName;
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public long getTurnStartTime() {
        return turnStartTime;
    }

    public void removePlayer(String playerId) {
        boolean removed = players.removeIf(p -> p.getId().equals(playerId));
        if (!removed) throw new IllegalStateException("Player not found in session");
        if (players.size() < 2 && gameStarted) stopGame();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean isEliminated(String playerId) {
        return eliminatedPlayerIds.contains(playerId);
    }

    public boolean addPlayer(Player player) {
        if (isFull()) throw new IllegalStateException("Session is full");
        if (hasPlayer(player.getId())) throw new IllegalStateException("Player already in session");
        if (sessionOver) throw new IllegalStateException("Session is over");
        players.add(player);
        if (canStartGame()) {
            startGame();
        }
        return true;
    }

    public boolean hasPlayer(String playerId) {
        return players.stream().anyMatch(p -> p.getId().equals(playerId));
    }

    private boolean canStartGame() {
        return players.size() >= 2 && !gameStarted;
    }

    public void startGame() {
        if (gameStarted) throw new IllegalStateException("Game already started");
        game = new PokerGame(players, new Deck());
        game.startGame();
        gameStarted = true;
        turnStartTime = System.currentTimeMillis();
    }

    public void stopGame() {
        gameStarted = false;
        game = null;
    }

    public void handleAction(String playerId, Action action) {
        if (!gameStarted) throw new IllegalStateException("Game has not started");
        if (sessionOver) throw new IllegalStateException("Session is over");
        Player player = findPlayer(playerId);
        if (player == null) throw new IllegalStateException("Player not found in session");
        if (isEliminated(playerId)) throw new IllegalStateException("You have been eliminated");
        if (!player.equals(game.getCurrentPlayer())) throw new IllegalStateException("Not this player's turn");

        game.handleAction(action);

        // Reset timer for the next player's turn
        turnStartTime = System.currentTimeMillis();

        if (game.getGameState().getPhase() == GamePhase.SHOWDOWN) {
            handleHandEnd();
        }
    }

    private void handleHandEnd() {
        // Short delay before starting next hand would be handled client-side via SHOWDOWN phase display
        for (Player p : players) {
            if (p.getChips() <= 0 && !eliminatedPlayerIds.contains(p.getId())) {
                eliminatedPlayerIds.add(p.getId());
                System.out.println(p.getName() + " has been eliminated.");
            }
        }

        List<Player> active = getActivePlayers();
        if (active.size() <= 1) {
            sessionOver = true;
            gameStarted = false;
            if (!active.isEmpty()) {
                sessionWinnerId = active.get(0).getId();
                sessionWinnerName = active.get(0).getName();
                System.out.println(sessionWinnerName + " wins the session!");
            }
            game = null;
        } else {
            // Clear hands before starting new game — this fixes the card stacking bug
            for (Player p : active) {
                p.resetForNewRound();
            }
            game = new PokerGame(active, new Deck());
            game.startGame();
            turnStartTime = System.currentTimeMillis();
        }
    }

    public List<Player> getActivePlayers() {
        List<Player> active = new ArrayList<>();
        for (Player p : players) {
            if (!eliminatedPlayerIds.contains(p.getId())) active.add(p);
        }
        return active;
    }

    private Player findPlayer(String playerId) {
        return players.stream().filter(p -> p.getId().equals(playerId)).findFirst().orElse(null);
    }

    public PokerGame getGame() {
        return game;
    }
}