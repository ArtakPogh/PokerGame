package poker.actions;

import poker.domain.Player;
import poker.domain.GameSate;

public class Action {
    protected final Player player;

    public Action (Player player){
        this.player = player;
    }

    public abstract void execute(GameState gameState);

    public Player getPlayer() {
        return player;
    }

    public abstract String getActionType();
}