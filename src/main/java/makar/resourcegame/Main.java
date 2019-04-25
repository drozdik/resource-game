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
        int portNumber = Integer.parseInt(args[0]);
        int maxRounds = 10;
        int maxPlayers = 4;
        int amountToGrowPerRound = 45;
        int initialTotalResource = 100;
        Game game = new Game(maxRounds, maxPlayers, amountToGrowPerRound, initialTotalResource);
        new ResourceGameServer(portNumber).startGame(game);
    }

}
