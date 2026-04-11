package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;
import poker.config.TableConfig;

public class BetAction extends Action {
    private final int amount;

    public BetAction(Player player, int amount) {
        super(player);
        if (amount < TableConfig.MIN_BET) {
            throw new IllegalArgumentException(
                    "Bet amount must be at least " + TableConfig.MIN_BET
            );
        }

        if (amount > player.getChips()) {
            throw new IllegalArgumentException(
                    "Player does not have enough chips. Has: " + player.getChips() +
                            ", needs: " + amount
            );
        }
        this.amount = amount;
    }

    @Override
    public void execute(GameState gameState) {
        player.bet(amount);
        gameState.addToPot(amount);
        player.setCurrentBet(player.getCurrentBet() + amount);
    }

    @Override
    public String getActionType() {
        return "BET";
    }

    public int getAmount() {
        return amount;
    }
}