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

public class LeaveClan implements CommandExecutor, TabExecutor {
    ClanManager clanManager;
    public LeaveClan(MongoDatabase database){
        clanManager = new ClanManager(database);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(player));

        if(clan ==null){
            commandSender.sendMessage("Você não esta em nenhum clã");
            return true;
        }
        if(clan.isOwner(player.getUniqueId())){
            commandSender.sendMessage("Você não pode sair do clã que é dono, delete ele para poder sair");
            return true;
        }

        clanManager.leaveClan(player,clan);
        commandSender.sendMessage("Você saiu do clã "+clan.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
