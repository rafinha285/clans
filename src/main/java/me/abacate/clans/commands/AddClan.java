package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddClan implements CommandExecutor, TabExecutor {
    ClanManager clanManager;

    public AddClan(MongoDatabase database){
        clanManager = new ClanManager(database);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command,  String s, String[] args) {
        if(args.length != 1){
            commandSender.sendMessage("Falta argumentos");
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