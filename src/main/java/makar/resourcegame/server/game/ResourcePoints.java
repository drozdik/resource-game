package makar.resourcegame.server.game;

import java.util.Collection;

public class ResourcePoints {

    public int getValue() {
        return value;
    }

    private final int value;

    public ResourcePoints(int value) {
        this.value = value;
    }

    public ResourcePoints add(ResourcePoints pointsToAdd) {
        return new ResourcePoints(value + pointsToAdd.value);
    }

    public ResourcePoints subtract(Collection<ResourcePoints> subtractAll) {
        int subtractFrom = value;
        for (ResourcePoints subtract : subtractAll) {
            subtractFrom = subtractFrom - subtract.value;
        }
        return new ResourcePoints(subtractFrom);
    }
}
