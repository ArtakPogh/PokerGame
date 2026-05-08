package poker.actions;

import poker.domain.GameState;
import poker.domain.Player;

public class RaiseAction implements Action {

    private final int raiseAmount;

    public RaiseAction(int raiseAmount) {
        this.raiseAmount = raiseAmount;
    }

    @Override
    public void execute(Player player, GameState gameState) {
        int totalBet = gameState.getCurrentBet() + raiseAmount;

        int toPay =  totalBet - player.getCurrentBet();

        if (raiseAmount <= 0) {
            throw new IllegalArgumentException(
                    "Raise must be positive"
            );
        }
        if (toPay > player.getChips()) {
            throw new IllegalStateException(
                    "Not enough chips to raise"
            );
        }
        player.bet(toPay);
        player.setCurrentBet(totalBet);
        gameState.setCurrentBet(totalBet);
        gameState.addToPot(toPay);
        System.out.println(player.getName() + " raises to " + totalBet);
    }

    @Override
    public String getActionType() {
        return "RAISE";
    }
}