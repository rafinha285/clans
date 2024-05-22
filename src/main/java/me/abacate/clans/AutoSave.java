package me.abacate.clans;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.managers.PlayerManager;
import me.abacate.clans.types.ClanMongo;

public class AutoSave implements Runnable{
    private ClanManager clanManager;
    private PlayerManager playerManager;

    public AutoSave(MongoDatabase database){
        clanManager = new ClanManager(database);
        playerManager = new PlayerManager(database);

    }

    @Override
    public void run(){
        for (ClanMongo clan: clanManager.getAllClans()){

        }
    }
//    private final static AutoSave instance = new AutoSave(da);
//    public static AutoSave getInstance(){
//        return instance;
//    }
}