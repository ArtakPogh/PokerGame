package poker.web.controller;

import org.springframework.web.bind.annotation.*;
import poker.session.*;
import poker.actions.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final SessionManager sessionManager = new SessionManager();

    @PostMapping("/{playerId}/action")
    public String action(
            @PathVariable String playerId,
            @RequestBody Action action
    ) {
        sessionManager.handleAction(playerId, action);
        return "ok";
    }
}
