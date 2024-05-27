package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveAlly implements CommandExecutor, TabExecutor {
    private ClanManager clanManager;
    public RemoveAlly(MongoDatabase database){
        clanManager = new ClanManager(database);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length!=1){
            commandSender.sendMessage("Falta Argumentos");
            return true;
        }
        Player player = (Player) commandSender;
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(player));
        ClanMongo clanTarget = clanManager.getClan(args[0]);
        if(clan ==null){
            commandSender.sendMessage("Você não esta em nenhum clã");
            return true;
        }
        if(clanTarget == null){
            commandSender.sendMessage("Esse clã não existe");
            return true;
        }
        if(!clan.isOwner(player.getUniqueId())){
            commandSender.sendMessage("Você não é o dono do clã");
            return true;
        }
        clanManager.removeAlly(clan,clanTarget);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(player));
        return clan.getAllies();
    }
}
