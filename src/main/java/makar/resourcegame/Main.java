package makar.resourcegame;

import makar.resourcegame.server.ResourceGameServer;
import makar.resourcegame.server.game.Game;

import java.io.IOException;

public class Main {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] %5$s %n");
    }

    public static void main(String[] args) throws IOException {
        int maxRounds = 3;
        int maxPlayers = 2;
        int amountToGrowPerRound = 10;
        int initialTotalResource = 500;
        Game game = new Game(maxRounds, maxPlayers, amountToGrowPerRound, initialTotalResource);
        new ResourceGameServer().startGame(game);
    }

}
