package me.abacate.clans.listeners;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.Clans;
import me.abacate.clans.config.ConfigManager;
import me.abacate.clans.managers.PlayerManager;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.checkerframework.checker.units.qual.C;

public class PlayerDeathListener implements Listener {
    private ConfigManager configManager;
    private PlayerManager playerManager;
    private ClanManager clanManager;
    public PlayerDeathListener(MongoDatabase database, Clans plugin){
        playerManager = new PlayerManager(database);
        clanManager = new ClanManager(database);
        configManager = new ConfigManager(plugin.getConfig());
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer!= null){
            int victimPoints = playerManager.getPointsPlayer(victim);
            int killerPoints = playerManager.getPointsPlayer(killer);
            ClanMongo victimClan = clanManager.getClan(clanManager.getClanFromPlayer(victim));
            ClanMongo killerClan = clanManager.getClan(clanManager.getClanFromPlayer(killer));
            boolean isEnemy;
            boolean isAlly;
            boolean sameClan;
            boolean isInClan;

            if(!(killerClan == null || victimClan == null)){
                isEnemy = clanManager.isEnemy(victimClan,killerClan);
                isAlly = clanManager.isAlly(victimClan,killerClan);
            }else{
                isEnemy= false;
                isAlly = false;
//                sameClan = false;
            }
//            if()
//            if(victimClan.getName() == ""){
//                isInClan = false;
//            }
            if(victimClan == killerClan&&!(victimClan==null)&&!(victimClan==null)){
                sameClan= true;

            }else {
                sameClan=false;

            }
            if(sameClan){
                Bukkit.getLogger().info("são do mesmo clã");
            }else{
                if(isEnemy){
                    if(victimPoints == 0){
                        playerManager.setPointsPlayer(killer,killerPoints+configManager.getEnemyPoints());
    
                    }else{
                        playerManager.setPointsPlayer(killer,killerPoints+victimPoints);
                        playerManager.setPointsPlayer(victim,0);
                    }
                }else{
                    if(isAlly){
                        Bukkit.getLogger().info("são aliados");
                    }else{
                        if(victimPoints == 0){
                            playerManager.setPointsPlayer(killer,killerPoints+configManager.getKillPoints());
                        }else{
                            playerManager.setPointsPlayer(killer,killerPoints+victimPoints);
                            playerManager.setPointsPlayer(victim,0);
                        }
                    }
                }
            }
            
        }
    }
}