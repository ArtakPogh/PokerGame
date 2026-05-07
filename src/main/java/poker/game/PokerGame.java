package poker.game;

import poker.domain.*;
import poker.domain.enums.GamePhase;
import poker.actions.Action;
import java.util.List;

public class PokerGame {
  private GameState gameState;
  private TablePositions tablePositions;
  private TurnManager turnManager;
  
  
  public Player getCurrentPlayer() {
    return turnManager.getCurrentPlayer();
  }
  public PokerGame(List<Player> players, Deck deck) {
        this.gameState = new GameState(players, deck);
        this.turnManager = new TurnManager(players);
        this.tablePositions = new TablePositions(2,6);
    }
  public void startGame() {
        gameState.getDeck().shuffle();
        tablePositions.initialize(gameState.getPlayers().size());
        dealHoleCards();
        gameState.setPhase(GamePhase.PRE_FLOP);
        int first = tablePositions.getFirstToActPreFlop(gameState.getPlayers().size());
        turnManager.setCurrentPlayerIndex(first);
    }
  private void dealHoleCards() {
        for (int i = 0; i < 2; i++) {
            for (Player p : gameState.getPlayers()) {
                p.receiveCard(gameState.getDeck().deal());
            }
        }
    }
  public void handleAction(Action action) {
        Player current = turnManager.getCurrentPlayer();
        if (current == null) throw new IllegalStateException("No current player");
        action.execute(current, gameState);
        if (!turnManager.isRoundOver()) {
            turnManager.nextPlayer();
        } else {
            advancePhase();
        }
    }
  private void advancePhase() {
        switch (gameState.getPhase()) {
            case PRE_FLOP ->{
                dealFlop();
                gameState.setPhase(GamePhase.FLOP);
              }
            case FLOP ->{
                dealTurn();
                gameState.setPhase(GamePhase.TURN);
              }
            case TURN ->{
                dealRiver();
                gameState.setPhase(GamePhase.RIVER);
              }
            case RIVER ->{
                gameState.setPhase(GamePhase.SHOWDOWN);
                determineWinner();
                return;
                }
        }
        int first = tablePositions.getFirstToActPostFlop(gameState.getPlayers().size());
        turnManager.setCurrentPlayerIndex(first);
    }
    private void dealFlop() {
        for (int i = 0; i < 3; i++) {
            gameState.addCommunityCard(gameState.getDeck().deal());
        }
    }

    private void dealTurn() {
        gameState.addCommunityCard(gameState.getDeck().deal());
    }

    private void dealRiver() {
        gameState.addCommunityCard(gameState.getDeck().deal());
    }

    private void determineWinner() {
        Player best = null;
        HandValue bestValue = null;

        for (Player p : gameState.getPlayers()) {
            if (p.isFolded()) continue;
            Hand hand = new Hand(p.getHand(), gameState.getCommunityCards());
            HandValue value = HandEvaluation.evaluate(hand);

            if (best == null || value.compareTo(bestValue) > 0) {
              bestValue = value;
              best = p;
            }
        }
        if (best != null) {
            best.addChips(gameState.getPot());
            System.out.println( best.gameName() + "wins the pot of" + gameState.getPot());
        }
    }
}
