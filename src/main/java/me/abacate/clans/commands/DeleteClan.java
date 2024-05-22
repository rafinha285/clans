package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeleteClan implements CommandExecutor, TabExecutor {
    ClanManager clanManager;
    public DeleteClan(MongoDatabase database){
        clanManager = new ClanManager(database);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length != 1) {
            commandSender.sendMessage("Falta argumentos");
            Bukkit.getLogger().info("Falta argumentos");
            return true;
        }
        ClanMongo clan = clanManager.getClan(args[0]);
        Player player = (Player) commandSender;
        Bukkit.getLogger().info(clan.getOwner()+ " "+ player.getUniqueId().toString());
        if(!Objects.equals(clan.getOwner(), player.getUniqueId().toString())){
            Bukkit.getLogger().info("Você não é o dono do clã");
            commandSender.sendMessage("Você não é o dono do clã");
            return true;
        }
        clanManager.deleteClan(clan);
        Bukkit.getLogger().warning("Clã: "+clan.getName()+" deletado com sucesso");
        commandSender.sendMessage("Clã: "+clan.getName()+" deletado com sucesso");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1){
            return clanManager.getAllClans().stream().map(a->a.getName()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
