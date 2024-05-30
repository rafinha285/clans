package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanChatManager;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChangeChat implements CommandExecutor, TabExecutor {
    ClanManager clanManager;
    ClanChatManager clanChatManager;
    public ChangeChat(MongoDatabase database,ClanChatManager clanChatManager){
        clanManager = new ClanManager(database);
        this.clanChatManager = clanChatManager;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(player));

            if (clan != null) {
                clanChatManager.toggleClanChat(player.getUniqueId(), clan.getName());
                boolean enabled = clanChatManager.isClanChatEnabled(player.getUniqueId(), clan.getName());
                player.sendMessage("Clan chat " + (enabled ? "enabled" : "disabled"));
            } else {
                player.sendMessage("You are not in a clan.");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return new ArrayList<>();
    }
}
