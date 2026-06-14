package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.TeamData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public final class ScoreboardManager {

    private final ReicheModus plugin;

    private BukkitTask updateTask;

    public ScoreboardManager(ReicheModus plugin) {

        this.plugin = plugin;

        startTask();
    }

    private void startTask() {

        updateTask = Bukkit.getScheduler().runTaskTimer(
                plugin,
                this::updateAll,
                20L,
                20L
        );
    }

    private void updateAll() {

        if (!plugin.getConfigManager().isScoreboardEnabled()) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            update(player);
        }
    }

    public void update(Player player) {

        Scoreboard scoreboard =
                Bukkit.getScoreboardManager()
                        .getNewScoreboard();

        Objective objective =
                scoreboard.registerNewObjective(
                        "reiche",
                        "dummy",
                        "§6🍌 Reiche Modus"
                );

        objective.setDisplaySlot(
                DisplaySlot.SIDEBAR
        );

        TeamData team =
                plugin.getTeamManager()
                        .getTeamByPlayer(
                                player.getUniqueId()
                        );

        String teamName =
                team == null
                        ? "-"
                        : team.getName();

        int lives =
                plugin.getLifeManager()
                        .getLives(
                                player.getUniqueId()
                        );

        long combat =
                plugin.getCombatManager()
                        .getRemaining(
                                player.getUniqueId()
                        );

        String banana =
                team == null
                        ? "-"
                        : (team.isBananaMissing()
                        ? "✘"
                        : "✔");

        objective.getScore(" ").setScore(8);

        objective.getScore(
                "§eTeam: §f" + teamName
        ).setScore(7);

        objective.getScore(
                "§cLeben: §f" + lives
        ).setScore(6);

        objective.getScore(
                "§6Combat: §f" + combat + "s"
        ).setScore(5);

        objective.getScore(
                "§aBanane: §f" + banana
        ).setScore(4);

        player.setScoreboard(scoreboard);
    }

    public void shutdown() {

        if (updateTask != null) {
            updateTask.cancel();
        }
    }
}
