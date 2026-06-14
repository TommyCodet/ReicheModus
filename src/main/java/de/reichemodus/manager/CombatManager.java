package de.reichemodus.manager;

import de.reichemodus.ReicheModus;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CombatManager {

    private final ReicheModus plugin;
    private final Map<UUID, Long> combatUntil = new ConcurrentHashMap<>();

    public CombatManager(ReicheModus plugin) {
        this.plugin = plugin;
    }

    public void tag(UUID uuid) {
        combatUntil.put(
                uuid,
                System.currentTimeMillis()
                        + (plugin.getConfigManager().getCombatSeconds() * 1000L)
        );
    }

    public boolean isInCombat(UUID uuid) {
        return getRemaining(uuid) > 0;
    }

    public long getRemaining(UUID uuid) {
        return Math.max(
                0,
                (combatUntil.getOrDefault(uuid, 0L)
                        - System.currentTimeMillis()) / 1000L
        );
    }
}
