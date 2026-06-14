package de.reichemodus;

import de.reichemodus.manager.BananaManager;
import de.reichemodus.manager.CombatManager;
import de.reichemodus.manager.ConfigManager;
import de.reichemodus.manager.GameManager;
import de.reichemodus.manager.LifeManager;
import de.reichemodus.manager.ScoreboardManager;
import de.reichemodus.manager.TeamManager;
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
        teamManager = new TeamManager(this);
        lifeManager = new LifeManager(this);
        combatManager = new CombatManager(this);
        bananaManager = new BananaManager(this);
        gameManager = new GameManager(this);
        scoreboardManager = new ScoreboardManager(this);
    }

    @Override
    public void onDisable() {
        teamManager.save();
        lifeManager.save();
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
