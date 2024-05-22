package me.abacate.clans.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(FileConfiguration configin) {
//        configFile = new File(configFilePath);
//        config = YamlConfiguration.loadConfiguration(configFile);
        config = configin;
    }
    public FileConfiguration getConfig() {
        return config;
    }
    public int getKillPoints(){
        return config.getInt("killPoints");
    }
    public int getEnemyPoints(){
        return config.getInt("enemyPoints");
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}