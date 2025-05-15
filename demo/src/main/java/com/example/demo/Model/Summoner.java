package com.example.demo.Model;

public class Summoner extends Player{
    String id;
    String accountId;
    int level;
    String summonerIconId;

    public Summoner(){

    }
    public Summoner(String tagLine, String gameName, String puuid, String id, String accountId, int level, String summonerIconId) {
        super(tagLine, gameName, puuid);
        this.id = id;
        this.accountId = accountId;
        this.level = level;
        this.summonerIconId = summonerIconId;
    }
    public Summoner(Player player,  String id, String accountId, int level, String summonerIconId){
        super(player);
        this.id = id;
        this.accountId = accountId;
        this.level = level;
        this.summonerIconId = summonerIconId;
    }

    public void setSummonerIconId(String id){
        this.summonerIconId = id;
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getLevel() {
        return level;
    }

    public String getSummonerIconId() {
        return summonerIconId;
    }
}
