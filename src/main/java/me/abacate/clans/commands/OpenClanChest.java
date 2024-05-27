package me.abacate.clans.commands;

import me.abacate.clans.managers.ClanChestManager;
import me.abacate.clans.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OpenClanChest implements CommandExecutor, TabExecutor {
    private ClanChestManager clanChestManager;
    private PlayerManager playerManager;
    public OpenClanChest(ClanChestManager clanInventoryManager, PlayerManager playerManager) {
        this.clanChestManager = clanInventoryManager;
        this.playerManager = playerManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        String clanName = playerManager.getClan(player);
        if (clanName == null || clanName.isEmpty()) {
            player.sendMessage("Você não pertence a um clã.");
            return true;
        }
        Inventory clanInventory = Bukkit.createInventory(null, 54, "Inventário do Clã: " + clanName);
        clanInventory.setContents(clanChestManager.loadClanInventory(clanName).toArray(new ItemStack[0]));

        player.openInventory(clanInventory);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
