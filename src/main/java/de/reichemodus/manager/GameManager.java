package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.PlayerData;
import de.reichemodus.model.TeamData;
import de.reichemodus.util.MessageUtil;
import org.bukkit.Bukkit;

public final class GameManager {

    private final ReicheModus plugin;

    private boolean running;

    public GameManager(ReicheModus plugin) {
        this.plugin = plugin;
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {

        if (running) {
            return;
        }

        running = true;

        MessageUtil.broadcast(
                "&aReiche Modus wurde gestartet."
        );

        checkWinCondition();
    }

    public void stop() {

        if (!running) {
            return;
        }

        running = false;

        MessageUtil.broadcast(
                "&cReiche Modus wurde gestoppt."
        );
    }

    public void reload() {

        plugin.reloadConfig();

        MessageUtil.broadcast(
                "&eReiche Modus Konfiguration wurde neu geladen."
        );
    }

    public String getStatus() {

        return running
                ? "AKTIV"
                : "GESTOPPT";
    }

    public int getAliveTeams() {

        int count = 0;

        for (TeamData team : plugin.getTeamManager().getTeams()) {

            if (!team.isAlive()) {
                continue;
            }

            if (team.isEliminated()) {
                continue;
            }

            if (!hasAlivePlayers(team)) {
                continue;
            }

            count++;
        }

        return count;
    }

    public TeamData getLastAliveTeam() {

        for (TeamData team : plugin.getTeamManager().getTeams()) {

            if (!team.isAlive()) {
                continue;
            }

            if (team.isEliminated()) {
                continue;
            }

            if (!hasAlivePlayers(team)) {
                continue;
            }

            return team;
        }

        return null;
    }

    public boolean hasAlivePlayers(TeamData team) {

        for (var uuid : team.getMembers()) {

            PlayerData player =
                    plugin.getLifeManager()
                            .getOrCreate(uuid);

            if (!player.isEliminated()
                    && player.getLives() > 0) {
                return true;
            }
        }

        return false;
    }

    public void checkWinCondition() {

        if (!running) {
            return;
        }

        int aliveTeams = getAliveTeams();

        if (aliveTeams > 1) {
            return;
        }

        TeamData winner = getLastAliveTeam();

        if (winner == null) {
            return;
        }

        Bukkit.broadcastMessage(
                "§6🏆 Team "
                        + winner.getName()
                        + " gewinnt Reiche Modus!"
        );

        stop();
    }

    public void eliminateTeam(TeamData team) {

        if (team == null) {
            return;
        }

        team.setAlive(false);
        team.setEliminated(true);

        plugin.getTeamManager().save();

        MessageUtil.broadcast(
                "&cTeam "
                        + team.getName()
                        + " wurde eliminiert."
        );

        checkWinCondition();
    }
}
