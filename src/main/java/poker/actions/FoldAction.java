package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;

public class FoldAction extends Action {

    public FoldAction(Player player) {
        super(player);
    }

    @Override
    public void execute(GameState gameState) {
        player.fold();
    }

    @Override
    public String getActionType() {
        return "FOLD";
    }
}