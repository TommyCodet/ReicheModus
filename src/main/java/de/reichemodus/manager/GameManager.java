package de.reichemodus.manager;

import de.reichemodus.ReicheModus;

public final class GameManager {

    private final ReicheModus plugin;
    private boolean running;

    public GameManager(ReicheModus plugin) {
        this.plugin = plugin;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }
}
