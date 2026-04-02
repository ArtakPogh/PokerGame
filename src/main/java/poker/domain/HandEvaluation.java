package poker.domain;

import poker.domain.enums.Suit;
import poker.domain.enums.Rank;
import poker.domain.enums.GamePhase;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class HandEvaluation{
    public static int evaluate(Hand hand) {
        List<Card> allCards = hand.getAllCards();
        int highestRank = 0;
        for (Card c : allCards) {
            if (c.getRank().getValue() > highestRank) {
                highestRank = c.getRank().getValue();
            }
        }
        return highestRank;
    }
}
