package poker.domain;

import poker.domain.enums.Suit;
import poker.domain.enums.Rank;
import poker.domain.enums.GamePhase;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards in the deck");
        }
        return cards.remove(cards.size() - 1);
    }

    public int remainingCards() {
        return cards.size();
    }
}
