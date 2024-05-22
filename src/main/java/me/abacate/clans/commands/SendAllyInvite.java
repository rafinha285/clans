package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.AllyInviteManager;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
//import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SendAllyInvite implements CommandExecutor, TabExecutor {
    private ClanManager clanManager;
    private final AllyInviteManager allyInviteManager;
    public SendAllyInvite(AllyInviteManager inviteManager,MongoDatabase database){
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
        ClanMongo targetClan = clanManager.getClan(args[0]);

        if (targetClan == null) {
            commandSender.sendMessage("Clã "+args[0]+" não existe");
        }
        String owner = targetClan.getOwner();
        allyInviteManager.sendAllyInvite(clan.getName(),targetClan.getName());
        commandSender.sendMessage("Convite enviado");
        Bukkit.getPlayer(UUID.fromString(owner)).sendMessage(ChatColor.LIGHT_PURPLE +"Você recebeu um convite para se aliar com o clã " + ChatColor.GREEN+ clan.getName() + " de " + player.getName()+"\n"+
                ChatColor.GOLD+"use /acceptAllyInvite "+clan.getName()+" para aceitar convite");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return clanManager.getAllClans().stream().map(a->a.getName()).collect(Collectors.toList());
    }
}
