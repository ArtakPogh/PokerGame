package poker.domain;

import poker.domain.enums.Suit;
import poker.domain.enums.Rank;
import poker.domain.enums.GamePhase;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class GameState{
  private List<Player> players;
  private Deck deck;
  private List<Card> communityCards;
  private int pot;
  private GamePhase phase;
  private int currentPlayerIndex;
  
  public GameState(List<Player> players, Deck deck){
    this.players = players;
    this.deck = deck;
    this.communityCards = new ArrayList<>();
    this.pot = 0;
    this.phase = GamePhase.PRE_FLOP;
    this.currentPlayerIndex = 0;
  }
  public List<Player> getPlayers(){return players;}
  public Deck getDeck(){return deck;}
  public List<Card> getCommunityCards(){return communityCards;}
  public int getPot(){return pot;}
  public GamePhase getPhase(){return phase;}
  public int getCurrentPlayerIndex() {return currentPlayerIndex; }
  
  public void addCommunityCard(Card card){
    communityCards.add(card);
  }
  
  public void addToPot(int amount){
    pot += amount;
  }
  
  public void setPhase(GamePhase phase) {
      this.phase = phase;
  }
  public void nextPlayer() {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }
  public void resetHand() {
      deck.shuffle();
      communityCards.clear();
      pot = 0;
      phase = GamePhase.PRE_FLOP;
      currentPlayerIndex = 0;
      for (Player p : players) {
          p.resetForNewRound();
      }
  }
}
