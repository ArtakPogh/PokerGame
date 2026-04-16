package poker.game;

public class TablePositions {
    private int dealerPosition;
    private int smallBlindPosition;
    private int bigBlindPosition;
    private final int minPlayers = 2;
    private final int maxPlayers = 6;

    public TablePositions() {
        this.dealerPosition = 0;
        this.smallBlindPosition = 0;
        this.bigBlindPosition = 0;
    }

    public void initializePositions(int playerCount) {
        validatePlayerCount(int playerCount);
        dealerPosition = 0;
        calculateBlindPositions(int playerCount);
    }


    public void rotatePositions(int playerCount) {
        validatePlayerCount(playerCount);
        dealerPosition = (dealerPosition + 1) % playerCount;
        calculateBlindPositions(playerCount);
    }

    private void calculateBlindPositions(int playerCount) {
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

    private void validatePlayerCount(int count) {
        if (count < minPlayers || count > maxPlayers) {
            throw new IllegalStateException(
                    "Player count must be between " + minPlayers + " and " + maxPlayers
            );
        }
    }
}
