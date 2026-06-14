package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class BananaManager {

    private final ReicheModus plugin;

    private final NamespacedKey bananaKey;
    private final NamespacedKey teamKey;

    public BananaManager(ReicheModus plugin) {
        this.plugin = plugin;

        this.bananaKey =
                new NamespacedKey(plugin, "reiche_banana");

        this.teamKey =
                new NamespacedKey(plugin, "reiche_team");
    }

    public NamespacedKey getBananaKey() {
        return bananaKey;
    }

    public NamespacedKey getTeamKey() {
        return teamKey;
    }

    public ItemStack createBanana(String teamName) {

        ItemStack item =
                new ItemStack(Material.GOLD_BLOCK);

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setDisplayName("§6🍌 Team-Banane");

        meta.getPersistentDataContainer().set(
                bananaKey,
                PersistentDataType.BYTE,
                (byte) 1
        );

        meta.getPersistentDataContainer().set(
                teamKey,
                PersistentDataType.STRING,
                teamName
        );

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);

        return item;
    }

    public boolean isBanana(ItemStack item) {

        if (item == null) {
            return false;
        }

        if (item.getType() != Material.GOLD_BLOCK) {
            return false;
        }

        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta =
                item.getItemMeta();

        return meta.getPersistentDataContainer().has(
                bananaKey,
                PersistentDataType.BYTE
        );
    }

    public String getTeam(ItemStack item) {

        if (!isBanana(item)) {
            return null;
        }

        ItemMeta meta =
                item.getItemMeta();

        return meta.getPersistentDataContainer().get(
                teamKey,
                PersistentDataType.STRING
        );
    }
}
