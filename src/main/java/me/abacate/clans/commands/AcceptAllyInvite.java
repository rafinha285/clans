package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.AllyInviteManager;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.managers.InviteManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class AcceptAllyInvite implements CommandExecutor, TabExecutor {
    ClanManager clanManager;
    private final AllyInviteManager allyInviteManager;
    public AcceptAllyInvite(AllyInviteManager inviteManager, MongoDatabase database){
        clanManager = new ClanManager(database);
        this.allyInviteManager = inviteManager;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length != 1){
            commandSender.sendMessage("Falta argumentos");
            return true;
        }
        Player player = (Player) commandSender;
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(player));
        if(clan == null){
            commandSender.sendMessage("Você não é dono de nenhum clã");
            return true;
        }
        if(!clan.isOwner(player.getUniqueId())){
            player.sendMessage("Você não é o dono do Clã");
            return true;
        }
        ClanMongo allyClan = clanManager.getClan(args[0]);
        if(allyClan == null){
            commandSender.sendMessage("Clã "+args[0]+" não existe");
            return true;
        }
        Bukkit.getLogger().info("addAlly clan ally");
        clanManager.addAlly(clan,allyClan);
        allyInviteManager.removeAllyInvite(allyClan.getName());
        Bukkit.getLogger().info("Aliado de clã adicionado ");
        commandSender.sendMessage("Aliado de clã adicionado ");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
//        String clan = clanManager.getClanFromPlayer((Player) commandSender);
//        allyInviteManager.getAllyInviter(clan);
//        return ;

        return null;
    }
}
