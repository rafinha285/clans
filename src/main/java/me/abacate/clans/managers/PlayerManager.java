package me.abacate.clans.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.abacate.clans.types.ClanMongo;
import me.abacate.clans.types.PlayerMongo;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Level;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class PlayerManager {

    public MongoCollection<Document> playerCollection;
    public PlayerManager(MongoDatabase database){
        this.playerCollection = database.getCollection("playerPoints");
    }
    public boolean isInDatabase(Player player){
//        Document query = new Document("_id",player.getUniqueId().toString());
        Object playerDoc = playerCollection.find(eq("_id",player.getUniqueId().toString())).first();
        return playerDoc!=null;
    }
    public boolean setPointsPlayer(Player player,Integer points){
        if(!isInDatabase(player)){
            Bukkit.getLogger().log(Level.parse("severe"),"O player naoe sta na database");
            return false;
        }else{
            playerCollection.updateOne(eq("_id",player.getUniqueId().toString()),set("points",points));
            return true;
        }
    }
    public String getClan(Player player){
        return playerCollection.find(eq("_id",player.getUniqueId().toString())).first().getString("clan");
    }
    public int getPointsPlayer(Player player){
        Document playerDoc = playerCollection.find(eq("_id",player.getUniqueId().toString())).first();
        return playerDoc.getInteger("points");
    }
    public void checkAndAddPlayer(Player player) {
        Document query = new Document("_id",player.getUniqueId().toString());
        Object playerDoc = playerCollection.find(query).first();
        PlayerMongo playerMongo = new PlayerMongo(player.getUniqueId().toString(),player.getName(),"",0);
//        Bukkit.getLogger().info(playerDoc.toString());
        if (playerDoc == null) {
            // O jogador não está no banco de dados, então adicionamos
            Document newPlayer = new Document("_id", playerMongo.getId().toString())
                    .append("name", playerMongo.getName())
                    .append("clan", playerMongo.getClan())  // Adiciona o jogador com um valor de clã vazio
                    .append("points", playerMongo.getPoints());
            playerCollection.insertOne(newPlayer);
            Bukkit.getLogger().info("adicionado");
        }
    }
    //set
    public void setClan(Player player,String clan){
        playerCollection.updateOne(eq("_id",player.getUniqueId().toString()),new Document("$set",new Document("clan",clan)));
    }
    public boolean isInClan(Player player){
        return playerCollection.find(eq("_id",player.getUniqueId().toString())).first().getString("clan") !=null;
    }
}