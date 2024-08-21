package dev.m7wq.project;

import dev.m7wq.project.mysql.MySQL;
import dev.m7wq.project.mysql.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;

public final class Project extends JavaPlugin {


    // Cache time        // playerName , His stats
    public static HashMap<String , PlayerStats> playerStats = new HashMap<>();






    MySQL mySQL;

    public MySQL getMySQL() {
        return mySQL;
    }



    public static Project getInstance(){
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        init();
        connect();

        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        try {
            getMySQL().getConnection().close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        getMySQL().disconnect();
    }

    public void init(){
        mySQL = new MySQL();

    }

    private static Project INSTANCE;

    public void connect()  {
        try {
            getMySQL().connect(this);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
