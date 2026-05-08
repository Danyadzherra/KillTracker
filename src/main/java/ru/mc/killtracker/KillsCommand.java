package ru.mc.killtracker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillsCommand implements CommandExecutor {
    private final KillTracker plugin;

    public KillsCommand(KillTracker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage("Эту команду можно использовать только игроку, либо укажите ник: /kills <игрок>");
            return true;
        }

        String targetName;
        if (args.length > 0) {
            targetName = args[0];
        } else {
            targetName = sender.getName();
        }

        int kills = plugin.getKills(targetName);
        sender.sendMessage(ChatColor.GREEN + "Игрок " + ChatColor.YELLOW + targetName +
                ChatColor.GREEN + " убил других игроков: " + ChatColor.RED + kills);
        return true;
    }
}
