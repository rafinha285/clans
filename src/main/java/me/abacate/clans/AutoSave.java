package me.abacate.clans;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanChestManager;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.managers.PlayerManager;
import me.abacate.clans.types.ClanMongo;
import me.abacate.clans.types.PlayerMongo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class AutoSave implements Runnable{
    private final ClanManager clanManager;
    private final PlayerManager playerManager;
    private final ClanChestManager chestManager;

    public AutoSave(MongoDatabase database){
        clanManager = new ClanManager(database);
        playerManager = new PlayerManager(database);
        chestManager = new ClanChestManager(database);
    }

    @Override
    public void run() {
        for (ClanMongo clan : clanManager.getAllClans()) {
            int sum = 0;

            MongoCursor<Document> cursor = playerManager.playerCollection.find(eq("clan", clan.getName())).iterator();
            try {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    PlayerMongo player = PlayerMongo.fromDocument(doc);
                    sum += player.getPoints();
//                    players.add(player);
                }
            } finally {
                cursor.close();
                clan.setPoints(sum);
            }
        }
    }
}