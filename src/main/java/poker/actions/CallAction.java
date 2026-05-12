package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;

public class CallAction implements Action {
    @Override
    public void execute(Player player, GameState gameState) {
        int toCall = gameState.getCurrentBet() - player.getCurrentBet();

        // FIX Bug 3: instead of throwing, cap at available chips (all-in call)
        int actualCall = Math.min(toCall, player.getChips());

        if (actualCall <= 0) {
            // Nothing to call (player already matched the bet)
            System.out.println(player.getName() + " checks (nothing to call)");
            gameState.recordAction();
            return;
        }

        player.bet(actualCall);
        // If all-in, currentBet reflects what they actually put in (may be less than table bet)
        player.setCurrentBet(player.getCurrentBet() + actualCall);
        gameState.addToPot(actualCall);
        gameState.recordAction();

        if (actualCall < toCall) {
            System.out.println(player.getName() + " calls all-in for " + actualCall);
        } else {
            System.out.println(player.getName() + " calls");
        }
    }

    @Override
    public String getActionType() {
        return "CALL";
    }
}