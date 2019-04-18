package makar.resourcegame.server.game;

import java.util.Objects;
import java.util.logging.Logger;

public class Player {

    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    private String uuid;

    public String getNickName() {
        return nickName;
    }

    private final String nickName;

    public Player(String nickName) {
        LOGGER.info("Creating new player: " + nickName);
        this.nickName = nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return nickName.equals(player.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }

    @Override
    public String toString() {
        return "Player: " + nickName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
