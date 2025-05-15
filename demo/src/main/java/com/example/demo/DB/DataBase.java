package com.example.demo.DB;

import com.example.demo.Model.Player;
import com.example.demo.Model.User;
import com.example.demo.RiotApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;


public class DataBase {
    private final JdbcClient jdbcClient;
    private final RiotApiService riotApiService;
    public DataBase(JdbcClient jdbcClient, RiotApiService riotApiService){
        this.jdbcClient = jdbcClient;
        this.riotApiService = riotApiService;
    }

    public boolean logIn(String username, String password){
        String passwordSHA = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        System.out.println(username + ':' + password);
        System.out.println(passwordSHA);
        String sql = "SELECT * from user where username = '" + username + "' and password ='" + passwordSHA + "'";
        return !jdbcClient.sql(sql).query().listOfRows().isEmpty();
    }
    public Player getPlayer(String username) throws JsonProcessingException {
        String sql = "SELECT * from user where username ='" + username + "'";
        JdbcClient.ResultQuerySpec stmt = this.jdbcClient.sql(sql).query();
        Player player;
        for(var a : stmt.listOfRows()){
            String temp = riotApiService.getPlayer(a.get("gameName").toString(), a.get("tagLine").toString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(temp);
            player = new Player(root.get("tagLine").asText(), root.get("gameName").asText(), root.get("puuid").asText());
            return player;
        }
        return null;
    }
    public boolean register(String username, String password, String gameName, String tagLine){
        if(!logIn(username, password)){
            String passwordSHA = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            String sql = "INSERT into user(username, password, gameName,tagLine) VALUES('" + username +"','" + passwordSHA +"','"+ gameName +"','" + tagLine + "')";
            jdbcClient.sql(sql).update();
            return true;
        }
        return false;
    }
    public List<Map<String, Object>> getLeaderBoards(){
        String sql = "SELECT * FROM leaderboard";
        return jdbcClient.sql(sql).query().listOfRows();
    }
    public List<Map<String, Object>> getFromLeaderboard(int id){
        String sql = "SELECT * from leaderboards where leaderboard_id = '" + id + "'";
        List<Map<String, Object>> temp = jdbcClient.sql(sql).query().listOfRows();
        for(int i = 0; i < temp.size(); i++){
            temp.get(i).put("totalPoints",getTotalPoints(temp.get(i).get("tier").toString(),temp.get(i).get("rank").toString(),Integer.parseInt(temp.get(i).get("leaguePoints").toString())));
        }
        for(int i = 0; i < temp.size(); i++){
            for(int j = 0; j < temp.size(); j++){
                if(Integer.parseInt(temp.get(i).get("totalPoints").toString()) > Integer.parseInt(temp.get(j).get("totalPoints").toString())){
                    Map<String, Object> temp2 = temp.get(i);
                    temp.set(i,temp.get(j));
                    temp.set(j,temp2);
                }
            }
        }
        return temp;
    }
    public void createLeaderboard(String name, String creator){
        String sql = "Insert into leaderboard(name, createdBy) VALUES('" + name + "','" + creator + "')";
        jdbcClient.sql(sql).update();
    }
    public boolean joinLeaderboard(String username, int leaderboardId) throws JsonProcessingException {
        String sql = "SELECT * from leaderboards where leaderboard_id ='" + leaderboardId + "' and username = '" + username + "'";
        if(!jdbcClient.sql(sql).query().listOfRows().isEmpty()) return false;

        Player temp = getPlayer(username);
        String temp2 = riotApiService.getSummoner(temp.getGameName(), temp.getTagLine());
        String temp3 = riotApiService.getRankedSummoner(temp.getGameName(), temp.getTagLine());
        System.out.println(temp);
        System.out.println(temp2);
        System.out.println(temp3);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(temp2);

        String profileIconId = rootNode.get("profileIconId").asText();
        String tier = "UNRANKED";
        String rank =  "?";
        int leaguePoints = 0;
        JsonNode rootNode2 = objectMapper.readTree(temp3);
        for(int i = 0; i < 3; i++){
            if(rootNode2.get(i) != null){
                if(rootNode2.get(i).get("queueType").asText().equals("RANKED_SOLO_5x5")){
                    tier = rootNode2.get(i).get("tier").asText();
                    rank = rootNode2.get(i).get("rank").asText();
                    leaguePoints = rootNode2.get(i).get("leaguePoints").asInt();
                }
            }
        }



        sql = "INSERT into leaderboards(username, tier, `rank`, leaguePoints, totalPoints, gameName, tagLine, leaderboard_id, summonerIconId) ";
        sql += "VALUES('" + username + "','" + tier + "','" + rank + "'," + leaguePoints + "," + getTotalPoints(tier,rank,leaguePoints) + ",'" + temp.getGameName() + "','" + temp.getTagLine() + "','" + leaderboardId + "','" + profileIconId + "')";

        jdbcClient.sql(sql).update();

        return true;
    }
    private int getTotalPoints(String tier, String rank, int leaguePoints){
        int points = leaguePoints;
        if(tier.equals("UNRANKED")) return 0;
        if(tier.equals("IRON")) points += 0;
        else if(tier.equals("BRONZE")) points += 400;
        else if(tier.equals("SILVER")) points += 800;
        else if(tier.equals("GOLD")) points += 1200;
        else if(tier.equals("PLATINUM")) points += 1600;
        else if(tier.equals("EMERALD")) points += 2000;
        else if(tier.equals("DIAMOND")) points += 2400;
        else  points += 2800;

        if(rank.equals("I") ) points += 300;
        if(rank.equals("II")) points += 200;
        if(rank.equals("III")) points += 100;

        return points;
    }
    public boolean changeLoLDetails(String gameName, String tagLine, User user){
        String sql = "UPDATE user set gameName = '" + gameName + "', tagLine='" + tagLine + "' where username='" + user.getUsername() + "'";
        this.jdbcClient.sql(sql).update();
        return true;
    }
    public void updateLeaderboards(){

    }
}
