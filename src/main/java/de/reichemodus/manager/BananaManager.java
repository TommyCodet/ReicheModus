package de.reichemodus.manager;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.TeamData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
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

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE
        );

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

    public boolean belongsToTeam(ItemStack item, String teamName) {

        String bananaTeam =
                getTeam(item);

        if (bananaTeam == null) {
            return false;
        }

        return bananaTeam.equalsIgnoreCase(teamName);
    }

    public void placeBanana(String teamName, Location location) {

        TeamData team =
                plugin.getTeamManager().getTeam(teamName);

        if (team == null) {
            return;
        }

        team.setBananaLocation(location);
        team.setBananaMissing(false);

        plugin.getTeamManager().save();
    }

    public void removeBanana(String teamName) {

        TeamData team =
                plugin.getTeamManager().getTeam(teamName);

        if (team == null) {
            return;
        }

        team.setBananaMissing(true);

        plugin.getTeamManager().save();
    }

    public boolean hasBanana(String teamName) {

        TeamData team =
                plugin.getTeamManager().getTeam(teamName);

        if (team == null) {
            return false;
        }

        return !team.isBananaMissing()
                && team.getBananaLocation() != null;
    }

    public Location getBananaLocation(String teamName) {

        TeamData team =
                plugin.getTeamManager().getTeam(teamName);

        if (team == null) {
            return null;
        }

        return team.getBananaLocation();
    }

    public TeamData getTeamByBananaLocation(Location location) {

        if (location == null) {
            return null;
        }

        for (TeamData team :
                plugin.getTeamManager().getTeams()) {

            Location bananaLocation =
                    team.getBananaLocation();

            if (bananaLocation == null) {
                continue;
            }

            if (!bananaLocation.getWorld().equals(location.getWorld())) {
                continue;
            }

            if (bananaLocation.getBlockX() != location.getBlockX()) {
                continue;
            }

            if (bananaLocation.getBlockY() != location.getBlockY()) {
                continue;
            }

            if (bananaLocation.getBlockZ() != location.getBlockZ()) {
                continue;
            }

            return team;
        }

        return null;
    }

    public TeamData getTeamByBananaBlock(Block block) {

        if (block == null) {
            return null;
        }

        return getTeamByBananaLocation(
                block.getLocation()
        );
    }

    public boolean isBananaBlock(Block block) {

        if (block == null) {
            return false;
        }

        if (block.getType() != Material.GOLD_BLOCK) {
            return false;
        }

        return getTeamByBananaBlock(block) != null;
    }

    public void clearBanana(String teamName) {

        TeamData team =
                plugin.getTeamManager().getTeam(teamName);

        if (team == null) {
            return;
        }

        team.setBananaMissing(true);
        team.setBananaLocation(null);

        plugin.getTeamManager().save();
    }
}
