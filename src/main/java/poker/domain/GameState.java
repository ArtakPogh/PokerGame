package poker.domain;

import poker.domain.enums.GamePhase;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class GameState {
    private List<Player> players;
    private Deck deck;
    private List<Card> communityCards;
    private int pot;
    private GamePhase phase;
    private int currentBet;
    private int lastRaiseAmount;
    private int actionsThisRound = 0;

    public GameState(List<Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;
        this.communityCards = new ArrayList<>();
        this.pot = 0;
        this.lastRaiseAmount = 0;
        this.phase = GamePhase.PRE_FLOP;
        this.currentBet = 0;
        this.actionsThisRound = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public int getPot() {
        return pot;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public int getLastRaiseAmount() {
        return lastRaiseAmount;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int bet) {
        this.currentBet = bet;
    }

    public void addCommunityCard(Card card) {
        communityCards.add(card);
    }
    public void setLastRaiseAmount(int amount) {
        this.lastRaiseAmount = amount;
    }

    public void addToPot(int amount) {
        pot += amount;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public boolean isBettingRoundComplete() {
        int activePlayers = 0;
        for (Player p : players) {
            if (!p.isFolded()) activePlayers++;
        }
        if (actionsThisRound < activePlayers) return false;
        for(Player p : players) {
          if(p.isFolded()) continue;
          if (p.getChips() == 0) continue;
          if(p.getCurrentBet() != currentBet)return false;
        }
        return true;
    }

    public int getActivePlayersCount() {
        int count = 0;
        for (Player p : players) {
            if (!p.isFolded()) {
                count++;
            }
        }
        return count;
    }
    public void recordAction(){
      actionsThisRound++;
    }
    public void resetActionsForRaise(){
      actionsThisRound = 1;
    }

    public void resetForNextBettingRound() {
        currentBet = 0;
        actionsThisRound = 0;
        for (Player p : players) {
            p.setCurrentBet(0);
        }
    }

    public void resetHand() {
        deck = new Deck();
        deck.shuffle();
        communityCards.clear();
        pot = 0;
        currentBet = 0;
        actionsThisRound = 0;
        phase = GamePhase.PRE_FLOP;
        for (Player p : players) {
            p.resetForNewRound();
        }
    }
}
