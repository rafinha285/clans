package me.abacate.clans.commands;

import me.abacate.clans.managers.InviteManager;
import me.abacate.clans.types.ClanInvite;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RejectInvite implements CommandExecutor{
    private final InviteManager inviteManager;

    public RejectInvite(InviteManager inviteManager) {
        this.inviteManager = inviteManager;
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
        inviteManager.removeInvite(playerUUID);

        player.sendMessage("Você rejeitou o convite para o clã " + invite.getClanName());
        Player senderPlayer = Bukkit.getPlayer(invite.getSender());
        if (senderPlayer != null) {
            senderPlayer.sendMessage(player.getName() + " rejeitou seu convite para o clã.");
        }

        return true;
    }
}
