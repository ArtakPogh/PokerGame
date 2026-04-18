package poker;

import poker.session.SessionManager;
import poker.session.GameSession;
import poker.domain.Player;
import poker.actions.*;

public class Main {

    public static void main(String[] args) {

        SessionManager manager = new SessionManager();

        Player p1 = new Player("1", "Alice", 1000);
        Player p2 = new Player("2", "Bob", 1000);
        Player p3 = new Player("3", "Charlie", 1000);

        manager.assignPlayerToSession(p1);
        manager.assignPlayerToSession(p2);
        manager.assignPlayerToSession(p3);

        System.out.println("Sessions created:");
        System.out.println(manager.getAllSessions().size());

        System.out.println("Session of Alice:");

        GameSession session = manager.getSessionByPlayer("1");

        if (session != null) {
          System.out.println(session.getSessionId());
        } else {
          System.out.println("No session found for Alice");
        }
        
        Action checkAction = new CheckAction();
        manager.handleAction("1", checkAction);
        manager.handleAction("2", checkAction);
        manager.handleAction("3", checkAction);
}
}
