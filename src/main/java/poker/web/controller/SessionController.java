package poker.web.controller;

import org.springframework.web.bind.annotation.*;
import poker.session.*;
import poker.domain.Player;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionManager sessionManager = new SessionManager();

    @PostMapping("/create")
    public GameSession createSession() {
        return sessionManager.createSession();
    }

    @PostMapping("/{sessionId}/join")
    public String join(
            @PathVariable String sessionId,
            @RequestBody Player player
    ) {
        GameSession session = sessionManager.getSession(sessionId);
        session.addPlayer(player);
        return "joined";
    }

    @GetMapping("/{sessionId}")
    public GameSession getSession(@PathVariable String sessionId) {
        return sessionManager.getSession(sessionId);
    }

    @GetMapping
    public List<GameSession> allSessions() {
        return sessionManager.getAllSessions();
    }
}