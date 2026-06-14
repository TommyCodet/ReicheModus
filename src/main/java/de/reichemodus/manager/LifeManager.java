package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LifeManager {

    private final ReicheModus plugin;

    private final Map<UUID, PlayerData> players;

    private final File file;
    private final YamlConfiguration configuration;

    public LifeManager(ReicheModus plugin) {

        this.plugin = plugin;
        this.players = new ConcurrentHashMap<>();

        File folder = plugin.getDataFolder();

        if (!folder.exists()) {
            folder.mkdirs();
        }

        this.file = new File(folder, "players.yml");

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

    public PlayerData getOrCreate(UUID uuid) {

        return players.computeIfAbsent(
                uuid,
                id -> new PlayerData(
                        id,
                        plugin.getConfigManager().getStartingLives()
                )
        );
    }

    public int getLives(UUID uuid) {
        return getOrCreate(uuid).getLives();
    }

    public void setLives(UUID uuid, int lives) {

        PlayerData data = getOrCreate(uuid);

        data.setLives(Math.max(0, lives));

        save();
    }

    public void removeLife(UUID uuid) {

        PlayerData data = getOrCreate(uuid);

        int lives = Math.max(0, data.getLives() - 1);

        data.setLives(lives);

        if (lives <= 0) {
            data.setEliminated(true);
        }

        save();
    }

    public boolean isEliminated(UUID uuid) {
        return getOrCreate(uuid).isEliminated();
    }

    public void setEliminated(UUID uuid, boolean eliminated) {

        PlayerData data = getOrCreate(uuid);

        data.setEliminated(eliminated);

        save();
    }

    public Map<UUID, PlayerData> getPlayers() {
        return players;
    }

    public void load() {

        players.clear();

        if (!configuration.contains("players")) {
            return;
        }

        if (configuration.getConfigurationSection("players") == null) {
            return;
        }

        for (String key :
                configuration.getConfigurationSection("players").getKeys(false)) {

            UUID uuid;

            try {
                uuid = UUID.fromString(key);
            } catch (IllegalArgumentException exception) {
                continue;
            }

            int lives =
                    configuration.getInt(
                            "players." + key + ".lives",
                            plugin.getConfigManager().getStartingLives()
                    );

            boolean eliminated =
                    configuration.getBoolean(
                            "players." + key + ".eliminated",
                            false
                    );

            String team =
                    configuration.getString(
                            "players." + key + ".team"
                    );

            PlayerData data =
                    new PlayerData(uuid, lives);

            data.setEliminated(eliminated);
            data.setTeamName(team);

            players.put(uuid, data);
        }
    }

    public void save() {

        configuration.set("players", null);

        for (PlayerData data : players.values()) {

            String path =
                    "players." + data.getUniqueId();

            configuration.set(
                    path + ".lives",
                    data.getLives()
            );

            configuration.set(
                    path + ".eliminated",
                    data.isEliminated()
            );

            configuration.set(
                    path + ".team",
                    data.getTeamName()
            );
        }

        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
