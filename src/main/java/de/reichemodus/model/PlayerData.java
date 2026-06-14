package de.reichemodus.model;

import java.util.UUID;

public final class PlayerData {

    private final UUID uniqueId;
    private int lives;
    private boolean eliminated;

    public PlayerData(UUID uniqueId, int lives) {
        this.uniqueId = uniqueId;
        this.lives = lives;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }
}
