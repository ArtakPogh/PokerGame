package poker.actions;

import poker.domain.Player;
import poker.domain.GameState;

public class CallAction implements Action{
  @Override
  public void execute(Player player, GameState gameState){
    int toCall = gameState.getCurrentBet() - player.getCurrentBet();
    if (toCall > player.getChips()) throw new IllegalStateException("Not enough chips to call");
        player.bet(toCall);
        player.setCurrentBet(gameState.getCurrentBet());
        gameState.addToPot(toCall);
        System.out.println(player.getName() + " calls");
    }
    @Override
    public String getActionType() {
        return "CALL";
    }
} 
