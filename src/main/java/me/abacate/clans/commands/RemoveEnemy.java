package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
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

public class RemoveEnemy implements CommandExecutor, TabExecutor {
    private ClanManager clanManager;
    private ConfigManager configManager;
    public RemoveEnemy(MongoDatabase database, FileConfiguration config){
        clanManager = new ClanManager(database);
        configManager = new ConfigManager(config);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length!=1){
            commandSender.sendMessage("Falta Argumentos");
            return true;
        }
        Player playerr = (Player) commandSender;
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(playerr));
        ClanMongo clanTarget = clanManager.getClan(args[0]);
        if(clan ==null){
            commandSender.sendMessage("Você não esta em nenhum clã");
            return true;
        }
        if(clanTarget == null){
            commandSender.sendMessage("Esse clã não existe");
            return true;
        }
        if(!clan.isOwner(playerr.getUniqueId())){
            commandSender.sendMessage("Você não é o dono do clã");
            return true;
        }
        clanManager.removeEnemy(clan,clanTarget);
        for(Player player: Bukkit.getOnlinePlayers()){
            player.sendTitle(ChatColor.AQUA+"TRATADO DE PAZ",ChatColor.ITALIC+""+ChatColor.GOLD+"O clã "+clan.getName()+" entrou deixou de ser inimigo com o clã "+clanTarget.getName(),10,80,10);
        }
        for (String p:clan.getMembers()){
            Player player = Bukkit.getPlayer(UUID.fromString(p));
            player.sendMessage(ChatColor.RED+"Agora cada morte do clã "+ChatColor.GOLD+clanTarget.getName()+ChatColor.RED+" serão "+configManager.getKillPoints()+" Honras para o clã");
        }
        for (String p:clanTarget.getMembers()){
            Player player = Bukkit.getPlayer(UUID.fromString(p));
            player.sendMessage(ChatColor.RED+"Agora cada morte do clã "+ChatColor.GOLD+clan.getName()+ChatColor.RED+" serão "+configManager.getKillPoints()+" Honras para o clã");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(player));
        return clan.getEnemies();
    }
}
