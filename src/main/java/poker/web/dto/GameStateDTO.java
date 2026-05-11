package poker.web.dto;

import poker.domain.Card;
import poker.domain.GameState;
import poker.domain.Player;
import poker.session.GameSession;

import java.util.List;
import java.util.stream.Collectors;

public class GameStateDTO {
    public String tableId;
    public List<PlayerDTO> players;
    public List<String> communityCards;
    public List<String> playerCards;
    public int pot;
    public int currentBet;
    public int minimumRaise;
    public String phase;
    public String currentPlayerId;
    public String currentPlayerName;
    public int remainingTime;
    public int playerMoney;
    public boolean gameStarted;
    public boolean waitingForPlayers;
    public boolean eliminated;
    public boolean sessionOver;
    public String sessionWinnerName;

    public GameStateDTO() {
    }

    public static GameStateDTO from(GameSession session, String playerId) {
        GameStateDTO dto = new GameStateDTO();
        dto.tableId = session.getSessionId();
        dto.gameStarted = session.isGameStarted();
        dto.sessionOver = session.isSessionOver();
        dto.sessionWinnerName = session.getSessionWinnerName();
        dto.eliminated = session.isEliminated(playerId);

        dto.players = session.getPlayers().stream()
                .map(PlayerDTO::from)
                .collect(Collectors.toList());

        dto.waitingForPlayers = !session.isGameStarted()
                && !session.isSessionOver()
                && session.getPlayerCount() < 2;

        if (!session.isGameStarted() || session.getGame() == null) {
            dto.communityCards = List.of();
            dto.playerCards = List.of();
            dto.pot = 0;
            dto.currentBet = 0;
            dto.minimumRaise = 0;
            dto.phase = session.isSessionOver() ? "SESSION_OVER" : "WAITING";
            dto.currentPlayerId = "";
            dto.currentPlayerName = "";
            dto.remainingTime = 30;
            dto.playerMoney = 0;
            return dto;
        }

        GameState state = session.getGame().getGameState();

        dto.communityCards = state.getCommunityCards().stream()
                .map(Card::toString)
                .collect(Collectors.toList());

        Player me = session.getPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst().orElse(null);

        if (me != null) {
            dto.playerCards = me.getHand().stream()
                    .map(Card::toString)
                    .collect(Collectors.toList());
            dto.playerMoney = me.getChips();
        } else {
            dto.playerCards = List.of();
            dto.playerMoney = 0;
        }

        dto.pot = state.getPot();
        dto.currentBet = state.getCurrentBet();
        dto.minimumRaise = state.getCurrentBet() + 50;
        dto.phase = state.getPhase().name();

        Player current = session.getGame().getCurrentPlayer();
        dto.currentPlayerId = current != null ? current.getId() : "";
        dto.currentPlayerName = current != null ? current.getName() : "";

        // Calculate real remaining time from turn start timestamp
        long elapsed = (System.currentTimeMillis() - session.getTurnStartTime()) / 1000;
        dto.remainingTime = Math.max(0, (int)(30 - elapsed));

        return dto;
    }
}