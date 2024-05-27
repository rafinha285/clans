package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.Clans;
import me.abacate.clans.config.ConfigManager;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddEnemy implements CommandExecutor, TabExecutor {
    ClanManager clanManager;
    ConfigManager configManager;
    public AddEnemy(MongoDatabase database, FileConfiguration config){
        clanManager = new ClanManager(database);
        configManager = new ConfigManager(config);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length!=1){
            commandSender.sendMessage("Falta Argumentos");
            return true;
        }

        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer((Player) commandSender));
        ClanMongo clanTarget = clanManager.getClan(args[0]);
        if(!clan.isOwner(((Player) commandSender).getUniqueId())){
            commandSender.sendMessage("Você não é o dono do clã");
            return true;
        }
        if(clanTarget == null){
            commandSender.sendMessage("Esse clã não existe");
            return true;
        }
        if(clanManager.isAlly(clan,clanTarget)){
            commandSender.sendMessage("Esse clã é seu aliado, temine a aliança antes de coloca-lo como inimigo");
            return true;
        }
        if(clanManager.isEnemy(clan,clanTarget)){
            commandSender.sendMessage("Esse clã ja é seu inimigo");
            return true;
        }
        clanManager.addEnemy(clan,clanTarget);
        for(Player player:Bukkit.getOnlinePlayers()){
            player.sendTitle(ChatColor.RED+"GUERRA!!!!!!!",ChatColor.ITALIC+""+ChatColor.GOLD+"O clã "+clan.getName()+" entrou em guerra com o clã"+clanTarget.getName(),10,80,10);
        }
        for (String p:clan.getMembers()){
            Player player = Bukkit.getPlayer(UUID.fromString(p));
            player.sendMessage(ChatColor.RED+"Agora cada morte do clã "+ChatColor.GOLD+clanTarget.getName()+ChatColor.RED+" serão "+configManager.getEnemyPoints()+" Honras para o clã");
        }
        for (String p:clanTarget.getMembers()){
            Player player = Bukkit.getPlayer(UUID.fromString(p));
            player.sendMessage(ChatColor.RED+"Agora cada morte do clã "+ChatColor.GOLD+clan.getName()+ChatColor.RED+" serão "+configManager.getEnemyPoints()+" Honras para o clã");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer((Player) commandSender));
        List<ClanMongo> clans = clanManager.getAllClans();
        clans.remove(clan);
        return clans.stream().map(a->a.getName()).collect(Collectors.toList());
    }
}
