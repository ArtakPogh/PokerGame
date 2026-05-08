package poker.game;

import poker.domain.Player;
import poker.domain.GameState;
import java.util.List;

public class TurnManager {
    private List<Player> players;
    private int currentPlayerIndex;

    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
       if (players.isEmpty()) return null;
       return players.get(currentPlayerIndex);
    }

    public Player nextPlayer() {
        int count = players.size();
        int checked = 0;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % count;
            checked++;
        } while (!isActive(players.get(currentPlayerIndex)) && checked < count);

        if (!isActive(players.get(currentPlayerIndex))) throw new IllegalStateException("No active players left");
        return players.get(currentPlayerIndex);
    }

    public boolean isRoundOver(GameState gameState) {
        for (Player p : players) {
            if (p.isFolded()) {
                continue;
            }
            if (p.getCurrentBet() !=
                    gameState.getCurrentBet()) {
                return false;
            }
        }
        return true;
    }

    private boolean isActive(Player p) {
        return !p.isFolded() && p.getChips() > 0;
    }
    
    public void setCurrentPlayerIndex(int index) {
      if (index < 0 || index >= players.size()) throw new IllegalArgumentException("Invalid index");
      this.currentPlayerIndex = index;
    }
}
