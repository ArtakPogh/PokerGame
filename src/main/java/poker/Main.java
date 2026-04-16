import poker.domain.*;
import poker.game.*;
import poker.actions.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Deck deck = new Deck();

        Player p1 = new Player("1", "Alice", 1000);
        Player p2 = new Player("2", "Bob", 1000);

        List<Player> players = Arrays.asList(p1, p2);

        PokerGame game = new PokerGame(players, deck);

        game.startGame();

        // simulate simple actions
        game.handleAction(new CheckAction());
        game.handleAction(new CheckAction());

        game.handleAction(new CheckAction());
        game.handleAction(new CheckAction());

        game.handleAction(new CheckAction());
        game.handleAction(new CheckAction());

        game.handleAction(new CheckAction());
        game.handleAction(new CheckAction());

        System.out.println("Game finished");
    }
}
