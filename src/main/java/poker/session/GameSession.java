package poker.session;

import poker.game.PokerGame;
import poker.domain.Player;
import poker.actions.Action;
import poker.domain.Deck;
import java.util.*;

public class GameSession{
    private final String sessionId;
    private final List<Player> players = new ArrayList<>();
    private final int maxPlayers = 6;
    private PokerGame game;
    private boolean gameStarted = false;
    
    public GameSession(String sessionId){
      this.sessionId = sessionId;
    }
     public String getSessionId() {
        return sessionId;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
    public int getPlayerCount() {
      return players.size();
    }
    
    public boolean addPlayer(Player player){
       if (isFull() || hasPlayer(player.getId())) {
            return false;
        }
        players.add(player);
        if (canStartGame()) {
            startGame();
        }
        return true;
    }
    public void removePlayer(String playerId) {
        boolean removed = players.removeIf(p -> p.getId().equals(playerId));
        if (removed && players.size() < 2 && gameStarted) {
            stopGame();
        }
    }
     public boolean hasPlayer(String playerId) {
        return players.stream().anyMatch(p -> p.getId().equals(playerId));
    }
     private boolean canStartGame() {
        return players.size() >= 2 && !gameStarted;
    }
    public void startGame() {
        if (gameStarted) return;
        game = new PokerGame(new ArrayList<>(players), new Deck());
        game.startGame();
        gameStarted = true;
    }
     public void stopGame() {
        gameStarted = false;
        game = null;
    }
     public void handleAction(String playerId, Action action) {
        if (!gameStarted) return;
        Player player = findPlayer(playerId);
        if (player == null) return;
        if (!player.equals(game.getCurrentPlayer())) return;
        game.handleAction(action);
    }
     private Player findPlayer(String playerId) {
        return players.stream().filter(p -> p.getId().equals(playerId)).findFirst().orElse(null);
    }
     public PokerGame getGameState() {
        return game;
    }
}
