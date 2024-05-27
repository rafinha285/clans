package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetPrefix implements CommandExecutor, TabExecutor {
    ClanManager clanManager;
    public SetPrefix(MongoDatabase database){
        clanManager = new ClanManager(database);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length != 1){
            commandSender.sendMessage("Falta argumentos");
            return true;
        }
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer((Player) commandSender));
        Player player = (Player) commandSender;
        if(!clan.isOwner(player.getUniqueId())){
            commandSender.sendMessage("Você não é o dono do clã");
            return true;
        }
        clanManager.setPrefix(clan,args[0]);
        commandSender.sendMessage("Prefixo setado");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
