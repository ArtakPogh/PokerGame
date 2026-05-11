package poker.web.dto;

import poker.domain.Card;
import poker.domain.Player;
import poker.domain.enums.GamePhase;

import java.util.List;

public class GameStateDTO {

    private List<Player> players;
    private List<Card> communityCards;
    private int pot;
    private int currentBet;
    private GamePhase phase;

    public GameStateDTO(
            List<Player> players,
            List<Card> communityCards,
            int pot,
            int currentBet,
            GamePhase phase
    ) {
        this.players = players;
        this.communityCards = communityCards;
        this.pot = pot;
        this.currentBet = currentBet;
        this.phase = phase;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public int getPot() {
        return pot;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public GamePhase getPhase() {
        return phase;
    }
}
public static GameStateDTO from(GameSession session, String playerId) {
    GameStateDTO dto = new GameStateDTO();
    return dto;
}
