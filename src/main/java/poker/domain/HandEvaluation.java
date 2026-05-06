package poker.domain;

import poker.domain.enums.*;

import java.util.*;

public class HandEvaluation {
    public static HandValue evaluate(Hand hand) {
        List<Card> cards = new ArrayList<>(hand.getAllCards());
        cards.sort((a, b) -> b.getRank().getValue() - a.getRank().getValue());
        Map<Integer, Integer> counts = getRankCounts(cards);
        boolean flush = isFlush(cards);
        int straightHigh = getStraightHigh(cards);
        List<Integer> fours = getOfAKind(counts, 4);
        List<Integer> threes = getOfAKind(counts, 3);
        List<Integer> pairs = getOfAKind(counts, 2);
        if (flush && straightHigh > 0) return new HandValue(HandRank.STRAIGHT_FLUSH, List.of(straightHigh));
        if (!fours.isEmpty()) {
            int kicker = getHighestExcluding(cards, fours.get(0));
            return new HandValue(HandRank.FOUR_OF_A_KIND, List.of(fours.get(0), kicker));
        }
        if (flush) return new HandValue(HandRank.FLUSH, getTopRanks(cards, 5));
        if (straightHigh > 0) return new HandValue(HandRank.STRAIGHT, List.of(straightHigh));
        if (!threes.isEmpty()) {
            List<Integer> kickers = getKickers(cards, List.of(threes.get(0)), 2);
            return new HandValue(HandRank.THREE_OF_A_KIND, merge(List.of(threes.get(0)), kickers));
        }
        if (pairs.size() >= 2) {
            int highPair = pairs.get(0);
            int lowPair = pairs.get(1);
            int kicker = getHighestExcluding(cards, highPair, lowPair);
            return new HandValue(HandRank.TWO_PAIR, List.of(highPair, lowPair, kicker));
        }
        if (pairs.size() == 1) {
            int pair = pairs.get(0);
            List<Integer> kickers = getKickers(cards, List.of(pair), 3);
            return new HandValue(HandRank.PAIR, merge(List.of(pair), kickers));
        }
        return new HandValue(HandRank.HIGH_CARD, getTopRanks(cards, 5));
    }

    private static Map<Integer, Integer> getRankCounts(List<Card> cards) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Card c : cards) {
            int v = c.getRank().getValue();
            map.put(v, map.getOrDefault(v, 0) + 1);
        }
        return map;
    }

    private static boolean isFlush(List<Card> cards) {
        if (cards.isEmpty()) return false;
        Suit suit = cards.get(0).getSuit();
        for (Card c : cards) {
            if (c.getSuit() != suit) return false;
        }
        return true;
    }

    private static int getStraightHigh(List<Card> cards) {
        Set<Integer> set = new HashSet<>();
        for (Card c : cards) set.add(c.getRank().getValue());
        List<Integer> values = new ArrayList<>(set);
        Collections.sort(values);
        int best = -1;
        int count = 1;
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) == values.get(i - 1) + 1) {
                count++;
                if (count >= 5) best = values.get(i);
            } else count = 1;
        }
        if (values.containsAll(List.of(14, 2, 3, 4, 5))) return 5;
        return best;
    }

    private static List<Integer> getOfAKind(Map<Integer, Integer> counts, int n) {
        List<Integer> result = new ArrayList<>();
        for (var entry : counts.entrySet()) {
            if (entry.getValue() == n) result.add(entry.getKey());
        }
        result.sort(Collections.reverseOrder());
        return result;
    }

    private static int getHighestExcluding(List<Card> cards, int... excluded) {
        Set<Integer> ex = new HashSet<>();
        for (int e : excluded) ex.add(e);
        for (Card c : cards) {
            int v = c.getRank().getValue();
            if (!ex.contains(v)) return v;
        }
        return 0;
    }

    private static List<Integer> getKickers(List<Card> cards, List<Integer> excluded, int count) {
        List<Integer> result = new ArrayList<>();
        for (Card c : cards) {
            int v = c.getRank().getValue();
            if (!excluded.contains(v)) {
                result.add(v);
                if (result.size() == count) break;
            }
        }
        return result;
    }

    private static List<Integer> getTopRanks(List<Card> cards, int n) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < Math.min(n, cards.size()); i++) res.add(cards.get(i).getRank().getValue());
        return res;
    }

    private static List<Integer> merge(List<Integer> a, List<Integer> b) {
        List<Integer> res = new ArrayList<>(a);
        res.addAll(b);
        return res;
    }
}
