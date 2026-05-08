package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;

public class BetAction implements Action {
    private final int amount;

    public BetAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Player player, GameState gameState) {
        if (gameState.getCurrentBet() > 0) {
            throw new IllegalStateException(
                    "Cannot bet after betting started. Use raise."
            );
        }
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Bet must be positive"
            );
        }
        if (amount > player.getChips()) {
            throw new IllegalStateException(
                    "Not enough chips"
            );
        }
        player.bet(amount);
        player.setCurrentBet(amount);
        gameState.setCurrentBet(amount);
        gameState.addToPot(amount);
        System.out.println(player.getName() + " bets " + amount);
    }

    @Override
    public String getActionType() {
        return "BET";
    }
}
