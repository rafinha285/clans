package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.managers.InviteManager;
import me.abacate.clans.managers.PlayerManager;
import me.abacate.clans.types.ClanInvite;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AcceptInvite implements CommandExecutor{
    private final InviteManager inviteManager;
    private final ClanManager clanManager;
    private final PlayerManager playerManager;

    public AcceptInvite(InviteManager inviteManager,MongoDatabase database) {
        this.inviteManager = inviteManager;
        this.clanManager = new ClanManager(database);
        this.playerManager = new PlayerManager(database);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Somente jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (!inviteManager.hasInvite(playerUUID)) {
            player.sendMessage("Você não tem nenhum convite pendente.");
            return true;
        }

        ClanInvite invite = inviteManager.getInvite(playerUUID);

        ClanMongo clan = clanManager.getClan(invite.getClanName());
        clanManager.addMembers(clan,player);
        playerManager.setClan(player,clan.getName());

        inviteManager.removeInvite(playerUUID);

        player.sendMessage("Você aceitou o convite para o clã " + invite.getClanName());
        Player senderPlayer = Bukkit.getPlayer(invite.getSender());
        if (senderPlayer != null) {
            senderPlayer.sendMessage(player.getName() + " aceitou seu convite para o clã.");
        }

        return true;
    }
}
