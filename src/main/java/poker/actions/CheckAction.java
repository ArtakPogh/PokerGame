package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;

public class CheckAction extends Action {

    public CheckAction(Player player) {
        super(player);
    }

    @Override
    public void execute(GameState gameState) {
        if (gameState.getCurrentBet() > 0) {
            throw new IllegalStateException("Cannot check when there is a bet.");
        }
        // Check is a no-op, just passes the action
    }

    @Override
    public String getActionType() {
        return "CHECK";
    }
}