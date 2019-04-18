package makar.resourcegame.server.game;

public class GameResponse {
    private int totalResources;
    private int roundNumber;
    private boolean gameOver;
    private String gameOverReason;
    private boolean waitForRoundOver;
    private int points;

    public void setTotalResources(int totalResources) {
        this.totalResources = totalResources;
    }

    public int getTotalResources() {
        return totalResources;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOverReason(String gameOverReason) {
        this.gameOverReason = gameOverReason;
    }

    public String getGameOverReason() {
        return gameOverReason;
    }

    @Override
    public String toString() {
        return
                " totalResources: " + totalResources + "\n"
                        + " roundNumber: " + roundNumber + "\n"
                        + " gameOver: " + gameOver + "\n"
                        + " gameOverReason: " + gameOverReason;
    }

    public boolean isWaitForRoundOver() {
        return waitForRoundOver;
    }

    public void setWaitForRoundOver(boolean waitForRoundOver) {
        this.waitForRoundOver = waitForRoundOver;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
