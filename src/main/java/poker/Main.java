/*package poker;

import poker.domain.*;
import poker.session.*;src/main/java/poker/Main.java:83: error: incompatible types: Rank cannot be converted to Suit
        alice.receiveCard(new Card(Rank.ACE, Suit.SPADES));
                                       ^
src/main/java/poker/Main.java:84: error: incompatible types: Rank cannot be converted to Suit
        alice.receiveCard(new Card(Rank.ACE, Suit.HEARTS));
                                       ^
src/main/java/poker/Main.java:86: error: incompatible types: Rank cannot be converted to Suit
        bob.receiveCard(new Card(Rank.KING, Suit.CLUBS));
                                     ^
src/main/java/poker/Main.java:87: error: incompatible types: Rank cannot be converted to Suit
        bob.receiveCard(new Card(Rank.KING, Suit.DIAMONDS));
                                     ^
src/main/java/poker/Main.java:91: error: incompatible types: Rank cannot be converted to Suit
                new Card(Rank.TEN, Suit.SPADES),
                             ^
src/main/java/poker/Main.java:92: error: incompatible types: Rank cannot be converted to Suit
                new Card(Rank.TEN, Suit.CLUBS),
                             ^
src/main/java/poker/Main.java:93: error: incompatible types: Rank cannot be converted to Suit
                new Card(Rank.TWO, Suit.HEARTS),
                             ^
src/main/java/poker/Main.java:94: error: incompatible types: Rank cannot be converted to Suit
                new Card(Rank.FIVE, Suit.DIAMONDS),
                             ^
src/main/java/poker/Main.java:95: error: incompatible types: Rank cannot be converted to Suit
                new Card(Rank.SEVEN, Suit.HEARTS)
                             ^
Note: Some messages have been simplified; recompile with -Xdiags:verbose to get full output

import poker.actions.*;

public class Main {

    public static void main(String[] args) {

        SessionManager manager = new SessionManager();

        Player alice = new Player("1", "Alice", 1000);
        Player bob = new Player("2", "Bob", 1000);

        manager.assignPlayerToSession(alice);
        manager.assignPlayerToSession(bob);

        GameSession session = manager.getSessionByPlayer("1");

        System.out.println("=== SESSION INFO ===");
        System.out.println("Session ID: " + session.getSessionId());
        System.out.println("Players: " + session.getPlayerCount());

        System.out.println("\n=== STARTING STACKS ===");
        for (Player p : session.getPlayers()) {
            System.out.println(p.getName() + ": " + p.getChips());
        }

        System.out.println("\n=== ACTIONS ===");

        try {
            manager.handleAction("1", new BetAction(100));
            manager.handleAction("2", new CallAction());

            System.out.println("Phase: " + session.getGame().getGameState().getPhase());
            System.out.println("Current player: " + session.getGame().getCurrentPlayer().getName());

            manager.handleAction("1", new CheckAction());
            manager.handleAction("2", new CheckAction());

            manager.handleAction("1", new CheckAction());
            manager.handleAction("2", new CheckAction());

            manager.handleAction("1", new CheckAction());
            manager.handleAction("2", new CheckAction());

            manager.handleAction("1", new CheckAction());
            manager.handleAction("2", new CheckAction());

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println("\n=== FINAL STACKS ===");
        for (Player p : session.getPlayers()) {
            System.out.println(p.getName() + ": " + p.getChips());
        }

        System.out.println("\n=== POT ===");
        System.out.println(session.getGame().getGameState().getPot());

        System.out.println("\n=== CURRENT BET ===");
        System.out.println(session.getGame().getGameState().getCurrentBet());
    }
}**/
package poker;

import poker.domain.*;
import poker.domain.enums.*;
import poker.game.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== POKER HAND EVALUATION TEST ===\n");

        // Players
        Player alice = new Player("1", "Alice", 1000);
        Player bob = new Player("2", "Bob", 1000);

        // Build hands manually
        alice.receiveCard(new Card(Suit.SPADES, Rank.ACE));
        alice.receiveCard(new Card(Suit.HEARTS, Rank.ACE)); // AA

        bob.receiveCard(new Card(Suit.CLUBS, Rank.KING));
        bob.receiveCard(new Card(Suit.HEARTS, Rank.KING)); // KK

        // Community cards (optional)
        List<Card> community = List.of(
                new Card(Suit.DIAMONDS, Rank.TEN),
                new Card(Suit.SPADES, Rank.JACK),
                new Card(Suit.CLUBS, Rank.QUEEN),
                new Card(Suit.HEARTS, Rank.TWO),
                new Card(Suit.DIAMONDS, Rank.FIVE)
        );

        // Build hands
        Hand aliceHand = new Hand(alice.getHand(), community);
        Hand bobHand = new Hand(bob.getHand(), community);

        // Evaluate
        HandValue aliceValue = HandEvaluation.evaluate(aliceHand);
        HandValue bobValue = HandEvaluation.evaluate(bobHand);

        System.out.println("Alice hand: " + aliceValue.getRank());
        System.out.println("Bob hand: " + bobValue.getRank());

        // Compare
        if (aliceValue.compareTo(bobValue) > 0) {
            System.out.println("\nWinner: Alice");
        } else if (aliceValue.compareTo(bobValue) < 0) {
            System.out.println("\nWinner: Bob");
        } else {
            System.out.println("\nTie");
        }
    }
}
