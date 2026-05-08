package ru.mc.killtracker;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

public class KillTracker extends JavaPlugin {
    private static KillTracker instance;
    private YamlConfiguration killsConfig;
    private File killsFile;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultKillsFile();
        getCommand("kills").setExecutor(new KillsCommand(this));
        getCommand("killtop").setExecutor(new KillTopCommand(this));
        getCommand("season").setExecutor(new SeasonEndCommand(this));
        getServer().getPluginManager().registerEvents(new KillListener(this), this);
        getLogger().info("KillTracker enabled!");
    }

    @Override
    public void onDisable() {
        saveKills();
    }

    public static KillTracker getInstance() {
        return instance;
    }

    private void saveDefaultKillsFile() {
        killsFile = new File(getDataFolder(), "kills.yml");
        if (!killsFile.exists()) {
            saveResource("kills.yml", false);
        }
        killsConfig = YamlConfiguration.loadConfiguration(killsFile);
    }

    public YamlConfiguration getKillsConfig() {
        return killsConfig;
    }

    public void saveKills() {
        try {
            killsConfig.save(killsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addKill(String playerName) {
        String path = "kills." + playerName;
        killsConfig.set(path, killsConfig.getInt(path, 0) + 1);
        saveKills();
    }

    public int getKills(String playerName) {
        return killsConfig.getInt("kills." + playerName, 0);
    }

    public void resetAllKills() {
        killsConfig.set("kills", null);
        saveKills();
    }
}
