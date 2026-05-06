package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;

public class FoldAction implements Action {

    @Override
    public void execute(Player player, GameState gameState) {
        player.fold();
        System.out.println(player.getName() + " folds");
    }

    @Override
    public String getActionType() {
        return "FOLD";
    }
}
