package me.abacate.clans.managers;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
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
                    (List<String>) clanDoc.get("members"),
                    clanDoc.getInteger("points"),
                    (List<String>) clanDoc.get("enemies"),
                    (List<String>) clanDoc.get("allies"),
                    clanDoc.getString("owner"));
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
                    (List<String>) document.get("members"),
                    document.getInteger("points"),
                    (List<String>) document.get("enemies"),
                    (List<String>) document.get("allies"),
                    document.getString("owner")
            );
            clans.add(clan);
        }
        return clans;
    }
    //create
    public void addClanToDatabase(String clan,UUID owner){
//        Bukkit.getLogger().info(new ClanConfiguration().getClan(clan));
        List<String> members = new ArrayList<>();
        members.add(owner.toString());
        ClanMongo clanNew = new ClanMongo(
                clan,
                members,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                owner.toString());
        Document clanDoc = new Document()
                .append("name",clanNew.getName())
                .append("members",clanNew.getMembers())
                .append("points",clanNew.getPoints())
                .append("enemies",clanNew.getEnemies())
                .append("allies",clanNew.getAllies())
                .append("owner",clanNew.getOwner().toString());
        clanCollection.insertOne(clanDoc);
    }
    //delete
    public void deleteClan(ClanMongo clan){
        clanCollection.deleteOne(eq("name",clan.getName()));
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

    //add
    public void addMembers(ClanMongo clan, Player playerToAdd){
        List<String> members = clan.getMembers();
        members.add(playerToAdd.getUniqueId().toString());
        clan.setMembers(members);
        Document query = new Document("name", clan.getName());
        Document update = new Document("$set", new Document("members", members));

        UpdateResult result = clanCollection.updateOne(query, update);
        if (result.getMatchedCount() > 0) {
            System.out.println("Clan atualizado com sucesso.");
        } else {
            System.out.println("Nenhum clã encontrado com o nome especificado.");
        }
//        return true;
    }
    public void addAlly(ClanMongo clan,ClanMongo ally){
        List<String> allies1 = clan.getAllies();
        Bukkit.getLogger().info(allies1.toString());
        allies1.add(ally.getName());
        Bukkit.getLogger().info(allies1.toString());
        List<String> allies2 = ally.getAllies();
        Bukkit.getLogger().info(allies2.toString());
        allies2.add(clan.getName());
        Bukkit.getLogger().info(allies2.toString());

        Document query1 = new Document("name", clan.getName());
        Document update1 = new Document("$set",new Document("allies",allies1));
        clanCollection.updateOne(query1,update1);
        Document query2 = new Document("name", ally.getName());
        Document update2 = new Document("$set",new Document("allies",allies2));
        clanCollection.updateOne(query2,update2);
    }
}
