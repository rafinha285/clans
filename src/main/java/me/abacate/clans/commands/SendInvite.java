package me.abacate.clans.commands;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.managers.InviteManager;
import me.abacate.clans.types.ClanInvite;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SendInvite implements CommandExecutor, TabExecutor {
    private final InviteManager inviteManager;
    private final ClanManager clanManager;

    public SendInvite(InviteManager inviteManager, MongoDatabase database) {
        this.inviteManager = inviteManager;
        this.clanManager = new ClanManager(database);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Somente jogadores podem usar este comando.");
            return true;
        }



        if (args.length != 1) {
            sender.sendMessage("Uso: /invite <jogador>");
            return true;
        }

        Player player = (Player) sender;
        if(Bukkit.getPlayerExact(args[0])==player){
            sender.sendMessage("Não da pra se convidar");
            return true;
        }
        ClanMongo clan = clanManager.getClan(clanManager.getClanFromPlayer(player));
        if(clan == null){
            sender.sendMessage("Você não é dono de nenhum clã");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
//        Bukkit.getLogger().info(String.valueOf(Objects.equals(clan.getOwner(), player.getUniqueId().toString())));
        if(!clan.isOwner(player.getUniqueId())){
            player.sendMessage("Você não é o dono do Clã");
            return true;
        }
        if (target == null) {
            player.sendMessage("Jogador não encontrado.");
            return true;
        }

//        String clanName = "NomeDoClan"; // Obtém o nome do clã do jogador
        ClanInvite invite = new ClanInvite(player.getUniqueId(), target.getUniqueId(), clan.getName());
        inviteManager.addInvite(target.getUniqueId(), invite);

        player.sendMessage("Convite enviado para " + target.getName());
        target.sendMessage(ChatColor.LIGHT_PURPLE +"Você recebeu um convite para o clã " + ChatColor.GREEN+ clan.getName() + " de " + player.getName()+"\n"+
                ChatColor.GOLD+"use /acceptInvite "+ player.getName()+" para aceitar convite");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
