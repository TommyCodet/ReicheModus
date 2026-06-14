package de.reichemodus.command;

import de.reichemodus.ReicheModus;
import de.reichemodus.model.TeamData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TeamCommand implements CommandExecutor, TabCompleter {

    private final ReicheModus plugin;

    public TeamCommand(ReicheModus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            org.bukkit.command.CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können diesen Command nutzen.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "create" -> create(player, args);

            case "delete" -> delete(player);

            case "join" -> join(player, args);

            case "leave" -> leave(player);

            case "kick" -> kick(player, args);

            case "info" -> info(player);

            case "list" -> list(player);

            default -> sendHelp(player);
        }

        return true;
    }

    private void create(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§c/team create <name>");
            return;
        }

        if (plugin.getTeamManager()
                .getTeamByPlayer(player.getUniqueId()) != null) {

            player.sendMessage("§cDu bist bereits in einem Team.");
            return;
        }

        String name = args[1];

        if (plugin.getTeamManager().exists(name)) {

            player.sendMessage("§cDieses Team existiert bereits.");
            return;
        }

        plugin.getTeamManager().createTeam(
                name,
                player.getUniqueId()
        );

        player.sendMessage(
                "§aTeam §e" + name + " §awurde erstellt."
        );
    }

    private void delete(Player player) {

        TeamData team =
                plugin.getTeamManager()
                        .getTeamByPlayer(player.getUniqueId());

        if (team == null) {
            player.sendMessage("§cDu bist in keinem Team.");
            return;
        }

        if (!plugin.getTeamManager()
                .isOwner(player.getUniqueId())
                && !player.isOp()) {

            player.sendMessage(
                    "§cNur der Teamleiter darf das Team löschen."
            );

            return;
        }

        String teamName = team.getName();

        plugin.getTeamManager()
                .deleteTeam(teamName);

        Bukkit.broadcastMessage(
                "§cTeam §e" + teamName + " §cwurde gelöscht."
        );
    }

    private void join(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§c/team join <team>");
            return;
        }

        TeamData current =
                plugin.getTeamManager()
                        .getTeamByPlayer(player.getUniqueId());

        if (current != null) {

            player.sendMessage(
                    "§cVerlasse zuerst dein aktuelles Team."
            );

            return;
        }

        String teamName = args[1];

        TeamData target =
                plugin.getTeamManager()
                        .getTeam(teamName);

        if (target == null) {

            player.sendMessage(
                    "§cDieses Team existiert nicht."
            );

            return;
        }

        plugin.getTeamManager()
                .addMember(
                        player.getUniqueId(),
                        teamName
                );

        player.sendMessage(
                "§aDu bist Team §e"
                        + teamName
                        + " §abeigetreten."
        );
    }

    private void leave(Player player) {

        TeamData team =
                plugin.getTeamManager()
                        .getTeamByPlayer(player.getUniqueId());

        if (team == null) {

            player.sendMessage(
                    "§cDu bist in keinem Team."
            );

            return;
        }

        plugin.getTeamManager()
                .removeMember(
                        player.getUniqueId()
                );

        player.sendMessage(
                "§eDu hast dein Team verlassen."
        );
    }

    private void kick(Player player, String[] args) {

        if (args.length < 2) {

            player.sendMessage(
                    "§c/team kick <spieler>"
            );

            return;
        }

        TeamData team =
                plugin.getTeamManager()
                        .getTeamByPlayer(player.getUniqueId());

        if (team == null) {

            player.sendMessage(
                    "§cDu bist in keinem Team."
            );

            return;
        }

        if (!plugin.getTeamManager()
                .isOwner(player.getUniqueId())
                && !player.isOp()) {

            player.sendMessage(
                    "§cNur der Teamleiter darf Spieler kicken."
            );

            return;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(args[1]);

        UUID targetUuid =
                target.getUniqueId();

        if (!team.hasMember(targetUuid)) {

            player.sendMessage(
                    "§cDieser Spieler ist nicht in deinem Team."
            );

            return;
        }

        if (player.getUniqueId().equals(targetUuid)) {

            player.sendMessage(
                    "§cDu kannst dich nicht selbst kicken."
            );

            return;
        }

        plugin.getTeamManager()
                .removeMember(targetUuid);

        player.sendMessage(
                "§aSpieler entfernt."
        );

        if (target.isOnline()) {

            target.getPlayer().sendMessage(
                    "§cDu wurdest aus Team "
                            + team.getName()
                            + " entfernt."
            );
        }
    }

    private void info(Player player) {

        TeamData team =
                plugin.getTeamManager()
                        .getTeamByPlayer(player.getUniqueId());

        if (team == null) {

            player.sendMessage(
                    "§cDu bist in keinem Team."
            );

            return;
        }

        player.sendMessage("§6=== Team Info ===");
        player.sendMessage("§eName: §f" + team.getName());
        player.sendMessage("§eMitglieder: §f" + team.getMembers().size());
        player.sendMessage(
                "§eBanane fehlt: §f"
                        + (team.isBananaMissing() ? "Ja" : "Nein")
        );
        player.sendMessage(
                "§eAlive: §f"
                        + (team.isAlive() ? "Ja" : "Nein")
        );
    }

    private void list(Player player) {

        player.sendMessage("§6=== Teams ===");

        for (TeamData team :
                plugin.getTeamManager().getTeams()) {

            player.sendMessage(
                    "§e"
                            + team.getName()
                            + " §7("
                            + team.getMembers().size()
                            + " Spieler)"
            );
        }
    }

    private void sendHelp(Player player) {

        player.sendMessage("§6=== Team Commands ===");
        player.sendMessage("§e/team create <name>");
        player.sendMessage("§e/team delete");
        player.sendMessage("§e/team join <team>");
        player.sendMessage("§e/team leave");
        player.sendMessage("§e/team kick <spieler>");
        player.sendMessage("§e/team info");
        player.sendMessage("§e/team list");
    }

    @Override
    public List<String> onTabComplete(
            org.bukkit.command.CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        List<String> suggestions =
                new ArrayList<>();

        if (args.length == 1) {

            suggestions.add("create");
            suggestions.add("delete");
            suggestions.add("join");
            suggestions.add("leave");
            suggestions.add("kick");
            suggestions.add("info");
            suggestions.add("list");

            return suggestions;
        }

        if (args.length == 2
                && args[0].equalsIgnoreCase("join")) {

            suggestions.addAll(
                    plugin.getTeamManager()
                            .getTeamNames()
            );
        }

        return suggestions;
    }
}
