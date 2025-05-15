package com.example.demo.Model;

public class RankedSummoner extends Summoner{
    String tier;
    String rank;
    int leaguePoints;


    public RankedSummoner(String tagLine, String gameName, String puuid, String id, String accountId, int level, String summonerIconId, String tier, String rank, int leaguePoints) {
        super(tagLine, gameName, puuid, id, accountId, level, summonerIconId);
        this.tier = tier;
        this.rank = rank;
        this.leaguePoints = leaguePoints;
    }

    public RankedSummoner(String tagLine, String gameName, String puuid, String id, String accountId, int level, String summonerIconId) {
        super(tagLine, gameName, puuid, id, accountId, level, summonerIconId);
    }

    public RankedSummoner(Player player,  String id, String accountId, int level, String summonerIconId) {
        super(player,  id, accountId, level, summonerIconId);
    }

    public String getRank() {
        return rank;
    }

    public String getTier() {
        return tier;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }
}
