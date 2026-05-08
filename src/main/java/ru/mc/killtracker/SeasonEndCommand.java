package ru.mc.killtracker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class SeasonEndCommand implements CommandExecutor {
    private final KillTracker plugin;

    public SeasonEndCommand(KillTracker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("killtracker.admin")) {
            sender.sendMessage(ChatColor.RED + "Недостаточно прав.");
            return true;
        }

        YamlConfiguration config = plugin.getKillsConfig();
        Set<String> players = config.getConfigurationSection("kills") != null ?
                config.getConfigurationSection("kills").getKeys(false) : new HashSet<>();

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>();
        for (String name : players) {
            int kills = config.getInt("kills." + name);
            if (kills > 0) {
                sorted.add(new AbstractMap.SimpleEntry<>(name, kills));
            }
        }
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        Bukkit.broadcastMessage(ChatColor.GOLD + "🏆 " + ChatColor.BOLD + "Сезон окончен! Топ-5 убийц:");
        int limit = Math.min(5, sorted.size());
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Integer> entry = sorted.get(i);
            Bukkit.broadcastMessage(ChatColor.YELLOW + "" + (i+1) + ". " + ChatColor.WHITE +
                    entry.getKey() + ChatColor.GRAY + " — " + ChatColor.RED + entry.getValue() + " kills");
        }
        if (sorted.isEmpty()) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "За сезон не было убийств между игроками.");
        }

        plugin.resetAllKills();
        sender.sendMessage(ChatColor.GREEN + "Статистика убийств обнулена. Новый сезон начался!");
        return true;
    }
}
