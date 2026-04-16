package poker.game;

public class TablePositions {
    private int dealerPosition;
    private int smallBlindPosition;
    private int bigBlindPosition;
    private final int minPlayers;
    private final int maxPlayers;

    public TablePositions(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.dealerPosition = 0;
    }

    public void initialize(int playerCount) {
        validate(playerCount);
        dealerPosition = 0;
        calculateBlindPosition(playerCount);
    }


    public void rotate(int playerCount) {
        dealerPosition = (dealerPosition + 1) % playerCount;
        calculateBlindPosition(playerCount);
    }

    private void calculateBlindPosition(int playerCount) {
        if (playerCount == 2) {
            smallBlindPosition = dealerPosition;
            bigBlindPosition = (dealerPosition + 1) % playerCount;
        } else {
            smallBlindPosition = (dealerPosition + 1) % playerCount;
            bigBlindPosition = (dealerPosition + 2) % playerCount;
        }
    }

    public int getDealerPosition() {
        return dealerPosition;
    }


    public int getSmallBlindPosition() {
        return smallBlindPosition;
    }

    public int getBigBlindPosition() {
        return bigBlindPosition;
    }

    public int getFirstToActPreFlop(int playerCount) {
        return (playerCount == 2)? dealerPosition : (bigBlindPosition + 1) % playerCount;
    }

    public int getFirstToActPostFlop(int playerCount) {
       return (playerCount == 2)? (dealerPosition + 1) % playerCount : (dealerPosition + 1) % playerCount;
    }

    private void validate(int count) {
        if (count < minPlayers || count > maxPlayers) {
            throw new IllegalStateException(
                    "Player count must be between " + minPlayers + " and " + maxPlayers
            );
        }
    }
}
