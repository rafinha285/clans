package me.abacate.clans.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanChatManager {
    private Map<String, Set<UUID>> clanChatEnabledPlayers;
    public ClanChatManager(){
        this.clanChatEnabledPlayers =  new HashMap<>();
    }
    public void toggleClanChat(UUID playerId, String clanName) {
        clanChatEnabledPlayers.putIfAbsent(clanName, new HashSet<>());
        Set<UUID> enabledPlayers = clanChatEnabledPlayers.get(clanName);

        if (enabledPlayers.contains(playerId)) {
            enabledPlayers.remove(playerId);
        } else {
            enabledPlayers.add(playerId);
        }
        Bukkit.getLogger().info(clanChatEnabledPlayers.toString());
    }
    public boolean isClanChatEnabled(UUID playerId, String clanName) {
        Bukkit.getLogger().info(clanChatEnabledPlayers.containsKey(clanName) ? clanName : "checkClanChatError");
        Bukkit.getLogger().info(clanChatEnabledPlayers.toString());
        // Verifica se o nome do clã existe na lista de clãs com bate-papo ativado
        if (clanChatEnabledPlayers.containsKey(clanName)) {
            // Obtém a lista de UUIDs dos jogadores com bate-papo ativado para o clã específico
            Set<UUID> enabledPlayers = clanChatEnabledPlayers.get(clanName);
            Bukkit.getLogger().info(enabledPlayers.toString());
            // Verifica se o conjunto de jogadores ativados é nulo antes de chamar o método contains
            if (enabledPlayers != null) {
                return enabledPlayers.contains(playerId);
            } else {
                Bukkit.getLogger().warning("Enabled players set for clan " + clanName + " is null!");
            }
        } else {
            Bukkit.getLogger().warning("Clan " + clanName + " not found in clan chat enabled players map!");
        }

        return false;
    }
    public void sendMessageToClan(String clanName, String message) {
        if (clanChatEnabledPlayers.containsKey(clanName)) {
            for (UUID playerId : clanChatEnabledPlayers.get(clanName)) {
                Player player = Bukkit.getPlayer(playerId);
                if (player != null && player.isOnline()) {
                    player.sendMessage(message);
                }
            }
        }
    }
}
