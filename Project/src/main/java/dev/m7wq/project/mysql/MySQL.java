package dev.m7wq.project.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.m7wq.project.Project;
import org.bukkit.configuration.file.FileConfiguration;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {


    HikariDataSource dataSource = new HikariDataSource();
    HikariConfig dataSourceConfig = new HikariConfig();

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public boolean connect(Project plugin) throws SQLException {
        boolean isConnected = initDatasource(plugin.getConfig());
        if (isConnected)
            initDatabase();
        return isConnected;
    }



    public void initDatabase(){
        try(Connection connection = getConnection()){
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS pvp_stats(" +
                    "kills INT," +
                    "deaths INT," +
                    "points INT," +
                    "coins INT," +
                    "scramble TINYINT(0)," +
                    "IGN VARCHAR(36) PRIMARY KEY" +
                    ")");
            System.out.println("PVP plugin is initialized");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPlayerStats(PlayerStats object, String IGN){
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO pvp_stats(kills,deaths,points,coins,scramble,IGN) VALUES (?,?,?,?,?,?)")){
            statement.setInt(1,object.getKills());
            statement.setInt(2,object.getDeaths());
            statement.setInt(3,object.getPoints());
            statement.setInt(4,object.getCoins());
            statement.setBoolean(5,false);
            statement.setString(6,IGN);

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public synchronized void saveStats(PlayerStats playerStats , String IGN){
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE pvp_stats SET kills = ?, deaths = ?, points = ? , coins = ?, scramble = ? WHERE IGN = ?")){
            statement.setInt(1,playerStats.getKills());
            statement.setInt(2,playerStats.getDeaths());
            statement.setInt(3,playerStats.getPoints());
            statement.setInt(4,playerStats.getCoins());
            statement.setBoolean(5,playerStats.isScramble());
            statement.setString(6,IGN);

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // getPlayerStats
    // hasStats

    public PlayerStats getPlayerStats(String IGN){
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM pvp_stats WHERE IGN = ?")){
            statement.setString(1,IGN);

            //

            ResultSet resultSet = statement.executeQuery();


            int kills = resultSet.getInt("kills");
            int deaths = resultSet.getInt("deaths");
            int points = resultSet.getInt("points");
            int coins = resultSet.getInt("coins");
            boolean scramble = resultSet.getBoolean("scramble");


            PlayerStats stats = new PlayerStats(kills,deaths,points,scramble,coins);

            return stats;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean hasStats(String IGN){
        try (Connection connection = getConnection();PreparedStatement statement=  connection.prepareStatement("SELECT * FROM pvp_stats WHERE IGN = ?")){
            statement.setString(1,IGN);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean initDatasource(FileConfiguration c) {
        try {
            String driver = c.getString("DATABASE.driver");
            String host = c.getString("DATABASE.host");
            int port = c.getInt("DATABASE.port");
            String username = c.getString("DATABASE.username");
            String password = c.getString("DATABASE.password");
            String database = c.getString("DATABASE.database");
            String databaseProperties = c.getString("DATABASE.databaseProperties");
            int maximumPoolSize = c.getInt("DATABASE.maximumPoolSize");
            this.dataSourceConfig.setPoolName("PvP");
            this.dataSourceConfig.setMaximumPoolSize(maximumPoolSize);
            this.dataSourceConfig.setIdleTimeout(0L);
            this.dataSourceConfig.setUsername(username);
            this.dataSourceConfig.setPassword(password);
            this.dataSourceConfig.setJdbcUrl(driver + host + ":" + port + "/" + database + databaseProperties);
            this.dataSource = new HikariDataSource(this.dataSourceConfig);
            System.out.println("PVP - Connected to database!");
            return true;
        } catch (Exception e) {
            System.out.println("PVP - Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean isClosed() {
        return (this.dataSource == null || !this.dataSource.isRunning() || this.dataSource.isClosed());
    }

    public void disconnect() {
        if (isClosed())
            System.out.println("PVP - Database is already closed!");
        this.dataSource.close();
        System.out.println("PVP - Successfully disconnected from the database.");
    }
}
