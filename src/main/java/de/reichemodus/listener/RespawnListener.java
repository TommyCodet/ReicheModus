package de.reichemodus.listener;

import de.reichemodus.ReicheModus;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class RespawnListener implements Listener {

    private final ReicheModus plugin;

    public RespawnListener(ReicheModus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        if (!plugin.getLifeManager()
                .isEliminated(player.getUniqueId())) {
            return;
        }

        player.getServer().getScheduler().runTask(
                plugin,
                () -> player.setGameMode(GameMode.SPECTATOR)
        );
    }
}
