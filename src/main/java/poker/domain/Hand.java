package poker.domain;

import poker.domain.enums.Suit;
import poker.domain.enums.Rank;
import poker.domain.enums.GamePhase;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Hand {
    private List<Card> playerCards;      
    private List<Card> communityCards;  

    public Hand(List<Card> playerCards, List<Card> communityCards) {
        this.playerCards = new ArrayList<>(playerCards);
        this.communityCards = new ArrayList<>(communityCards);
    }

    public List<Card> getPlayerCards() {
        return new ArrayList<>(playerCards);    
    }

    public List<Card> getCommunityCards() {
        return new ArrayList<>(communityCards);
    }

    public List<Card> getAllCards() {
        List<Card> all = new ArrayList<>(playerCards);
        all.addAll(communityCards);
        return all;
    }
}
