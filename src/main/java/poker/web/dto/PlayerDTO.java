package poker.web.dto;

import poker.domain.Player;

public class PlayerDTO {
    public String id;
    public String name;
    public int chips;
    public boolean folded;
    public int currentBet;

    public PlayerDTO() {}

    public static PlayerDTO from(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.id = player.getId();
        dto.name = player.getName();
        dto.chips = player.getChips();
        dto.folded = player.isFolded();
        dto.currentBet = player.getCurrentBet();
        return dto;
    }
}