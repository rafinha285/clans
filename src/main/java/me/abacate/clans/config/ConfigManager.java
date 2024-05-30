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

    public int getNoPoints(){
        return config.getInt("points.nopoints");
    }
    public int getFirstPoints(){
        return config.getInt("points.first");
    }
    public int getMidPoints(){
        return config.getInt("points.mid");
    }
    public int getFinalPoints(){
        return config.getInt("points.final");
    }

    public int getNoClanPoints(){
        return config.getInt("clanPoints.nopoints");
    }
    public int getFirstClanPoints(){
        return config.getInt("clanPoints.first");
    }
    public int getMidClanPoints(){
        return config.getInt("clanPoints.mid");
    }
    public int getFinalClanPoints(){
        return config.getInt("clanPoints.final");
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