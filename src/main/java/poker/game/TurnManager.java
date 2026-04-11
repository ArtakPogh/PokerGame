class TurnManager {
    private List<Player> players;
    private int currentPlayerIndex;

    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player nextPlayer() {
        int count = players.size();
        int checked = 0;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % count;
            checked++;
        } while (!isActive(players.get(currentPlayerIndex)) && checked < count);
        if (!isActive(players.get(currentPlayerIndex))) return null;
        return players.get(currentPlayerIndex);
    }

    public boolean isRoundOver() {
        int active = 0;
        for (Player p : players) {
            if (isActive(p)) active++;
        }
        return active <= 1;
    }

    private boolean isActive(Player p) {
        return !p.isFolded() && p.getChips() > 0;
    }

    public void reset() {
        currentPlayerIndex = 0;
    }
}
