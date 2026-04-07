package poker.actions;

public class CheckAction extends Action {
import poker.domain.Player;
import poker.domain.GameState;

    @Override
    public void execute(GameState gameState) {
        if (gameState.getCurrentBet() > 0) {
            throw new IllegalStateException("Cannot check when there is a bet.");
        }
    }

    @Override
    public String getActionType() {
        return "CHECK";
    }
}
