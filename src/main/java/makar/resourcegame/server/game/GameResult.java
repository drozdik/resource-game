package makar.resourcegame.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameResult {
    // place // player nickname // score

    List<PlayerScore> result = new ArrayList<>();

    public List<PlayerScore> score(List<Round> rounds) {
        List<Player> players = rounds.get(0).getAllPlayers();
        for (Player player : players) {
            PlayerScore score = new PlayerScore(player);
            score.addScoreFromAllRounds(rounds);
            result.add(score);
        }
        Collections.sort(result, new Comparator<PlayerScore>() {
            @Override
            public int compare(PlayerScore o1, PlayerScore o2) {
                if (o1.getTotalHarvest().getValue() > o2.getTotalHarvest().getValue()) {
                    return -1;
                } else if (o1.getTotalHarvest().getValue() < o2.getTotalHarvest().getValue()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return result;
    }

    public static class PlayerScore {
        public Player getPlayer() {
            return player;
        }

        public ResourcePoints getTotalHarvest() {
            return totalHarvest;
        }

        private final Player player;
        private ResourcePoints totalHarvest = new ResourcePoints(0);

        public PlayerScore(Player player) {
            this.player = player;
        }

        public void addScoreFromRound(Round round) {
            ResourcePoints roundHarvest = round.getPlayerHarvest(player);
            totalHarvest = totalHarvest.add(roundHarvest);
        }

        public void addScoreFromAllRounds(List<Round> rounds) {
            for (Round round : rounds) {
                addScoreFromRound(round);
            }
        }
    }
}
