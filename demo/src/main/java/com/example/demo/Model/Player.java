package com.example.demo.Model;

public class Player {
    private String gameName;
    private String tagLine;
    private String puuid;

    public Player(){

    }

    public Player(String tagLine, String gameName, String puuid) {
        this.tagLine = tagLine;
        this.gameName = gameName;
        this.puuid = puuid;
    }

    public Player(Player player){
        this.gameName = player.getGameName();
        this.tagLine = player.getTagLine();
        this.puuid = player.getPuuid();
    }

    public String getGameName() {
        return gameName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String getPuuid() {
        return puuid;
    }
    public void setGameName(String gameName){
        this.gameName = gameName;
    }
    public void setTagLine(String tagLine){
        this.tagLine = tagLine;
    }
}
