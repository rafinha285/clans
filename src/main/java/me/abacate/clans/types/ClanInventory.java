package me.abacate.clans.types;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ClanInventory {
    private String clanName;
    private List<ItemStack> items;

    public ClanInventory(String clanName, List<ItemStack> items) {
        this.clanName = clanName;
        this.items = items;
    }

    public String getClanName() {
        return clanName;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }
}
