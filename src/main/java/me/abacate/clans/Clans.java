package me.abacate.clans;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import me.abacate.clans.commands.*;
import me.abacate.clans.listeners.PlayerDeathListener;
import me.abacate.clans.managers.AllyInviteManager;
import me.abacate.clans.managers.InviteManager;
import me.abacate.clans.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Clans extends JavaPlugin implements Listener {

    public MongoClient mongoClient;
    public MongoDatabase database;
    private PlayerManager playerManager;
    private InviteManager inviteManager;
    private AllyInviteManager allyInviteManager;
    private BukkitTask AutoSaveTask;

    //    public MongoCollection<Document> playerPointsCollection;
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Iniciando database para clans");
        mongoClient = MongoClients.create("mongodb://192.168.1.20:27017/");
        database = mongoClient.getDatabase("minecraft");
        playerManager = new PlayerManager(database);
        inviteManager = new InviteManager();
        allyInviteManager = new AllyInviteManager();

        Bukkit.getPluginManager().registerEvents(this, this);
//        Bukkit.getPluginManager().registerEvents(new CreateClanListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(database,this),this);
        AutoSaveTask = getServer().getScheduler().runTaskTimer(this,new AutoSave(database),100,200);
        Bukkit.getLogger().info(getCommand("addClan").toString());

        getCommand("deleteClan").setExecutor(new DeleteClan(database));
        getCommand("addClan").setExecutor(new AddClan(database));
        getCommand("sendInvite").setExecutor(new SendInvite(inviteManager,database));
        getCommand("acceptInvite").setExecutor(new AcceptInvite(inviteManager,database));
        getCommand("rejectInvite").setExecutor(new RejectInvite(inviteManager));

        getCommand("sendAllyInvite").setExecutor(new SendAllyInvite(allyInviteManager,database));
        getCommand("acceptAllyInvite").setExecutor(new AcceptAllyInvite(allyInviteManager,database));
        getCommand("rejectAllyInvite").setExecutor(new RejectAllyInvite(allyInviteManager,database));
//        playerPointsCollection =
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Bukkit.getLogger().info("clansManager "+player.getName());
        playerManager.checkAndAddPlayer(player);
    }

    @Override
    public void onDisable() {
        mongoClient.close();
        // Plugin shutdown logic
    }
}
