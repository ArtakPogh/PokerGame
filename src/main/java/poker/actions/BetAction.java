package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;
import poker.config.TableConfig;

public class BetAction implements Action {
    private final int amount;

    public BetAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Player player, GameState gameState) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bet must be positive");
        }

        if (amount > player.getChips()) {
            throw new IllegalArgumentException(
                "Player does not have enough chips. Has: " + player.getChips()
            );
            
        }
        player.bet(amount);
        gameState.addToPot(amount);
    }
    @Override
    public String getActionType() {
        return "BET";
    }
}
