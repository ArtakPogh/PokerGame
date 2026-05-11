package poker.web.controller;

import org.springframework.web.bind.annotation.*;
import poker.session.*;
import poker.domain.Player;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionManager sessionManager;

    @Autowired
    public SessionController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createSession() {
        GameSession session = sessionManager.createSession();
        return ResponseEntity.ok(Map.of("tableId", session.getSessionId()));
    }

    @PostMapping("/{sessionId}/join")
    public ResponseEntity<GameStateDTO> join(
            @PathVariable String sessionId,
            @RequestBody CreatePlayerRequest request
    ) {
        GameSession session = sessionManager.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        String name = (request.name != null && !request.name.isBlank())
                ? request.name
                : "Player-" + request.id.substring(0, 6);

        int chips = (request.chips > 0) ? request.chips : TableConfig.STARTING_CHIPS;

        Player player = new Player(request.id, name, chips);
        session.addPlayer(player);

        return ResponseEntity.ok(GameStateDTO.from(session, request.id));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<GameStateDTO> getSession(
            @PathVariable String sessionId,
            @RequestParam(required = false, defaultValue = "") String playerId
    ) {
        GameSession session = sessionManager.getSession(sessionId);
        if (session == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(GameStateDTO.from(session, playerId));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> allSessions() {
        List<Map<String, Object>> result = sessionManager.getAllSessions().stream()
                .map(s -> Map.<String, Object>of(
                        "tableId", s.getSessionId(),
                        "players", s.getPlayerCount(),
                        "started", s.isGameStarted()
                ))
                .toList();
        return ResponseEntity.ok(result);
    }
}
