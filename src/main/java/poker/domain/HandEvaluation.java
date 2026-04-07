package poker.domain;

import poker.domain.enums.Suit;
import poker.domain.enums.Rank;
import poker.domain.enums.GamePhase;
import poker.domain.enums.HandRank;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class HandEvaluation{
    public static HandRank evaluate(Hand hand) {
        if (isRoyalFlush(hand)) return HandRank.ROYAL_FLUSH;
        if (isStraightFlush(hand)) return HandRank.STRAIGHT_FLUSH;
        if (isFourOfAKind(hand)) return HandRank.FOUR_OF_A_KIND;
        if (isFullHouse(hand)) return HandRank.FULL_HOUSE;
        if (isFlush(hand)) return HandRank.FLUSH;
        if (isStraight(hand)) return HandRank.STRAIGHT;
        if (isThreeOfAKind(hand)) return HandRank.THREE_OF_A_KIND;
        if (isTwoPair(hand)) return HandRank.TWO_PAIR;
        if (isPair(hand)) return HandRank.PAIR;
        return HandRank.HIGH_CARD;
    }
    
    //compare stuff
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
