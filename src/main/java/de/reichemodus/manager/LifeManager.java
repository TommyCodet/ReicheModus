package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.PlayerData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LifeManager {

    private final ReicheModus plugin;
    private final Map<UUID, PlayerData> players = new ConcurrentHashMap<>();

    public LifeManager(ReicheModus plugin) {
        this.plugin = plugin;
    }

    public PlayerData getOrCreate(UUID uuid) {
        return players.computeIfAbsent(
                uuid,
                id -> new PlayerData(
                        id,
                        plugin.getConfigManager().getStartingLives()
                )
        );
    }

    public void save() {
    }
}
