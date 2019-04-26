package makar.resourcegame.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import makar.resourcegame.server.game.Game;
import makar.resourcegame.server.game.GameResponse;
import makar.resourcegame.server.game.GameResult;
import makar.resourcegame.server.game.Player;
import makar.resourcegame.server.game.PlayerRequest;
import makar.resourcegame.server.game.Round;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class GameServerRequestHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(GameServerRequestHandler.class.getName());
    private Iterator<String> nicknames;
    private Game game;
    private Map<String, Player> playersByIp = new HashMap<>();
    private Map<String, Player> playersByUuid = new HashMap<>();


    public GameServerRequestHandler(Game game) {
        this.game = game;
        this.nicknames = new ArrayList(Arrays.asList("player1", "player2", "player3", "player4")).iterator();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            tryDoStuff(exchange);
        } catch (Throwable throwable) {
            LOGGER.warning(exchange.toString());
            System.out.println(throwable);
            throwable.printStackTrace();
        }
        return;
    }

    private void tryDoStuff(HttpExchange exchange) throws IOException {
        String responseContent;
        GameResponse gameResponse;
        //Player player = getPlayerBasedOn(exchange);
        if (exchange.getRequestURI().getPath().startsWith("/favicon")) {
            LOGGER.info("Asked for icon, skip");
            return;
        }
        if (exchange.getRequestURI().getPath().endsWith("restartGame")) {
            playersByIp = new HashMap<>();
            playersByUuid = new HashMap<>();
            nicknames = new ArrayList(Arrays.asList("player1", "player2", "player3", "player4")).iterator();
            game.restart();
            exchange.sendResponseHeaders(200, "Game restarted".getBytes().length);//response code and length
            OutputStream os = exchange.getResponseBody();
            os.write("Game restarted".getBytes());
            os.close();
            return;
        }
        Player player = getPlayerBasedOnCookie(exchange);
        if (exchange.getRequestMethod().equals("POST")) {
            int pointsToHarvest = new PostRequestBodyParser().parsePoints(exchange.getRequestBody());
            PlayerRequest request = new PlayerRequest(player, pointsToHarvest);
            PlayerRequest playerRequest = request;
            LOGGER.info("Player " + playerRequest.getPlayer().getNickName() + " wanted to harvest " + playerRequest.getHarvestedPoints());
            gameResponse = game.handlePlayerReadyToHarvestRequest(playerRequest);
        } else {
            gameResponse = game.updateStateRequest(player);
        }
        if (gameResponse.isGameOver()) {
            List<Round> allRounds = game.getAllRounds();
            List<GameResult.PlayerScore> score = new GameResult().score(allRounds);
            LOGGER.info("Game result");
            for (GameResult.PlayerScore playerScore : score) {
                LOGGER.info(playerScore.getPlayer().getNickName() + " : " + playerScore.getTotalHarvest().getValue());
            }
            responseContent = new GameOverPage(score, gameResponse.getGameOverReason(), player).getContent();
        } else {
            responseContent = new GamePage(gameResponse, player).getContent();
        }

        // write content out
        exchange.getResponseHeaders().add("Set-cookie", "userId=" + player.getUuid());
        exchange.sendResponseHeaders(200, responseContent.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(responseContent.getBytes());
        os.close();
    }

    private Player getPlayerBasedOn(HttpExchange exchange) {
        String ipAddress = exchange.getRemoteAddress().toString();
        if (!playersByIp.containsKey(ipAddress)) {
            playersByIp.put(ipAddress, new Player(nicknames.next()));
        }
        return playersByIp.get(ipAddress);
    }

    private Player getPlayerBasedOnCookie(HttpExchange exchange) {
        List<String> cookie = exchange.getRequestHeaders().get("Cookie");
        if (cookie == null || !cookie.stream().filter(s -> s.startsWith("userId=")).findFirst().isPresent()) {
            String uuid = UUID.randomUUID().toString();
            Player newPlayer = new Player(nicknames.next());
            LOGGER.info("Request without cookie - new player" + newPlayer.getNickName());
            newPlayer.setUuid(uuid);
            playersByUuid.put(uuid, newPlayer);
            return newPlayer;
        } else {
            String cookieValue = cookie.stream().filter(s -> s.startsWith("userId=")).findFirst().get().split("=")[1];
            if (!playersByUuid.containsKey(cookieValue)) {
                Player newPlayer = new Player(nicknames.next());
                newPlayer.setUuid(cookieValue);
                playersByUuid.put(cookieValue, newPlayer);
                return newPlayer;
            } else {
                return playersByUuid.get(cookieValue);
            }
        }
    }

}
