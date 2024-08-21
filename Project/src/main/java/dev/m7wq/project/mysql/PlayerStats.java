package dev.m7wq.project.mysql;

public class PlayerStats {

    private int kills;
    private int deaths;
    private int points;
    private int coins;
    private boolean scramble;

    public PlayerStats(int kills, int deaths, int points, boolean scramble, int coins) {
        this.kills = kills;
        this.deaths = deaths;
        this.points = points;
        this.scramble = scramble;
        this.coins = coins;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public boolean isScramble() {
        return scramble;
    }

    public void setScramble(boolean scramble) {
        this.scramble = scramble;
    }
}
