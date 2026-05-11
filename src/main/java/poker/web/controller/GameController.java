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
        String name = (request.getName() != null && !request.getName().isBlank())
                ? request.getName()
                : "Player-" + request.getId().substring(0, 6);
        int chips = (request.getChips() > 0) ? request.getChips() : TableConfig.STARTING_CHIPS;

        Player player = new Player(request.getId(), name, chips);
        GameSession session = sessionManager.assignPlayerToSession(player);

        return ResponseEntity.ok(GameStateDTO.from(session, request.getId()));
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
        if (request.getTableId() == null || request.getPlayerId() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "tableId and playerId are required")
            );
        }

        GameSession session = sessionManager.getSession(request.getTableId());
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        Action action = buildAction(request);
        if (action == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Unknown action type: " + request.getType())
            );
        }

        try {
            session.handleAction(request.getPlayerId(), action);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }

        return ResponseEntity.ok(GameStateDTO.from(session, request.getPlayerId()));
    }

    private Action buildAction(ActionRequest request) {
        if (request.getType() == null) return null;
        return switch (request.getType().toUpperCase()) {
            case "FOLD"  -> new FoldAction();
            case "CHECK" -> new CheckAction();
            case "CALL"  -> new CallAction();
            case "BET"   -> new BetAction(request.getAmount());
            case "RAISE" -> new RaiseAction(request.getAmount());
            default      -> null;
        };
    }
}