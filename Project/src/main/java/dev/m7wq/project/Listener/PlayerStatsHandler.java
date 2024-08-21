package dev.m7wq.project.Listener;

import dev.m7wq.project.Project;
import dev.m7wq.project.mysql.PlayerStats;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerStatsHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        String playerName = e.getPlayer().getName();

        if (!Project.getInstance().getMySQL().hasStats(playerName)) {


            // insert | register
            Project.getInstance().getMySQL().createPlayerStats(new PlayerStats(0, 0, 0, false, 0), playerName); // INSERT INTO...
            Project.playerStats.put(playerName,Project.getInstance().getMySQL().getPlayerStats(playerName));

        }

        Project.playerStats.put(playerName,Project.getInstance().getMySQL().getPlayerStats(playerName));

    }   // while on the server

    @EventHandler
    public void onLeave(PlayerQuitEvent e){

        String playerName = e.getPlayer().getName();

        PlayerStats playerCurrentlyStats = Project.playerStats.get(playerName);


        Project.getInstance().getMySQL().saveStats(playerCurrentlyStats,playerName);

        Project.playerStats.remove(playerName);

    }



}
