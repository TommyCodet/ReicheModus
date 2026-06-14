package de.reichemodus;

import de.reichemodus.command.GiveBananaCommand;
import de.reichemodus.command.LivesCommand;
import de.reichemodus.command.ReicheCommand;
import de.reichemodus.command.TeamCommand;
import de.reichemodus.listener.BananaListener;
import de.reichemodus.listener.CombatListener;
import de.reichemodus.listener.DeathListener;
import de.reichemodus.listener.JoinListener;
import de.reichemodus.listener.QuitListener;
import de.reichemodus.listener.RespawnListener;
import de.reichemodus.manager.BananaManager;
import de.reichemodus.manager.CombatManager;
import de.reichemodus.manager.ConfigManager;
import de.reichemodus.manager.GameManager;
import de.reichemodus.manager.LifeManager;
import de.reichemodus.manager.ScoreboardManager;
import de.reichemodus.manager.TeamManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ReicheModus extends JavaPlugin {

    private static ReicheModus instance;

    private ConfigManager configManager;
    private TeamManager teamManager;
    private LifeManager lifeManager;
    private CombatManager combatManager;
    private BananaManager bananaManager;
    private GameManager gameManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        configManager = new ConfigManager(this);

        lifeManager = new LifeManager(this);
        teamManager = new TeamManager(this);

        combatManager = new CombatManager(this);
        bananaManager = new BananaManager(this);
        gameManager = new GameManager(this);
        scoreboardManager = new ScoreboardManager(this);

        registerCommands();
        registerListeners();

        getLogger().info("=================================");
        getLogger().info("ReicheModus v1.0 aktiviert");
        getLogger().info("Minecraft: Paper 1.21.11");
        getLogger().info("Java: 21");
        getLogger().info("=================================");
    }

    @Override
    public void onDisable() {

        try {

            if (teamManager != null) {
                teamManager.save();
            }

            if (lifeManager != null) {
                lifeManager.save();
            }

            if (scoreboardManager != null) {
                scoreboardManager.shutdown();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        getLogger().info("ReicheModus deaktiviert.");
    }

    private void registerCommands() {

        TeamCommand teamCommand =
                new TeamCommand(this);

        PluginCommand team =
                getCommand("team");

        if (team != null) {

            team.setExecutor(teamCommand);
            team.setTabCompleter(teamCommand);
        }

        PluginCommand lives =
                getCommand("lives");

        if (lives != null) {

            lives.setExecutor(
                    new LivesCommand(this)
            );
        }

        PluginCommand giveBanane =
                getCommand("givebanane");

        if (giveBanane != null) {

            giveBanane.setExecutor(
                    new GiveBananaCommand(this)
            );
        }

        PluginCommand reiche =
                getCommand("reiche");

        if (reiche != null) {

            reiche.setExecutor(
                    new ReicheCommand(this)
            );
        }
    }

    private void registerListeners() {

        getServer().getPluginManager().registerEvents(
                new CombatListener(this),
                this
        );

        getServer().getPluginManager().registerEvents(
                new DeathListener(this),
                this
        );

        getServer().getPluginManager().registerEvents(
                new RespawnListener(this),
                this
        );

        getServer().getPluginManager().registerEvents(
                new QuitListener(this),
                this
        );

        getServer().getPluginManager().registerEvents(
                new JoinListener(this),
                this
        );

        getServer().getPluginManager().registerEvents(
                new BananaListener(this),
                this
        );
    }

    public static ReicheModus getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public LifeManager getLifeManager() {
        return lifeManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public BananaManager getBananaManager() {
        return bananaManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
