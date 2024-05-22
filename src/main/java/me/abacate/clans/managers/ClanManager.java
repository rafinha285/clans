package me.abacate.clans.managers;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.PlayerManager;
import me.abacate.clans.types.ClanMongo;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.*;
//import static com.mongodb.client.model.Updates.*;

public class ClanManager {
    private MongoCollection<Document> clanCollection;
    private MongoCollection<Document> playerCollection;
    private PlayerManager playerManager;
    public ClanManager(MongoDatabase database){
        playerManager = new PlayerManager(database);
        playerCollection = database.getCollection("playerPoints");
        clanCollection = database.getCollection("clansPoints");
    }
    //get
    public ClanMongo getClan(String clanName){
        Document clanDoc = clanCollection.find(eq("name",clanName)).first();
        if(clanDoc != null){
            ClanMongo clan = new ClanMongo(clanDoc.getString("name"),
                    (List<UUID>) clanDoc.get("members"),
                    clanDoc.getInteger("points"),
                    (List<String>) clanDoc.get("enemies"),
                    (List<String>) clanDoc.get("allies"),
                    UUID.fromString(clanDoc.getString("owner")));
            return clan;
        }
        return null;
    }
    public String getClanFromPlayer(Player player){
        Document clanDoc = clanCollection.find(in("members",player.getUniqueId().toString())).first();
        if(clanDoc !=null){
            return clanDoc.getString("name");
        }
        return "";
    }
    public List<ClanMongo> getAllClans(){
        List<Document> documents = clanCollection.find().into(new ArrayList<>());
        List<ClanMongo> clans = new ArrayList<>();
        for (Document document : documents) {
            ClanMongo clan = new ClanMongo(
                    document.getString("name"),
                    (List<UUID>) document.get("members"),
                    document.getInteger("points"),
                    (List<String>) document.get("enemies"),
                    (List<String>) document.get("allies"),
                    UUID.fromString(document.getString("owner"))
            );
            clans.add(clan);
        }
        return clans;
    }
    //create
    public void addClanToDatabase(String clan,UUID owner){
//        Bukkit.getLogger().info(new ClanConfiguration().getClan(clan));
        ClanMongo clanNew = new ClanMongo(
                clan,
                new ArrayList<>(),
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                owner);
        Document clanDoc = new Document()
                .append("name",clanNew.getName())
                .append("members",clanNew.getMembers())
                .append("points",clanNew.getPoints())
                .append("enemies",clanNew.getEnemies())
                .append("allies",clanNew.getAllies())
                .append("owner",clanNew.getOwner().toString());
        clanCollection.insertOne(clanDoc);
    }
    //boolean
    public boolean isEnemy(String clan1,String clan2){
//        String clanName = playerManager.getClan(player);
        ClanMongo clan = getClan(clan1);
        if(clan !=null){
            return clan.getEnemies().contains(clan2);
        }
        return false;
    }
    public boolean isInDatabase(String clan){
        Document clanDoc= clanCollection.find(eq("name",clan)).first();
        return clanDoc!=null;
    }
}
