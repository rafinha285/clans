package me.abacate.clans.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import me.abacate.clans.handles.ItemStackConverter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

public class ClanChestManager {
    private MongoCollection<Document> clanInventoryCollection;

    public ClanChestManager(MongoDatabase database) {
        this.clanInventoryCollection = database.getCollection("clanchests");
    }

    public void saveClanInventory(String clanName, List<ItemStack> items) {
        List<Document> itemDocuments = new ArrayList<>();
        for (ItemStack item : items) {
            itemDocuments.add(ItemStackConverter.itemStackToDocument(item));
        }

        Document query = new Document("clanName", clanName);
        Document update = new Document("$set", new Document("items", itemDocuments));
        clanInventoryCollection.updateOne(query, update, new com.mongodb.client.model.UpdateOptions().upsert(true));
    }

    public List<ItemStack> loadClanInventory(String clanName) {
        Document document = clanInventoryCollection.find(eq("clanName", clanName)).first();
        List<ItemStack> items = new ArrayList<>();

        if (document != null) {
            List<Document> itemDocuments = (List<Document>) document.get("items");
            for (Document itemDocument : itemDocuments) {
                items.add(ItemStackConverter.documentToItemStack(itemDocument));
            }
        }

        return items;
    }
    public void deleteClanInventory(String clanName){
        clanInventoryCollection.deleteOne(eq("clanName",clanName));
    }
}
