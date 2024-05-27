package me.abacate.clans.handles;

import me.abacate.clans.config.ConfigManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatHandles {
    ConfigManager configManager;
    public ChatHandles(ConfigManager config){
        configManager = config;
    }
    public ChatColor checkChatColor(int playerKills){
        if(playerKills==configManager.getNoPoints()){
            return ChatColor.GREEN;
        }else if(playerKills<configManager.getFirstPoints()){
            return ChatColor.YELLOW;
        }else if(playerKills<configManager.getMidPoints()){
            return ChatColor.RED;
        }else if(playerKills<configManager.getFinalPoints()){
            return ChatColor.DARK_RED;
        }else{
            return ChatColor.DARK_PURPLE;
        }
    }
    public ChatColor checkChatColorClan(int points){
        if(points==configManager.getNoClanPoints()){
            return ChatColor.GREEN;
        }else if(points<configManager.getFirstClanPoints()){
            return ChatColor.YELLOW;
        }else if(points<configManager.getMidClanPoints()){
            return ChatColor.RED;
        }else if(points<configManager.getFinalClanPoints()){
            return ChatColor.DARK_RED;
        }else{
            return ChatColor.DARK_PURPLE;
        }
    }
    public void sendMessageToAllClan(ClanMongo clan,String message){
        for (String p:clan.getMembers()){
            Bukkit.getPlayer(UUID.fromString(p)).sendMessage(message);
        }
    }
}
