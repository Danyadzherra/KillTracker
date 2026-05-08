package ru.mc.killtracker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class KillTopCommand implements CommandExecutor {
    private final KillTracker plugin;

    public KillTopCommand(KillTracker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean showAll = false;
        if (args.length > 0 && args[0].equalsIgnoreCase("all")) {
            if (!sender.hasPermission("killtracker.admin")) {
                sender.sendMessage(ChatColor.RED + "Недостаточно прав.");
                return true;
            }
            showAll = true;
        }

        YamlConfiguration config = plugin.getKillsConfig();
        Set<String> players = config.getConfigurationSection("kills") != null ?
                config.getConfigurationSection("kills").getKeys(false) : new HashSet<>();

        if (players.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Пока никто никого не убил.");
            return true;
        }

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>();
        for (String name : players) {
            int kills = config.getInt("kills." + name);
            if (kills > 0) {
                sorted.add(new AbstractMap.SimpleEntry<>(name, kills));
            }
        }
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        if (showAll) {
            sender.sendMessage(ChatColor.GOLD + "=== Все убийцы (сезон) ===");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : sorted) {
                sender.sendMessage(ChatColor.YELLOW + "" + rank + ". " + ChatColor.WHITE +
                        entry.getKey() + ChatColor.GRAY + " — " + ChatColor.RED + entry.getValue() + " kills");
                rank++;
            }
        } else {
            sender.sendMessage(ChatColor.GOLD + "=== Топ-5 убийц ===");
            int limit = Math.min(5, sorted.size());
            for (int i = 0; i < limit; i++) {
                Map.Entry<String, Integer> entry = sorted.get(i);
                sender.sendMessage(ChatColor.YELLOW + "" + (i+1) + ". " + ChatColor.WHITE +
                        entry.getKey() + ChatColor.GRAY + " — " + ChatColor.RED + entry.getValue() + " kills");
            }
        }
        return true;
    }
}
