package poker.domain;

import poker.domain.enums.Suit;
import poker.domain.enums.Rank;
import poker.domain.enums.GamePhase;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Player{
  private final String id;
  private final String name;
  private int chips;
  private List<Card> hand = new ArrayList<>();
  private boolean folded = false;
  
  public Player(String id, String name, int startingChips){
    this.id = id;
    this.name = name;
    this.chips = startingChips;
  }
  
  public void receiveCard(Card card){
    hand.add(card);
  }
  public List<Card> getHand(){
    return Collections.unmodifiableList(hand);
  }
  public void clearHand(){
    hand.clear();
  }
  public void bet(int amount){
    if(amount <= 0) throw new IllegalArgumentException("Bet must be positive");
    if(amount > chips) throw new IllegalArgumentException("Not enough chips");
    chips -= amount;
  }
  public void addChips(int amount){
    chips +=amount;
  }
  public void fold(){
    folded = true;
  }
  public boolean isFolded(){
    return folded;
  }
  public void resetForNewRound(){
    folded = false;
    hand.clear();
  }
  public String getId(){
    return id;
  }
  public String getName(){
    return name;
  }
  public int getChips(){
    return chips;
  }
}
