package poker.web.dto;

public class CreatePlayerRequest {
    private String playerId;
    private String name;
    private int chips;
    private String id;
    private String tableId;
    private String type;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }
}
