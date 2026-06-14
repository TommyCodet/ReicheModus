package de.reichemodus.command;

import de.reichemodus.ReicheModus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class LivesCommand implements CommandExecutor {

    private final ReicheModus plugin;

    public LivesCommand(ReicheModus plugin) {
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

        int lives =
                plugin.getLifeManager()
                        .getLives(player.getUniqueId());

        player.sendMessage(
                "§6❤ Leben: §f" + lives
        );

        return true;
    }
}
