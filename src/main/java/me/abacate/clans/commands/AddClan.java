package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.managers.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddClan implements CommandExecutor, TabExecutor {
    ClanManager clanManager;
    PlayerManager playerManager;

    public AddClan(MongoDatabase database){
        clanManager = new ClanManager(database);
        playerManager = new PlayerManager(database);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command,  String s, String[] args) {
        if(args.length != 1){
            commandSender.sendMessage("Falta argumentos");
            return true;
        }
        if(!playerManager.isInClan((Player) commandSender)){
            commandSender.sendMessage("Você já esta em um clã, saia do atual para criar outro");
            return true;
        }
        if(clanManager.getClan(args[0])!=null){
            commandSender.sendMessage("Clã ja existe");
            return true;
        }
        clanManager.addClanToDatabase(args[0],((Player) commandSender).getUniqueId());
        commandSender.sendMessage("Criado Clã: "+args[0]);
        return true;
    }

    @Override
    public List<String> onTabComplete( CommandSender commandSender,  Command command,  String s,  String[] strings) {
        return new ArrayList<>();
    }
}