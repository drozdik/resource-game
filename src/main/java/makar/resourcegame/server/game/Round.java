package makar.resourcegame.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Round {

    private static final Logger LOGGER = Logger.getLogger(Round.class.getName());

    private int roundNumber;
    private int playersNumber;
    private ResourcePoints roundResource;
    private ResourcePoints amountToGrow;
    private Map<Player, ResourcePoints> playersReadyToHarvest = new HashMap<>();

    public Round(int playersNumber, int roundNumber, ResourcePoints amountToGrow) {
        this.playersNumber = playersNumber;
        this.roundNumber = roundNumber;
        this.amountToGrow = amountToGrow;
    }

    public void startWith(ResourcePoints roundResource) {
        LOGGER.info("ROUND " + roundNumber + " STARTED");
        this.roundResource = roundResource;
        growResourcesBy(amountToGrow);
    }

    private void growResourcesBy(ResourcePoints amountToGrowPerRound) {
        roundResource = roundResource.add(amountToGrowPerRound);
    }

    public void processReadyToHarvestRequest(PlayerRequest playerRequest) {
        if (allPlayersMadeRequest()) {
            return;
        }
        playersReadyToHarvest.put(playerRequest.getPlayer(), new ResourcePoints(playerRequest.getHarvestedPoints()));
    }

    private boolean allPlayersMadeRequest() {
        if (playersReadyToHarvest.size() > playersNumber) {
            throw new RuntimeException("Players identity problem");
        }
        return playersReadyToHarvest.size() == playersNumber;
    }

    public boolean isOver() {
        return allPlayersMadeRequest();
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public ResourcePoints getRoundResource() {
        if (isOver()) {
            return roundResource.subtract(playersReadyToHarvest.values());
        }
        return roundResource;
    }

    @Override
    public String toString() {
        return "Round " + roundNumber;
    }

    public boolean isSubmitted(Player player) {
        return playersReadyToHarvest.containsKey(player);
    }

    public ResourcePoints getPlayerHarvest(Player player) {
        return playersReadyToHarvest.get(player);
    }

    public List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(playersReadyToHarvest.keySet());
        return allPlayers;
    }
}
