package de.reichemodus.manager;

import de.reichemodus.ReicheModus;

public final class ConfigManager {

    private final ReicheModus plugin;

    public ConfigManager(ReicheModus plugin) {
        this.plugin = plugin;
    }

    public boolean isBananaDecayEnabled() {
        return plugin.getConfig().getBoolean("banana-decay-enabled", true);
    }

    public int getBananaDecayRadius() {
        return plugin.getConfig().getInt("banana-decay-radius", 30);
    }

    public int getCombatSeconds() {
        return plugin.getConfig().getInt("combat-seconds", 15);
    }

    public int getStartingLives() {
        return plugin.getConfig().getInt("starting-lives", 3);
    }
}
