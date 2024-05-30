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
import java.util.Arrays;
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
            List<Document> pipeline = Arrays.asList(
                    new Document("$match", new Document("clan", clan.getName())), // Supondo que `getName()` retorna o nome do clã
                    new Document("$group", new Document("_id", "").append("points", new Document("$sum", "$points"))),
                    new Document("$project", new Document("_id", 0).append("points", "$points"))
            );
            int points = 0;
            MongoCursor<Document> cursor = playerManager.playerCollection.aggregate(pipeline).iterator();
            if (cursor.hasNext()) {
                Document result = cursor.next();
                points = result.getInteger("points", 0);
            }
            cursor.close();

            clanManager.setPoints(clan,points);

        }
    }
}