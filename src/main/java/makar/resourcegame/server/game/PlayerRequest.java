package makar.resourcegame.server.game;

public class PlayerRequest {
    private Player player;
    private int harvestedPoints;

    public PlayerRequest(Player player, int harvestedPoints) {
        this.player = player;
        this.harvestedPoints = harvestedPoints;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getHarvestedPoints() {
        return this.harvestedPoints;
    }

    @Override
    public String toString() {
        return "Player request with " + player + " and points to harvest: " + harvestedPoints;
    }
}
