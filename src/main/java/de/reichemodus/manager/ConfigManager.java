package de.reichemodus.manager;

import de.reichemodus.ReicheModus;

public final class ConfigManager {

    private final ReicheModus plugin;

    public ConfigManager(ReicheModus plugin) {
        this.plugin = plugin;
    }

    public int getStartingLives() {
        return plugin.getConfig().getInt(
                "starting-lives",
                3
        );
    }

    public int getCombatSeconds() {
        return plugin.getConfig().getInt(
                "combat-seconds",
                15
        );
    }

    public boolean isBananaDecayEnabled() {
        return plugin.getConfig().getBoolean(
                "banana-decay-enabled",
                true
        );
    }

    public int getBananaDecayRadius() {
        return plugin.getConfig().getInt(
                "banana-decay-radius",
                30
        );
    }

    public long getBananaDecayIntervalTicks() {
        return plugin.getConfig().getLong(
                "banana-decay-interval-ticks",
                40L
        );
    }

    public boolean isScoreboardEnabled() {
        return plugin.getConfig().getBoolean(
                "scoreboard-enabled",
                true
        );
    }

    public String getGameStartMessage() {
        return plugin.getConfig().getString(
                "broadcasts.game-start",
                "&aReiche Modus wurde gestartet."
        );
    }

    public String getGameStopMessage() {
        return plugin.getConfig().getString(
                "broadcasts.game-stop",
                "&cReiche Modus wurde gestoppt."
        );
    }

    public String getBananaStolenMessage() {
        return plugin.getConfig().getString(
                "broadcasts.banana-stolen",
                "&6🍌 Die Banane von Team %team% wurde gestohlen!"
        );
    }

    public String getWinnerMessage() {
        return plugin.getConfig().getString(
                "broadcasts.winner",
                "&6🏆 Team %team% gewinnt Reiche Modus!"
        );
    }

    public String getCombatLogMessage() {
        return plugin.getConfig().getString(
                "broadcasts.combat-log",
                "&c%player% hat während Combat ausgeloggt."
        );
    }

    public void reload() {
        plugin.reloadConfig();
    }
}
