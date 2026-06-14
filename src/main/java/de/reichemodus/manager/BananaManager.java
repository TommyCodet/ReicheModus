package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import org.bukkit.NamespacedKey;

public final class BananaManager {

    private final ReicheModus plugin;
    private final NamespacedKey bananaKey;

    public BananaManager(ReicheModus plugin) {
        this.plugin = plugin;
        this.bananaKey = new NamespacedKey(plugin, "reiche_banana");
    }

    public NamespacedKey getBananaKey() {
        return bananaKey;
    }
}
