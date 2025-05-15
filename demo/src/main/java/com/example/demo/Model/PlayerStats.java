package com.example.demo.Model;

import java.util.Arrays;

public class PlayerStats {
    String puuid;
    String gameName;
    String tagLine;
    String championId;
    String championName;

    int summ1;
    int summ2;
    int kills;
    int deaths;
    int assists;

    boolean win;

    int items[];

    public PlayerStats(String puuid, String gameName, String tagLine, String championId, String championName, int deaths, int kills, int assists, boolean win) {
        this.puuid = puuid;
        this.gameName = gameName;
        this.championId = championId;
        this.tagLine = tagLine;
        this.championName = championName;
        this.deaths = deaths;
        this.kills = kills;
        this.assists = assists;
        this.win = win;
        items = new int[7];
    }
    public void setSumm1(int summ1){
        this.summ1 = summ1;
    }

    public void setSumm2(int summ2){
        this.summ2 = summ2;
    }

    public int getItem(int i){
        return items[i];
    }

    public void setItem(int item, int i){
        items[i] = item;
    }

    public String getPuuid() {
        return puuid;
    }

    public String getGameName() {
        return gameName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String getChampionId() {
        return championId;
    }

    public int getDeaths() {
        return deaths;
    }

    public String getChampionName() {
        return championName;
    }

    public int getKills() {
        return kills;
    }

    public boolean isWin() {
        return win;
    }

    public int[] getItems() {
        return items;
    }

    public int getAssists() {
        return assists;
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "puuid='" + puuid + '\'' +
                ", gameName='" + gameName + '\'' +
                ", tagLine='" + tagLine + '\'' +
                ", championId='" + championId + '\'' +
                ", championName='" + championName + '\'' +
                ", summ1=" + summ1 +
                ", summ2=" + summ2 +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", assists=" + assists +
                ", win=" + win +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}
