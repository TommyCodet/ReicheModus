package de.reichemodus.command;

import de.reichemodus.ReicheModus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class ReicheCommand implements CommandExecutor {

    private final ReicheModus plugin;

    public ReicheCommand(ReicheModus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.isOp()) {

            sender.sendMessage(
                    "§cKeine Berechtigung."
            );

            return true;
        }

        if (args.length == 0) {

            sender.sendMessage("§6/reiche start");
            sender.sendMessage("§6/reiche stop");
            sender.sendMessage("§6/reiche reload");
            sender.sendMessage("§6/reiche status");

            return true;
        }

        switch (args[0].toLowerCase()) {

            case "start" -> {

                plugin.getGameManager().start();

                sender.sendMessage(
                        "§aSpiel gestartet."
                );
            }

            case "stop" -> {

                plugin.getGameManager().stop();

                sender.sendMessage(
                        "§cSpiel gestoppt."
                );
            }

            case "reload" -> {

                plugin.getGameManager().reload();

                sender.sendMessage(
                        "§eKonfiguration neu geladen."
                );
            }

            case "status" -> {

                sender.sendMessage(
                        "§6Status: §f"
                                + plugin.getGameManager().getStatus()
                );
            }

            default -> sender.sendMessage(
                    "§cUnbekannter Unterbefehl."
            );
        }

        return true;
    }
}
