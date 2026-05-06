package poker.domain;

import poker.domain.enums.HandRank;

import java.util.*;

public class HandValue implements Comparable<HandValue> {
    private final HandRank rank;
    private final List<Integer> tiebreakers;

    public HandValue(HandRank rank, List<Integer> tiebreakers) {
        this.rank = rank;
        this.tiebreakers = tiebreakers;
    }

    public HandRank getRank() {
        return rank;
    }

    @Override
    public int compareTo(HandValue other) {
        if (this.rank != other.rank) return Integer.compare(this.rank.getValue(), other.rank.getValue());
        for (int i = 0; i < Math.min(this.tiebreakers.size(), other.tiebreakers.size()); i++) {
            int cmp = Integer.compare(this.tiebreakers.get(i), other.tiebreakers.get(i));
            if (cmp != 0) return cmp;
        }
        return 0;
    }
}
