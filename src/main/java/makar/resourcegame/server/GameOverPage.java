package makar.resourcegame.server;

import makar.resourcegame.server.game.GameResponse;
import makar.resourcegame.server.game.GameResult;
import makar.resourcegame.server.game.Player;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GameOverPage {

    private static final String REASON_PLACEHOLDER = "REASON_PLACEHOLDER";
    private final List<GameResult.PlayerScore> result;
    private final String reason;
    private String nickname;

    public GameOverPage(List<GameResult.PlayerScore> result, String reason, Player player) {
        this.result = result;
        this.reason = reason;
        this.nickname = player.getNickName();
    }

    public String getContent() {
        try {
            String pageContent = pageContent("game-over-page.html");
            for (int index = 0, number = 1; index < result.size(); index++, number++) {
                pageContent = pageContent.replaceAll("PLACE_" + number, getRecord(result.get(index)));
            }
            pageContent = pageContent.replaceAll(REASON_PLACEHOLDER, reason);
            pageContent = pageContent.replaceAll("NICKNAME_PLACEHOLDER", nickname);
            return pageContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getRecord(GameResult.PlayerScore score) {
        return score.getPlayer().getNickName() + " " + score.getTotalHarvest().getValue();
    }

    public String pageContent(String fileName) throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
        String content = Files.readString(path, Charset.defaultCharset());
        return content;
    }

}
