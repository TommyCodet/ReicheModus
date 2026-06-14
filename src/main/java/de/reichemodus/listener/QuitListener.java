package de.reichemodus.listener;

import de.reichemodus.ReicheModus;
import de.reichemodus.util.MessageUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class QuitListener implements Listener {

    private final ReicheModus plugin;

    public QuitListener(ReicheModus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (!plugin.getGameManager().isRunning()) {
            return;
        }

        if (!plugin.getCombatManager()
                .isInCombat(player.getUniqueId())) {
            return;
        }

        plugin.getLifeManager()
                .removeLife(player.getUniqueId());

        int lives =
                plugin.getLifeManager()
                        .getLives(player.getUniqueId());

        String message =
                plugin.getConfigManager()
                        .getCombatLogMessage()
                        .replace(
                                "%player%",
                                player.getName()
                        );

        MessageUtil.broadcast(message);

        if (lives > 0) {
            return;
        }

        plugin.getLifeManager()
                .setEliminated(
                        player.getUniqueId(),
                        true
                );

        player.setGameMode(
                GameMode.SPECTATOR
        );

        var team =
                plugin.getTeamManager()
                        .getTeamByPlayer(
                                player.getUniqueId()
                        );

        if (team != null
                && !plugin.getGameManager()
                .hasAlivePlayers(team)) {

            plugin.getGameManager()
                    .eliminateTeam(team);
        }

        plugin.getGameManager()
                .checkWinCondition();
    }
}
