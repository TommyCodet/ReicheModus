package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.PlayerData;
import de.reichemodus.model.TeamData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TeamManager {

    private final ReicheModus plugin;

    private final Map<String, TeamData> teams;

    private final File file;
    private final YamlConfiguration configuration;

    public TeamManager(ReicheModus plugin) {
        this.plugin = plugin;
        this.teams = new ConcurrentHashMap<>();

        File folder = new File(plugin.getDataFolder().getAbsolutePath());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        this.file = new File(folder, "teams.yml");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);

        load();
    }

    public TeamData createTeam(String name) {
        TeamData team = new TeamData(name);

        teams.put(name.toLowerCase(), team);

        save();

        return team;
    }

    public boolean exists(String name) {
        return teams.containsKey(name.toLowerCase());
    }

    public TeamData getTeam(String name) {
        return teams.get(name.toLowerCase());
    }

    public Collection<TeamData> getTeams() {
        return teams.values();
    }

    public void deleteTeam(String name) {
        TeamData team = getTeam(name);

        if (team == null) {
            return;
        }

        for (UUID uuid : team.getMembers()) {
            PlayerData playerData =
                    plugin.getLifeManager().getOrCreate(uuid);

            playerData.setTeamName(null);
        }

        teams.remove(name.toLowerCase());

        save();
    }

    public boolean addMember(UUID uuid, String teamName) {

        TeamData team = getTeam(teamName);

        if (team == null) {
            return false;
        }

        removeMember(uuid);

        team.addMember(uuid);

        PlayerData playerData =
                plugin.getLifeManager().getOrCreate(uuid);

        playerData.setTeamName(team.getName());

        save();

        return true;
    }

    public void removeMember(UUID uuid) {

        TeamData team = getTeamByPlayer(uuid);

        if (team != null) {
            team.removeMember(uuid);
        }

        PlayerData playerData =
                plugin.getLifeManager().getOrCreate(uuid);

        playerData.setTeamName(null);

        save();
    }

    public TeamData getTeamByPlayer(UUID uuid) {

        String teamName =
                plugin.getLifeManager()
                        .getOrCreate(uuid)
                        .getTeamName();

        if (teamName == null) {
            return null;
        }

        return getTeam(teamName);
    }

    public void load() {

        teams.clear();

        if (!configuration.contains("teams")) {
            return;
        }

        for (String key :
                configuration.getConfigurationSection("teams").getKeys(false)) {

            TeamData team = new TeamData(key);

            team.setAlive(
                    configuration.getBoolean(
                            "teams." + key + ".alive",
                            true
                    )
            );

            team.setEliminated(
                    configuration.getBoolean(
                            "teams." + key + ".eliminated",
                            false
                    )
            );

            team.setBananaMissing(
                    configuration.getBoolean(
                            "teams." + key + ".bananaMissing",
                            false
                    )
            );

            team.setBananaWorld(
                    configuration.getString(
                            "teams." + key + ".banana.world"
                    )
            );

            team.setBananaX(
                    configuration.getDouble(
                            "teams." + key + ".banana.x"
                    )
            );

            team.setBananaY(
                    configuration.getDouble(
                            "teams." + key + ".banana.y"
                    )
            );

            team.setBananaZ(
                    configuration.getDouble(
                            "teams." + key + ".banana.z"
                    )
            );

            List<String> members =
                    configuration.getStringList(
                            "teams." + key + ".members"
                    );

            for (String member : members) {
                team.addMember(UUID.fromString(member));
            }

            teams.put(key.toLowerCase(), team);
        }
    }

    public void save() {

        configuration.set("teams", null);

        for (TeamData team : teams.values()) {

            String path =
                    "teams." + team.getName();

            configuration.set(
                    path + ".alive",
                    team.isAlive()
            );

            configuration.set(
                    path + ".eliminated",
                    team.isEliminated()
            );

            configuration.set(
                    path + ".bananaMissing",
                    team.isBananaMissing()
            );

            configuration.set(
                    path + ".banana.world",
                    team.getBananaWorld()
            );

            configuration.set(
                    path + ".banana.x",
                    team.getBananaX()
            );

            configuration.set(
                    path + ".banana.y",
                    team.getBananaY()
            );

            configuration.set(
                    path + ".banana.z",
                    team.getBananaZ()
            );

            configuration.set(
                    path + ".members",
                    team.getMembers()
                            .stream()
                            .map(UUID::toString)
                            .toList()
            );
        }

        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
