package poker.action;

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

        if (amount > player.getMoney()) {
            throw new IllegalArgumentException(
                    "Player does not have enough money. Has: " + player.getMoney() +
                            ", needs: " + amount
            );
        }
        this.amount = amount;
    }

    @Override
    public void execute(GameState gamestate) {
        player.bet(amount);
        gamestate.addToPot(amount);
    }

    @Override
    public String getActionType() {
        return "BET";
    }

    public int getAmount() {
        return amount;
    }

}