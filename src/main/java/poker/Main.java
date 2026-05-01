package poker;

import poker.session.SessionManager;
import poker.domain.Player;
import poker.actions.*;

public class Main {

    public static void main(String[] args) {

        SessionManager manager = new SessionManager();

        // Create players
        Player alice = new Player("1", "Alice", 1000);
        Player bob = new Player("2", "Bob", 1000);
        Player charlie = new Player("3", "Charlie", 1000);

        // Assign to session
        manager.assignPlayerToSession(alice);
        manager.assignPlayerToSession(bob);
        manager.assignPlayerToSession(charlie);

        System.out.println("Sessions created:");
        System.out.println(manager.getAllSessions().size());

        System.out.println("Session of Alice:");
        System.out.println(manager.getSessionByPlayer("1").getSessionId());

        // --- Test actions ---
        Action check = new CheckAction();
        Action bet50 = new BetAction(50);
        Action fold = new FoldAction();

        System.out.println("\n--- ACTIONS ---");

        manager.handleAction("1", check);
        manager.handleAction("2", bet50);
        manager.handleAction("3", fold);

        System.out.println("\nDone.");
    }
}
