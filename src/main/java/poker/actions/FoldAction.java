package poker.action;

import poker.domain.Player;
import poker.domain.GameState;
import poker.config.TableConfig;

public class FoldAction extends Action {

    public FoldAction(Player player) {
        super(player);
    }

    @Override
    public void execute(GameState gamestate) {
        player.fold();
    }

    @Override
    public String getActionType() {
        return "FOLD";
    }
}
