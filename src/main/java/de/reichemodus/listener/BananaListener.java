package de.reichemodus.listener;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.TeamData;
import de.reichemodus.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public final class BananaListener implements Listener {

    private final ReicheModus plugin;

    public BananaListener(ReicheModus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();

        ItemStack item =
                event.getItemInHand();

        if (!plugin.getBananaManager()
                .isBanana(item)) {
            return;
        }

        TeamData playerTeam =
                plugin.getTeamManager()
                        .getTeamByPlayer(
                                player.getUniqueId()
                        );

        if (playerTeam == null) {

            player.sendMessage(
                    "§cDu bist in keinem Team."
            );

            event.setCancelled(true);
            return;
        }

        String bananaTeam =
                plugin.getBananaManager()
                        .getTeam(item);

        if (bananaTeam == null) {

            event.setCancelled(true);
            return;
        }

        if (!bananaTeam.equalsIgnoreCase(
                playerTeam.getName()
        )) {

            player.sendMessage(
                    "§cDu kannst nur die Banane deines Teams platzieren."
            );

            event.setCancelled(true);
            return;
        }

        Block block =
                event.getBlockPlaced();

        if (block.getType() != Material.GOLD_BLOCK) {
            return;
        }

        Location location =
                block.getLocation();

        plugin.getBananaManager()
                .placeBanana(
                        playerTeam.getName(),
                        location
                );

        Bukkit.broadcastMessage(
                "§6🍌 Team "
                        + playerTeam.getName()
                        + " hat seine Banane platziert."
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {

        Block block =
                event.getBlock();

        if (!plugin.getBananaManager()
                .isBananaBlock(block)) {
            return;
        }

        TeamData team =
                plugin.getBananaManager()
                        .getTeamByBananaBlock(block);

        if (team == null) {
            return;
        }

        plugin.getBananaManager()
                .clearBanana(
                        team.getName()
                );

        String message =
                plugin.getConfigManager()
                        .getBananaStolenMessage()
                        .replace(
                                "%team%",
                                team.getName()
                        );

        MessageUtil.broadcast(message);
    }
}
