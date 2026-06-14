package de.reichemodus.listener;

import de.reichemodus.ReicheModus;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class JoinListener implements Listener {

    private final ReicheModus plugin;

    public JoinListener(ReicheModus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        var data =
                plugin.getLifeManager()
                        .getOrCreate(
                                player.getUniqueId()
                        );

        if (data.isEliminated()
                || data.getLives() <= 0) {

            player.setGameMode(
                    GameMode.SPECTATOR
            );

            player.sendMessage(
                    "§cDu bist aus Reiche Modus ausgeschieden."
            );

            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR) {

            player.setGameMode(
                    GameMode.SURVIVAL
            );
        }
    }
}
