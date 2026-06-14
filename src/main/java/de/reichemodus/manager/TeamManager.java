package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.TeamData;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TeamManager {

    private final ReicheModus plugin;
    private final Map<String, TeamData> teams = new ConcurrentHashMap<>();

    public TeamManager(ReicheModus plugin) {
        this.plugin = plugin;
    }

    public TeamData createTeam(String name) {
        TeamData team = new TeamData(name);
        teams.put(name.toLowerCase(), team);
        return team;
    }

    public TeamData getTeam(String name) {
        return teams.get(name.toLowerCase());
    }

    public void deleteTeam(String name) {
        teams.remove(name.toLowerCase());
    }

    public Collection<TeamData> getTeams() {
        return teams.values();
    }

    public void save() {
    }
}
