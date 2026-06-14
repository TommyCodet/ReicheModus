package de.reichemodus.listener;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.TeamData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class DeathListener implements Listener {

    private final ReicheModus plugin;

    public DeathListener(ReicheModus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getPlayer();

        if (!plugin.getGameManager().isRunning()) {
            return;
        }

        plugin.getLifeManager()
                .removeLife(player.getUniqueId());

        int remainingLives =
                plugin.getLifeManager()
                        .getLives(player.getUniqueId());

        player.sendMessage(
                "§cDu hast ein Leben verloren. Verbleibende Leben: §e"
                        + remainingLives
        );

        if (remainingLives > 0) {

            TeamData team =
                    plugin.getTeamManager()
                            .getTeamByPlayer(player.getUniqueId());

            if (team != null) {
                plugin.getGameManager()
                        .checkWinCondition();
            }

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

        player.sendMessage(
                "§4Du hast alle Leben verloren und bist ausgeschieden."
        );

        TeamData team =
                plugin.getTeamManager()
                        .getTeamByPlayer(player.getUniqueId());

        if (team != null
                && !plugin.getGameManager().hasAlivePlayers(team)) {

            plugin.getGameManager()
                    .eliminateTeam(team);
        }

        plugin.getGameManager()
                .checkWinCondition();
    }
}
