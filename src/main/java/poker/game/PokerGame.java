package poker.game;

import poker.domain.*;
import poker.domain.enums.GamePhase;
import poker.actions.Action;
import poker.config.TableConfig;

import java.util.List;

public class PokerGame {
    private GameState gameState;
    private TablePositions tablePositions;
    private TurnManager turnManager;

    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
    }

    public GameState getGameState() {
        return gameState;
    }

    public PokerGame(List<Player> players, Deck deck) {
        this.gameState = new GameState(players, deck);
        this.turnManager = new TurnManager(players);
        this.tablePositions = new TablePositions(2, 6);
    }

    public void startGame() {
        // Clear hands from any previous round
        for (Player p : gameState.getPlayers()) {
            p.resetForNewRound();
        }

        gameState.getDeck().shuffle();
        tablePositions.initialize(gameState.getPlayers().size());
        dealHoleCards();
        postBlinds();
        gameState.setPhase(GamePhase.PRE_FLOP);

        int first = tablePositions.getFirstToActPreFlop(gameState.getPlayers().size());
        turnManager.setCurrentPlayerIndex(first);
    }

    private void postBlinds() {
        List<Player> players = gameState.getPlayers();
        int sbIdx = tablePositions.getSmallBlindPosition();
        int bbIdx = tablePositions.getBigBlindPosition();

        Player sb = players.get(sbIdx);
        Player bb = players.get(bbIdx);

        int sbAmount = Math.min(TableConfig.SMALL_BLIND, sb.getChips());
        int bbAmount = Math.min(TableConfig.BIG_BLIND, bb.getChips());

        sb.bet(sbAmount);
        sb.setCurrentBet(sbAmount);
        gameState.addToPot(sbAmount);

        bb.bet(bbAmount);
        bb.setCurrentBet(bbAmount);
        gameState.addToPot(bbAmount);

        gameState.setCurrentBet(bbAmount);

        System.out.println(sb.getName() + " posts small blind: " + sbAmount);
        System.out.println(bb.getName() + " posts big blind: " + bbAmount);
    }

    private void dealHoleCards() {
        for (int i = 0; i < 2; i++) {
            for (Player p : gameState.getPlayers()) {
                p.receiveCard(gameState.getDeck().deal());
            }
        }
    }

    public void handleAction(Action action) {
        Player currentPlayer = turnManager.getCurrentPlayer();
        action.execute(currentPlayer, gameState);

        if (gameState.getActivePlayersCount() == 1) {
            awardPotToLastPlayer();
            gameState.setPhase(GamePhase.SHOWDOWN);
            return;
        }

        if (gameState.isBettingRoundComplete()) {
            advancePhase();
        } else {
            turnManager.nextPlayer();
        }
    }

    private void awardPotToLastPlayer() {
        for (Player p : gameState.getPlayers()) {
            if (!p.isFolded()) {
                p.addChips(gameState.getPot());
                System.out.println(p.getName() + " wins pot of " + gameState.getPot() + " (others folded)");
                break;
            }
        }
    }

    private void advancePhase() {
        switch (gameState.getPhase()) {
            case PRE_FLOP -> {
                dealFlop();
                gameState.setPhase(GamePhase.FLOP);
            }
            case FLOP -> {
                dealTurn();
                gameState.setPhase(GamePhase.TURN);
            }
            case TURN -> {
                dealRiver();
                gameState.setPhase(GamePhase.RIVER);
            }
            case RIVER -> {
                gameState.setPhase(GamePhase.SHOWDOWN);
                determineWinner();
                return;
            }
            default -> { return; }
        }
        gameState.resetForNextBettingRound();
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
            System.out.println(best.getName() + " wins the pot of " + gameState.getPot());
        }
    }
}