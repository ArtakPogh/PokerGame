package poker.actions;
import poker.domain.Player;
import poker.domain.GameState;

public class CheckAction implements Action {
    @Override
    public void execute(Player player, GameState gameState) {
        if (gameState.getCurrentBet() < gameState.getCurrentBet()) throw new IllegalStateException("Cannot check when there is a bet.");
        System.out.println(player.getName() + " checks");
    }
    @Override
    public String getActionType() {
        return "CHECK";
    }
}
