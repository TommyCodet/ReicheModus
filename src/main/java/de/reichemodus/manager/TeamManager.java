package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.PlayerData;
import de.reichemodus.model.TeamData;
import org.bukkit.configuration.ConfigurationSection;
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

        File folder = plugin.getDataFolder();

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

        this.configuration =
                YamlConfiguration.loadConfiguration(file);

        load();
    }

    public boolean exists(String teamName) {
        return teams.containsKey(
                teamName.toLowerCase()
        );
    }

    public TeamData createTeam(String teamName, UUID owner) {

        TeamData team =
                new TeamData(teamName);

        team.setOwner(owner);
        team.addMember(owner);

        teams.put(
                teamName.toLowerCase(),
                team
        );

        PlayerData playerData =
                plugin.getLifeManager()
                        .getOrCreate(owner);

        playerData.setTeamName(teamName);

        save();

        return team;
    }

    public TeamData getTeam(String teamName) {

        if (teamName == null) {
            return null;
        }

        return teams.get(
                teamName.toLowerCase()
        );
    }

    public Collection<TeamData> getTeams() {
        return teams.values();
    }

    public TeamData getTeamByPlayer(UUID uuid) {

        PlayerData playerData =
                plugin.getLifeManager()
                        .getOrCreate(uuid);

        String teamName =
                playerData.getTeamName();

        if (teamName == null) {
            return null;
        }

        return getTeam(teamName);
    }

    public String getTeamName(UUID uuid) {

        TeamData team =
                getTeamByPlayer(uuid);

        if (team == null) {
            return null;
        }

        return team.getName();
    }

    public boolean isOwner(UUID uuid) {

        TeamData team =
                getTeamByPlayer(uuid);

        if (team == null) {
            return false;
        }

        return uuid.equals(
                team.getOwner()
        );
    }

    public boolean addMember(UUID uuid, String teamName) {

        TeamData team =
                getTeam(teamName);

        if (team == null) {
            return false;
        }

        removeMember(uuid);

        team.addMember(uuid);

        PlayerData playerData =
                plugin.getLifeManager()
                        .getOrCreate(uuid);

        playerData.setTeamName(
                team.getName()
        );

        save();

        return true;
    }

    public void removeMember(UUID uuid) {

        TeamData team =
                getTeamByPlayer(uuid);

        if (team == null) {
            return;
        }

        team.removeMember(uuid);

        PlayerData playerData =
                plugin.getLifeManager()
                        .getOrCreate(uuid);

        playerData.setTeamName(null);

        if (uuid.equals(team.getOwner())) {

            if (team.getMembers().isEmpty()) {

                deleteTeam(
                        team.getName()
                );

                return;
            }

            UUID newOwner =
                    team.getMembers()
                            .iterator()
                            .next();

            team.setOwner(newOwner);
        }

        save();
    }

    public void deleteTeam(String teamName) {

        TeamData team =
                getTeam(teamName);

        if (team == null) {
            return;
        }

        for (UUID uuid : team.getMembers()) {

            PlayerData playerData =
                    plugin.getLifeManager()
                            .getOrCreate(uuid);

            playerData.setTeamName(null);
        }

        teams.remove(
                teamName.toLowerCase()
        );

        save();
    }

    public void load() {

        teams.clear();

        ConfigurationSection section =
                configuration.getConfigurationSection("teams");

        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {

            TeamData team =
                    new TeamData(key);

            String ownerString =
                    configuration.getString(
                            "teams." + key + ".owner"
                    );

            if (ownerString != null) {

                try {

                    team.setOwner(
                            UUID.fromString(ownerString)
                    );

                } catch (Exception ignored) {
                }
            }

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

                try {

                    team.addMember(
                            UUID.fromString(member)
                    );

                } catch (Exception ignored) {
                }
            }

            teams.put(
                    key.toLowerCase(),
                    team
            );
        }
    }

    public void save() {

        configuration.set("teams", null);

        for (TeamData team : teams.values()) {

            String path =
                    "teams." + team.getName();

            configuration.set(
                    path + ".owner",
                    team.getOwner() == null
                            ? null
                            : team.getOwner().toString()
            );

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
