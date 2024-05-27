package me.abacate.clans.handles;

import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ItemStackConverter {
    public static Document itemStackToDocument(ItemStack item) {
        Document document = new Document();
        document.put("type", item.getType().toString());
        document.put("amount", item.getAmount());

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            document.put("meta", meta.serialize());
        }

        return document;
    }

    public static ItemStack documentToItemStack(Document document) {
        String type = document.getString("type");
        int amount = document.getInteger("amount");

        ItemStack item = new ItemStack(org.bukkit.Material.valueOf(type), amount);

        if (document.containsKey("meta")) {
            ItemMeta meta = (ItemMeta) org.bukkit.configuration.serialization.ConfigurationSerialization.deserializeObject((Map<String, Object>) document.get("meta"));
            item.setItemMeta(meta);
        }

        return item;
    }
}
