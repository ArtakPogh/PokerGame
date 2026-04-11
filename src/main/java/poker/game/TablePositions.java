package poker.game;

import poker.domain.Player;
import java.util.List;

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

    public void initializePositions(List<Player> activePlayers) {
        validatePlayerCount(activePlayers.size());

        dealerPosition = 0;
        calculateBlindPositions(activePlayers.size());
    }


    public void rotatePositions(List<Player> activePlayers) {
        int playerCount = activePlayers.size();
        validatePlayerCount(playerCount);

        dealerPosition = (dealerPosition + 1) % playerCount;
        calculateBlindPositions(playerCount);
    }

    private void calculateBlindPositions(int playerCount) {
        if (playerCount == 2) {
            // Heads-up: dealer is small blind, other player is big blind
            smallBlindPosition = dealerPosition;
            bigBlindPosition = (dealerPosition + 1) % playerCount;
        } else {
            // 3+ players: normal rotation
            smallBlindPosition = (dealerPosition + 1) % playerCount;
            bigBlindPosition = (dealerPosition + 2) % playerCount;
        }
    }


    public Player getDealer(List<Player> activePlayers) {
        validatePlayerList(activePlayers);
        return activePlayers.get(dealerPosition);
    }


    public Player getSmallBlind(List<Player> activePlayers) {
        validatePlayerList(activePlayers);
        return activePlayers.get(smallBlindPosition);
    }


    public Player getBigBlind(List<Player> activePlayers) {
        validatePlayerList(activePlayers);
        return activePlayers.get(bigBlindPosition);
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
        if (playerCount == 2) {
            // Heads-up: dealer acts first pre-flop
            return dealerPosition;
        } else {
            // 3+ players: player after big blind acts first
            return (bigBlindPosition + 1) % playerCount;
        }
    }

    public int getFirstToActPostFlop(int playerCount) {
        if (playerCount == 2) {
            // Heads-up: non-dealer acts first post-flop
            return (dealerPosition + 1) % playerCount;
        } else {
            // 3+ players: player after dealer acts first
            return (dealerPosition + 1) % playerCount;
        }
    }

    private void validatePlayerCount(int count) {
        if (count < minPlayers || count > maxPlayers) {
            throw new IllegalStateException(
                    "Player count must be between " + minPlayers + " and " + maxPlayers
            );
        }
    }

    private void validatePlayerList(List<Player> players) {
        if (players == null) {
            throw new IllegalStateException("Player list cannot be null");
        }
        validatePlayerCount(players.size());
        if (dealerPosition >= players.size()) {
            throw new IllegalStateException("Dealer position out of bounds");
        }
    }
}