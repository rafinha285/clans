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
    public MongoCollection<Document> clanCollection;
    private MongoCollection<Document> playerCollection;
    private final ClanChestManager chestManager;
    private final PlayerManager playerManager;
    public ClanManager(MongoDatabase database){
        playerManager = new PlayerManager(database);
        playerCollection = database.getCollection("playerPoints");
        clanCollection = database.getCollection("clansPoints");
        chestManager = new ClanChestManager(database);
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
                    clanDoc.getString("owner"),
                    clanDoc.getString("prefix"));
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
                    document.getString("owner"),
                    document.getString("prefix")
            );
            clans.add(clan);
        }
        return clans;
    }
    //set
    public void setPrefix(ClanMongo clan,String prefix){
        Document query = new Document("name",clan.getName());
        Document update = new Document("$set",new Document("prefix",prefix));

        clanCollection.updateOne(query,update);
    }
    public void setPoints(ClanMongo clan,int points){
        Document query = new Document("name",clan.getName());
        Document update = new Document("$set",new Document("points",points));

        clanCollection.updateOne(query,update);
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
                owner.toString(),
                "");
        Document clanDoc = new Document()
                .append("name",clanNew.getName())
                .append("members",clanNew.getMembers())
                .append("points",clanNew.getPoints())
                .append("enemies",clanNew.getEnemies())
                .append("allies",clanNew.getAllies())
                .append("owner",clanNew.getOwner().toString())
                .append("prefix",clanNew.getPrefix());
        clanCollection.insertOne(clanDoc);
//        chestManager.saveClanInventory(clanDoc.getString("name"),new ArrayList<>());
        playerManager.setClan(Bukkit.getPlayer(owner),clanNew.getName());
    }
    //delete
    public void deleteClan(ClanMongo clan){
        for(String member:clan.getMembers()){
            playerCollection.updateOne(eq("_id",member),new Document("$set",new Document("clan","")));
        }
        clanCollection.deleteOne(eq("name",clan.getName()));
//        chestManager.deleteClanInventory(clan.getName());
    }
    //boolean
    public boolean isEnemy(ClanMongo clan1,ClanMongo clan2){
//        String clanName = playerManager.getClan(player);
//        ClanMongo clan = getClan(clan1);
        if(clan1 !=null){
            return clan1.getEnemies().contains(clan2.getName());
        }
        return false;
    }
    public boolean isAlly(ClanMongo clan1,ClanMongo clan2){
//        ClanMongo clan = getClan(clan1);
        if(clan1!=null){
            return clan1.getAllies().contains(clan2.getName());
        }
        return false;
    }
//    public boolean isInDatabase(String clan){
//        Document clanDoc= clanCollection.find(eq("name",clan)).first();
//        return clanDoc!=null;
//    }

    //add
    public void addMembers(ClanMongo clan, Player playerToAdd){
        List<String> members = clan.getMembers();
        members.add(playerToAdd.getUniqueId().toString());
        playerManager.setClan(playerToAdd,clan.getName());
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

    //save
    private void saveAllies(String clan,List<String> allies1,String ally,List<String> allies2){
        Document query1 = new Document("name", clan);
        Document update1 = new Document("$set",new Document("allies",allies1));
        clanCollection.updateOne(query1,update1);
        Document query2 = new Document("name", ally);
        Document update2 = new Document("$set",new Document("allies",allies2));
        clanCollection.updateOne(query2,update2);
    }
    private void saveEnemies(String clan,List<String> enemies1,String enemy,List<String> enemies2){
        Document query1 = new Document("name", clan);
        Document update1 = new Document("$set",new Document("enemies",enemies1));
        clanCollection.updateOne(query1,update1);
        Document query2 = new Document("name", enemy);
        Document update2 = new Document("$set",new Document("enemies",enemies2));
        clanCollection.updateOne(query2,update2);
    }

    public void addAlly(ClanMongo clan,ClanMongo ally){
        List<String> allies1 = clan.getAllies();
        allies1.add(ally.getName());
        List<String> allies2 = ally.getAllies();
        allies2.add(clan.getName());

        saveAllies(clan.getName(),allies1,ally.getName(),allies2);
    }
    public void addEnemy(ClanMongo clan,ClanMongo enemy){
        List<String> enemies1 = clan.getEnemies();
        enemies1.add(enemy.getName());
        List<String> enemies2 = enemy.getEnemies();
        enemies2.add(clan.getName());

        saveEnemies(clan.getName(),enemies1,enemy.getName(),enemies2);
    }
    //remove
    public void removeAlly(ClanMongo clan,ClanMongo ally){
        List<String> allies1 = clan.getAllies();
        List<String> allies2 = ally.getAllies();
        allies1.remove(ally.getName());
        allies2.remove(clan.getName());

        saveAllies(clan.getName(),allies1,ally.getName(),allies2);
    }
    public void removeEnemy(ClanMongo clan,ClanMongo enemy){
        List<String> enemies1 = clan.getEnemies();
        List<String> enemies2 = enemy.getEnemies();
        enemies1.remove(enemy.getName());
        enemies2.remove(clan.getName());

        saveEnemies(clan.getName(),enemies1,enemy.getName(),enemies2);
    }


    public void leaveClan(Player player,ClanMongo clan){
        Document query = new Document("name",clan.getName());
        Document update = new Document("$pull", new Document("members", player.getUniqueId().toString()));
        clanCollection.updateOne(query,update);
        playerManager.setClan(player,"");
    }
}
