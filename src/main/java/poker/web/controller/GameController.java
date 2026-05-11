package poker.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poker.actions.*;
import poker.config.TableConfig;
import poker.domain.Player;
import poker.session.GameSession;
import poker.session.SessionManager;
import poker.web.dto.ActionRequest;
import poker.web.dto.CreatePlayerRequest;
import poker.web.dto.GameStateDTO;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final SessionManager sessionManager;

    @Autowired
    public GameController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @PostMapping("/join")
    public ResponseEntity<GameStateDTO> join(@RequestBody CreatePlayerRequest request) {
        String name = (request.name != null && !request.name.isBlank())
                ? request.name
                : "Player-" + request.id.substring(0, 6);
        int chips = (request.chips > 0) ? request.chips : TableConfig.STARTING_CHIPS;

        Player player = new Player(request.id, name, chips);
        GameSession session = sessionManager.assignPlayerToSession(player);

        return ResponseEntity.ok(GameStateDTO.from(session, request.id));
    }


    @GetMapping("/state")
    public ResponseEntity<GameStateDTO> getState(
            @RequestParam String tableId,
            @RequestParam String playerId
    ) {
        GameSession session = sessionManager.getSession(tableId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(GameStateDTO.from(session, playerId));
    }


    @PostMapping("/action")
    public ResponseEntity<?> action(@RequestBody ActionRequest request) {
        if (request.tableId == null || request.playerId == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "tableId and playerId are required")
            );
        }

        GameSession session = sessionManager.getSession(request.tableId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        Action action = buildAction(request);
        if (action == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Unknown action type: " + request.type)
            );
        }

        try {
            session.handleAction(request.playerId, action);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }

        return ResponseEntity.ok(GameStateDTO.from(session, request.playerId));
    }

    private Action buildAction(ActionRequest request) {
        if (request.type == null) return null;
        return switch (request.type.toUpperCase()) {
            case "FOLD"  -> new FoldAction();
            case "CHECK" -> new CheckAction();
            case "CALL"  -> new CallAction();
            case "BET"   -> new BetAction(request.amount);
            case "RAISE" -> new RaiseAction(request.amount);
            default      -> null;
        };
    }
}
