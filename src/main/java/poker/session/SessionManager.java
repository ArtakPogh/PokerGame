package poker.session;

import poker.game.PokerGame;
import poker.domain.Player;
import poker.actions.Action;
import java.util.*;

public class SessionManager{
  private final Map<String, GameSession> sessions = new HashMap<>();
  private final Map<String, String> playerToSession = new HashMap<>();
  public GameSession assignPlayerToSession(Player player) {
        if (playerToSession.containsKey(player.getId())) {
            return sessions.get(playerToSession.get(player.getId()));
        }
        for (GameSession session : sessions.values()) {
            if (!session.isFull() && !session.isGameStarted()) {
                session.addPlayer(player);
                playerToSession.put(player.getId(), session.getSessionId());
                return session;
            }
        }
        GameSession newSession = createSession();
        newSession.addPlayer(player);
        playerToSession.put(player.getId(), newSession.getSessionId());
        return newSession;
  }
  public GameSession createSession() {
        String sessionId = UUID.randomUUID().toString();
        GameSession session = new GameSession(sessionId);
        sessions.put(sessionId, session);
        return session;
  }
  public void removeSession(String sessionId) {
    GameSession session = sessions.remove(sessionId);
    if (session == null) return;
    for (Player p : session.getPlayers()) {
        playerToSession.remove(p.getId());
    }
  }
  public GameSession getSession(String sessionId) {
        return sessions.get(sessionId);
  }
  public List<GameSession> getAllSessions() {
        return new ArrayList<>(sessions.values());
  }
  public GameSession getSessionByPlayer(String playerId) {
        String sessionId = playerToSession.get(playerId);
        if (sessionId == null) return null;
        return sessions.get(sessionId);
  }
  public void removePlayer(String playerId) {
        GameSession session = getSessionByPlayer(playerId);
        if (session != null) {
            session.removePlayer(playerId);
            playerToSession.remove(playerId);
            if (session.isEmpty()) {
                removeSession(session.getSessionId());
            }
        }
  }
  public void handleAction(String playerId, Action action) {
        GameSession session = getSessionByPlayer(playerId);
        if (session == null) return;
        session.handleAction(playerId, action);
  }
  public void cleanupEmptySessions() {
    Iterator<Map.Entry<String, GameSession>> it = sessions.entrySet().iterator();

    while (it.hasNext()) {
        Map.Entry<String, GameSession> entry = it.next();
        if (entry.getValue().isEmpty()) {
            GameSession session = entry.getValue();
            for (Player p : session.getPlayers()) {
                playerToSession.remove(p.getId());
            }
            it.remove();
        }
    }
  }
}
