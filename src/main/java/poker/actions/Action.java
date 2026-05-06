package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;

public interface Action {
    public void execute(Player player, GameState gameState);
    String getActionType();
}
