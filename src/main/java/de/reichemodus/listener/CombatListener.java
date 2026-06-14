package de.reichemodus.listener;

import de.reichemodus.ReicheModus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class CombatListener implements Listener {

    private final ReicheModus plugin;

    private final Set<UUID> activeDisplays;

    public CombatListener(ReicheModus plugin) {

        this.plugin = plugin;
        this.activeDisplays = new HashSet<>();

        startActionBarTask();
    }

    private void startActionBarTask() {

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {

                    Set<UUID> remove = new HashSet<>();

                    for (UUID uuid : activeDisplays) {

                        Player player =
                                Bukkit.getPlayer(uuid);

                        if (player == null || !player.isOnline()) {

                            remove.add(uuid);
                            continue;
                        }

                        long remaining =
                                plugin.getCombatManager()
                                        .getRemaining(uuid);

                        if (remaining <= 0) {

                            player.sendActionBar(
                                    Component.text("")
                            );

                            remove.add(uuid);
                            continue;
                        }

                        player.sendActionBar(
                                Component.text(
                                        "⚔ Combat: "
                                                + remaining
                                                + " Sekunden"
                                )
                        );
                    }

                    activeDisplays.removeAll(remove);

                },
                20L,
                20L
        );

        if (task.isCancelled()) {
            Bukkit.getLogger().warning(
                    "Combat ActionBar Task konnte nicht gestartet werden."
            );
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (plugin.getLifeManager()
                .isEliminated(attacker.getUniqueId())) {
            return;
        }

        if (plugin.getLifeManager()
                .isEliminated(victim.getUniqueId())) {
            return;
        }

        plugin.getCombatManager()
                .tag(attacker.getUniqueId());

        plugin.getCombatManager()
                .tag(victim.getUniqueId());

        activeDisplays.add(
                attacker.getUniqueId()
        );

        activeDisplays.add(
                victim.getUniqueId()
        );
    }
}
