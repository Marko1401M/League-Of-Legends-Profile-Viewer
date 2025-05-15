package com.example.demo.Model;

public class User {
    String username;
    private Player player;
    private Summoner summoner;
    private RankedSummoner rankedSummoner;
    public User(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public Player getPlayer(){
        return player;
    }

    public RankedSummoner getRankedSummoner() {
        return rankedSummoner;
    }

    public Summoner getSummoner() {
        return summoner;
    }
    public void setPlayer(Player player){
        this.player = player;
    }
    public void setSummoner(Summoner summoner){
        this.summoner = summoner;
    }
    public void setRankedSummoner(RankedSummoner rankedSummoner){
        this.rankedSummoner = rankedSummoner;
    }
}
