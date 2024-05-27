package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.config.ConfigManager;
import me.abacate.clans.handles.ChatHandles;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RemoveMember implements CommandExecutor, TabExecutor {
    ClanManager clanManager;
    ChatHandles chatHandles;
    public RemoveMember(MongoDatabase database, ConfigManager config){
        clanManager = new ClanManager(database);
        chatHandles = new ChatHandles(config);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length != 1){
            commandSender.sendMessage("Falta argumentos");
            return true;
        }
        Player sender = (Player) commandSender;
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(sender));
        if(clan == null){
            commandSender.sendMessage("Você não esta em nenhum clã");
            return true;
        }
        if(!clan.isOwner(sender.getUniqueId())){
            commandSender.sendMessage("Você não é o dono do clã");
            return true;
        }
        if(Bukkit.getPlayerExact(args[0])==null){
            commandSender.sendMessage("Esse jogador não existe");
            return true;
        }
        Player leaver = Bukkit.getPlayerExact(args[0]);
        clanManager.leaveClan(leaver,clan);
        commandSender.sendMessage("Jogador:"+leaver.getName()+" removido do Clã");
        leaver.sendMessage("Você foi retirado do clã "+ clan.getName());
        chatHandles.sendMessageToAllClan(clan, ChatColor.RED +leaver.getName()+" foi retirado do cla");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(player));
        List<Player> players = new ArrayList<>();
        for(String pS:clan.getMembers()){
            Player p = Bukkit.getPlayer(UUID.fromString(pS));
            players.add(p);
        }
        players.remove(player);
        return players.stream().map(p->p.getName()).collect(Collectors.toList());
    }
}
