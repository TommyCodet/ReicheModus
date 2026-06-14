package de.reichemodus.command;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.TeamData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class GiveBananaCommand implements CommandExecutor {

    private final ReicheModus plugin;

    public GiveBananaCommand(ReicheModus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    "§cNur Spieler können diesen Command nutzen."
            );

            return true;
        }

        if (!player.isOp()) {

            player.sendMessage(
                    "§cKeine Berechtigung."
            );

            return true;
        }

        if (args.length < 1) {

            player.sendMessage(
                    "§c/givebanane <team>"
            );

            return true;
        }

        String teamName = args[0];

        TeamData team =
                plugin.getTeamManager()
                        .getTeam(teamName);

        if (team == null) {

            player.sendMessage(
                    "§cDieses Team existiert nicht."
            );

            return true;
        }

        ItemStack banana =
                plugin.getBananaManager()
                        .createBanana(team.getName());

        player.getInventory()
                .addItem(banana);

        player.sendMessage(
                "§aTeam-Banane für §e"
                        + team.getName()
                        + " §avergeben."
        );

        return true;
    }
}
