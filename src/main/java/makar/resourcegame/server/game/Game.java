package makar.resourcegame.server.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/*
 * move harvesting into Round? Round.startWithAmountOfResources(int x) // or even currentRound = roundIterator.next().startWithAmountOfResources(currentRound.getResourcesLeft()); // round builders?
 * Round.getResult > RoundResult a) resources left b) drained all resources
 * then Game operates only on rounds and last round result
 * handle this player already did request in this round (idempotent) // in the round
 * */
public class Game {

    private List<Round> rounds = new ArrayList<>();
    private Iterator<Round> roundsIterator;
    private Round currentRound;

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
    private int maxRounds;
    private int maxPlayers;
    private int amountToGrowPerRound;
    private int initialTotalResource;

    public Game(int maxRounds, int maxPlayers, int amountToGrowPerRound, int initialTotalResource) {
        this.maxRounds = maxRounds;
        this.maxPlayers = maxPlayers;
        this.amountToGrowPerRound = amountToGrowPerRound;
        this.initialTotalResource = initialTotalResource;

        LOGGER.info(String.format("START GAME with %s rounds, %s players, growing %s points every round, initial resource %s ", maxRounds, maxPlayers, amountToGrowPerRound, initialTotalResource));
        for (int i = 0; i < maxRounds; i++) {
            rounds.add(new Round(maxPlayers, i + 1, new ResourcePoints(amountToGrowPerRound)));
        }
        roundsIterator = rounds.iterator();
        currentRound = roundsIterator.next();
        currentRound.startWith(new ResourcePoints(initialTotalResource-amountToGrowPerRound));
    }

    public synchronized GameResponse handlePlayerReadyToHarvestRequest(PlayerRequest request) {
        currentRound.processReadyToHarvestRequest(request);
        if (currentRound.isOver()) {
            if (currentRound.getRoundResource().getValue() <= 0) {
                return gameOverResponse("Resource drained");
            } else if (hasMoreRounds()) {
                startNextRound();
                return activeRoundResponse(request.getPlayer());
            } else {
                return gameOverResponse("Rounds run out");
            }
        } else {
            return waitForRoundOverResponse(request.getPlayer());
        }
    }

    private GameResponse activeRoundResponse(Player player) {
        GameResponse response = gameResponse();
        if (currentRound.getRoundNumber() == 1) {
            response.setPoints(0);
        } else {
            int prevRoundPlayerHarvest = getPrevRound().getPlayerHarvest(player).getValue();
            response.setPoints(prevRoundPlayerHarvest);
        }
        return response;
    }

    private Round getPrevRound() {
        return rounds.get(rounds.indexOf(currentRound) - 1);
    }

    private GameResponse waitForRoundOverResponse(Player player) {
        GameResponse response = gameResponse();
        int prevRoundPlayerHarvest = currentRound.getPlayerHarvest(player).getValue();
        response.setPoints(prevRoundPlayerHarvest);
        response.setWaitForRoundOver(true);
        return response;
    }

    private boolean hasMoreRounds() {
        return roundsIterator.hasNext();
    }

    private void startNextRound() {
        ResourcePoints resourcesLeft = currentRound.getRoundResource();
        currentRound = roundsIterator.next();
        currentRound.startWith(resourcesLeft);
    }

    private GameResponse gameResponse() {
        GameResponse response = new GameResponse();
        response.setTotalResources(currentRound.getRoundResource().getValue());
        response.setRoundNumber(currentRound.getRoundNumber());
        return response;
    }

    private GameResponse gameOverResponse(String reason) {
        GameResponse response = gameResponse();
        response.setGameOver(true);
        response.setGameOverReason(reason);
        return response;
    }

    // should be refactored
    public GameResponse updateStateRequest(Player player) {
        if (currentRound.isOver()) {
            if (currentRound.getRoundResource().getValue() <= 0) {
                return gameOverResponse("Resource drained");
            } else {
                return gameOverResponse("Rounds run out");
            }
        } else {
            boolean submitted = currentRound.isSubmitted(player);
            if (submitted) {
                return waitForRoundOverResponse(player);
            } else {
                return activeRoundResponse(player);
            }
            // round is not over
            // should wait for others
            // or new round started and should set harvest points
        }
    }

    public List<Round> getAllRounds() {
        List<Round> finished = new ArrayList<>();
        for (Round round : rounds) {
            if (round.isOver()) {
                finished.add(round);
            }
        }
        return finished;
    }

    public void restart() {
        rounds = new ArrayList<>();
        for (int i = 0; i < maxRounds; i++) {
            rounds.add(new Round(maxPlayers, i + 1, new ResourcePoints(amountToGrowPerRound)));
        }
        roundsIterator = rounds.iterator();
        currentRound = roundsIterator.next();
        currentRound.startWith(new ResourcePoints(initialTotalResource-amountToGrowPerRound));

        LOGGER.info(String.format("RESTART GAME with %s rounds, %s players, growing %s points every round, initial resource %s ", maxRounds, maxPlayers, amountToGrowPerRound, initialTotalResource));

    }
}
