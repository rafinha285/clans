package me.abacate.clans.listeners;

import me.abacate.clans.managers.ClanChestManager;
import me.abacate.clans.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InventoryCloseListener implements Listener {
    private ClanChestManager clanChestManager;
    private PlayerManager playerManager;

    public InventoryCloseListener(ClanChestManager clanInventoryManager, PlayerManager playerManager) {
        this.clanChestManager = clanInventoryManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (title.startsWith("Inventário do Clã: ")) {
            String clanName = title.replace("Inventário do Clã: ", "");
            Inventory inventory = event.getInventory();

            clanChestManager.saveClanInventory(clanName, Arrays.asList(inventory.getContents()));
        }
    }
}
