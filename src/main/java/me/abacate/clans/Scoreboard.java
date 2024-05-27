package me.abacate.clans;

import com.mongodb.client.MongoDatabase;
import me.abacate.clans.config.ConfigManager;
import me.abacate.clans.handles.ChatHandles;
import me.abacate.clans.managers.ClanManager;
import me.abacate.clans.managers.PlayerManager;
import me.abacate.clans.types.ClanMongo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Scoreboard implements Runnable{
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private ClanManager clanManager;
    private PlayerManager playerManager;
    private ConfigManager configManager;
    int TOP_KILLS_COUNT = 5;
    public Scoreboard(MongoDatabase database,FileConfiguration config){
        configManager = new ConfigManager(config);
        clanManager = new ClanManager(database);
        playerManager = new PlayerManager(database);
    }
    @Override
    public void run() {
        for (Player player: Bukkit.getOnlinePlayers()){
            createNewScoreboard(player);
        }
    }
    public void createNewScoreboard(Player player){
//        OfflinePlayer[] players = Bukkit.getOfflinePlayers();
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Kills", Criteria.DUMMY,"KillCount");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.WHITE+""+ChatColor.BOLD+"QUINTILIS");

        objective.getScore(ChatColor.WHITE+" ").setScore(16);
        objective.getScore(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+ "-- Honra de Morte --").setScore(15);
        objective.getScore(ChatColor.WHITE+"  ").setScore(14);

        updateScoreboard(player);
        objective.getScore(ChatColor.WHITE+"   ").setScore(7);
        objective.getScore(ChatColor.AQUA+""+ChatColor.BOLD+"-- Honra dos Clãs --").setScore(6);
        objective.getScore(ChatColor.WHITE+"    ").setScore(5);
        updateScoreboardClans();
        player.setScoreboard(scoreboard);
    }
    private void updateScoreboard(Player player){
        Objective objective = scoreboard.getObjective("Kills");
//        Bukkit.getLogger().info(player.getName());
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
//        ClanMongo clanPlayer = clanManager.getClan(clanManager.getClanFromPlayer(players));
        Arrays.sort(players, (player1, player2)->{
            int kills1 = playerManager.getPointsPlayer(player1);
            int kills2 = playerManager.getPointsPlayer(player2);
            return Double.compare(kills2,kills1);
        });
        for(int i = 0;i<Math.min(TOP_KILLS_COUNT,players.length);i++){
//            int kills = players[i].getStatistic(Statistic.PLAYER_KILLS);
            int kills = playerManager.getPointsPlayer(players[i]);

//            Bukkit.getLogger().info(String.valueOf(kills));
//            Bukkit.getLogger().info(clanAPI.getClanPlayerByBukkitPlayer(players[i].getPlayer()).toString());
            objective.getScore(new ChatHandles(configManager).checkChatColor(kills)+
//                            ""+
//                            "[]"+
                            players[i].getName()+
                            " "+
                            ChatColor.RESET+
                            kills)
                    .setScore(13-i);
//            System.out.println(clanAPI.getAllClans().values());
//            System.out.println(clanPlayer);
//            System.out.println(clanPlayer.getClanFinalName());

//            Bukkit.getLogger().info("aaa "+clanPlayer.getClanPrefix());

        }
    }
    private void updateScoreboardClans(){
        Objective objective = scoreboard.getObjective("Kills");



        List<ClanMongo> clans = clanManager.getAllClans();
        Collections.sort(clans,(clan1, clan2)->{
            int points1 = clan1.getPoints();
            int points2 = clan2.getPoints();
            return Integer.compare(points2,points1);
        });


        for(int i = 0;i<Math.min(clans.size(),5);i++){
            ClanMongo clan = clans.get(i);


            objective.getScore(ChatColor.GRAY+
                    "["+clan.getPrefix()+"]"+
                    new ChatHandles(configManager).checkChatColorClan(clan.getPoints())+
                    clan.getName()+
                    ": "+
                    ChatColor.RESET+
                    clan.getPoints()).setScore(4-i);
        }

    }
}
