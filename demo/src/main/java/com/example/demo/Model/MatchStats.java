package com.example.demo.Model;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;

public class MatchStats {
    public PlayerStats[] playerStats;
    private String matchType;
    private String matchId;
    public MatchStats(){
        this.playerStats = new PlayerStats[10];
    }

    public void setMatchType(String matchType){
        this.matchType = matchType;
    }
    public void setMatchID(String matchId){
        this.matchId = matchId;
    }
    public String getMatchId(){
        return matchId;
    }
    public String getMatchType(){
        return matchType;
    }
}
