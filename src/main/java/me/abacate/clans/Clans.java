package me.abacate.clans;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import me.abacate.clans.commands.*;
import me.abacate.clans.config.ConfigManager;
import me.abacate.clans.listeners.PlayerDeathListener;
import me.abacate.clans.managers.*;
import me.abacate.clans.types.ClanMongo;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public final class Clans extends JavaPlugin implements Listener {

    public MongoClient mongoClient;

    public MongoDatabase database;
    private PlayerManager playerManager;
    private InviteManager inviteManager;
    private AllyInviteManager allyInviteManager;
    private ClanManager clanManager;
    private ClanChatManager clanChatManager;
    private ConfigManager configManager;
//    private ClanChestManager chestManager;
    private BukkitTask AutoSaveTask;
    private BukkitTask ScoreBoardTask;
    private LuckPerms luckPerms;

    //    public MongoCollection<Document> playerPointsCollection;
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Iniciando database para clans");
        if (configFileExists()) {
            getLogger().info("Arquivo de configuração encontrado.");
        } else {
            getLogger().info("Arquivo de configuração não encontrado. Criando um novo.");
            saveDefaultConfig(); // Cria um novo arquivo de configuração com os valores padrão
        }
        mongoClient = MongoClients.create("mongodb://192.168.1.20:27017/");
        database = mongoClient.getDatabase("minecraft");
        playerManager = new PlayerManager(database);
        inviteManager = new InviteManager();
        allyInviteManager = new AllyInviteManager();
//        chestManager = new ClanChestManager(database);
        clanManager = new ClanManager(database);
        clanChatManager = new ClanChatManager();

        luckPerms= LuckPermsProvider.get();

        Bukkit.getPluginManager().registerEvents(this, this);
//        Bukkit.getPluginManager().registerEvents(new CreateClanListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(database,this),this);
        AutoSaveTask = getServer().getScheduler().runTaskTimer(this,new AutoSave(database),100,200);
        ScoreBoardTask = getServer().getScheduler().runTaskTimer(this,new Scoreboard(database,getConfig()),1,40);
        Bukkit.getLogger().info(getCommand("addClan").toString());

        getCommand("deleteClan").setExecutor(new DeleteClan(database));
        getCommand("addClan").setExecutor(new AddClan(database));

        getCommand("sendInvite").setExecutor(new SendInvite(inviteManager,database));
        getCommand("acceptInvite").setExecutor(new AcceptInvite(inviteManager,database));
        getCommand("rejectInvite").setExecutor(new RejectInvite(inviteManager));

        getCommand("sendAllyInvite").setExecutor(new SendAllyInvite(allyInviteManager,database));
        getCommand("acceptAllyInvite").setExecutor(new AcceptAllyInvite(allyInviteManager,database));
        getCommand("rejectAllyInvite").setExecutor(new RejectAllyInvite(allyInviteManager,database));

        getCommand("addEnemy").setExecutor(new AddEnemy(database,getConfig()));

        getCommand("leaveClan").setExecutor(new LeaveClan(database));

        getCommand("removeAlly").setExecutor(new RemoveAlly(database));
        getCommand("removeEnemy").setExecutor(new RemoveEnemy(database,getConfig()));
        getCommand("removeMember").setExecutor(new RemoveMember(database,configManager));

        getCommand("setPrefix").setExecutor(new SetPrefix(database));

//        getCommand("toggleChat").setExecutor(new ChangeChat(database,clanChatManager));
//        getCommand("openClanChest").setExecutor(new OpenClanChest(chestManager,playerManager));


        ClanChestManager clanInventoryManager = new ClanChestManager(database);
        PlayerManager playerManager = new PlayerManager(database);

//        playerPointsCollection =
    }
    private boolean configFileExists() {
        // Obtenha a pasta de dados do plugin
        File dataFolder = getDataFolder();
        // Crie um objeto File para o arquivo de configuração
        File configFile = new File(dataFolder, "config.yml");
        // Verifique se o arquivo existe
        return configFile.exists();
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Bukkit.getLogger().info("clansManager "+player.getName());
        playerManager.checkAndAddPlayer(player);
    }

    @Override
    public void onDisable() {
        if(!AutoSaveTask.isCancelled()){
            AutoSaveTask.isCancelled();
        }
        if(!ScoreBoardTask.isCancelled()){
            ScoreBoardTask.isCancelled();
        }
//        for (ClanMongo clan: clanManager.getAllClans()){
//            chestManager.saveClanInventory(clan.getName(),chestManager.loadClanInventory(clan.getName()));
//        }
        mongoClient.close();
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
//        Bukkit.getLogger().info(message);
        Player player = event.getPlayer();

        String clanName = clanManager.getClanFromPlayer(player);
        ClanMongo clan = clanManager.getClan(clanName);
//        String clanPrefix = (clan == null) ? "" : clan.getPrefix();
        String primaryGroup = luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
        Bukkit.getLogger().info(!clanName.isEmpty()?clanName:"aaaaa cu");
        Bukkit.getLogger().info(clanChatManager.isClanChatEnabled(player.getUniqueId(),clanName)?player.getName():"cu2haaghagaha");
        if(!clanName.isEmpty()&& clanChatManager.isClanChatEnabled(player.getUniqueId(),clanName)){
            event.setCancelled(true);
            String format = buildChatFormat(player, message, clan, primaryGroup);
            clanChatManager.sendMessageToClan(clan.getName(), format);
            Bukkit.getLogger().info(format);
        }else{
            String format = buildChatFormat(player, message, clan, primaryGroup);
            event.setFormat(format);
        }
    }


    private String buildChatFormat(Player player,String message,ClanMongo clan,String primaryGroup){
        String format = "";
        if (primaryGroup != null) {
            switch (primaryGroup) {
                case "Vilão":
                    format += ChatColor.RED;
                    break;
                case "Aldeão":
                    format += ChatColor.BLUE;
                    break;
                case "ADM":
                    format += ChatColor.LIGHT_PURPLE;
                    break;
                default:
                    format += ChatColor.WHITE;
            }
            format += "[" + primaryGroup + "] " + ChatColor.RESET;
        }
        if (clan.getPrefix() != null && !clan.getPrefix().isEmpty()) {
            format += ChatColor.AQUA + "[" + clan.getPrefix() + "]" + ChatColor.RESET;
        }
        format += " <" + player.getName() + ">: " + message;
        return format;
    }

}
