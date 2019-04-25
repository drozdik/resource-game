package makar.resourcegame.server;

import makar.resourcegame.server.game.GameResponse;
import makar.resourcegame.server.game.Player;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GamePage {

    private static final String TOTAL_POINTS_PLACEHOLDER = "TOTAL_POINTS_PLACEHOLDER";
    private static final String ROUND_NUMBER_PLACEHOLDER = "ROUND_NUMBER_PLACEHOLDER";
    private static final String POINTS_PLACEHOLDER = "POINTS_PLACEHOLDER";
    private String totalPoints;
    private String roundNumber;
    private boolean waitingForRoundOver;
    private String points;
    private String nickName;

    public GamePage(GameResponse gameResponse, Player player) {
        totalPoints = Integer.toString(gameResponse.getTotalResources());
        roundNumber = Integer.toString(gameResponse.getRoundNumber());
        waitingForRoundOver = gameResponse.isWaitForRoundOver();
        points = Integer.toString(gameResponse.getPoints());
        nickName = player.getNickName();
    }

    public String getContent() {
        try {
            String pageContent;
            if (waitingForRoundOver) {
                pageContent = pageContent("game-page-waiting.html");
            } else {
                pageContent = pageContent("game-page-active.html");
            }
            pageContent = pageContent.replaceAll(TOTAL_POINTS_PLACEHOLDER, totalPoints);
            pageContent = pageContent.replaceAll(ROUND_NUMBER_PLACEHOLDER, roundNumber);
            pageContent = pageContent.replaceAll(POINTS_PLACEHOLDER, points);
            pageContent = pageContent.replaceAll("PLAYER_PLACEHOLDER", nickName);
            return pageContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String pageContent(String fileName) throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
        String content = Files.readString(path, Charset.defaultCharset());
        return content;
    }

}
