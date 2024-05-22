package me.abacate.clans;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import me.abacate.clans.commands.AddClan;
import me.abacate.clans.listeners.PlayerDeathListener;
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
    private BukkitTask AutoSaveTask;

    //    public MongoCollection<Document> playerPointsCollection;
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Iniciando database para clans");
        mongoClient = MongoClients.create("mongodb://192.168.1.20:27017/");
        database = mongoClient.getDatabase("minecraft");
        playerManager = new PlayerManager(database);

        Bukkit.getPluginManager().registerEvents(this, this);
//        Bukkit.getPluginManager().registerEvents(new CreateClanListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(database,this),this);
        AutoSaveTask = getServer().getScheduler().runTaskTimer(this,new AutoSave(database),100,200);
        Bukkit.getLogger().info(getCommand("addClan").toString());
        if (getCommand("addClan") == null) {
            getLogger().severe("Comando 'addClan' não encontrado no plugin.yml");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("addClan").setExecutor(new AddClan(database));
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
