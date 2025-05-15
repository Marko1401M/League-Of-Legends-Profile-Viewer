package com.example.demo;

import com.example.demo.Model.MatchStats;
import com.example.demo.Model.Player;
import com.example.demo.Model.PlayerStats;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.MathContext;

@Service
public class RiotApiService {
    @Value("${riot.api.key}")
    private String apiKey;
    Player player;
    private final RestTemplate restTemplate;


    public RiotApiService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    public String getPlayer(String gameName, String tagLine){
        String url = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"+gameName+"/" + tagLine + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }

    public String getSummonerByPuuid(String puuid){
        String url = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=" + apiKey;

        return restTemplate.getForObject(url,String.class);
    }

    public String getSummoner(String gameName, String tagLine) throws JsonProcessingException {
        String temp = getPlayer(gameName, tagLine);
        ObjectMapper om = new ObjectMapper();
        JsonNode rootNode = om.readTree(temp);

        String url = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + rootNode.get("puuid").asText() + "?api_key=" + apiKey;

        return restTemplate.getForObject(url,String.class);
    }
    public String getRankedSummonerById(String id){
        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + id + "?api_key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }
    public String getRankedSummoner(String gameName, String tagLine) throws JsonProcessingException {
        String temp = getSummoner(gameName, tagLine);
        ObjectMapper om = new ObjectMapper();
        JsonNode rootNode = om.readTree(temp);

        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + rootNode.get("id").asText() + "?api_key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }

    public String getMatchIds(String puuid){

        String url = UriComponentsBuilder
                .fromHttpUrl("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid + "/ids")
                .queryParam("api_key",apiKey)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    public String getMatch(String matchId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://europe.api.riotgames.com/lol/match/v5/matches/" + matchId)
                .encode()
                .toUriString();


        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", apiKey); // Add the API key header
        headers.add("Accept","*/*");

        HttpEntity<String> entity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody(); // Return the response body
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


    public MatchStats[] getMatchHistory(String puuid) throws JsonProcessingException {
        String matchIDs = getMatchIds(puuid);

        ObjectMapper om = new ObjectMapper();
        JsonNode rootNode1 = om.readTree(matchIDs);
        MatchStats[] matchStats = new MatchStats[7];
        for(int i = 0 ; i < 7; i++){
            String match = getMatch(rootNode1.get(i).asText());
            JsonNode rootNode = om.readTree(match);
            PlayerStats[] playerStats = new PlayerStats[10];
            matchStats[i] = new MatchStats();
            matchStats[i].setMatchID(rootNode1.get(i).asText());
            System.out.println(rootNode);
            for(int j = 0; j < 10; j++){
                if(rootNode.get("info").get("participants").get(j) == null) break;
                if(!rootNode.get("info").get("gameType").asText().equals("MATCHED_GAME")) {
                    matchStats[i] = null;
                    break;
                }
                String champName = rootNode.get("info").get("participants").get(j).get("championName").asText();
                String champId = rootNode.get("info").get("participants").get(j).get("championId").asText();
                String gameName = rootNode.get("info").get("participants").get(j).get("riotIdGameName").asText();
                String tagLine = rootNode.get("info").get("participants").get(j).get("riotIdTagline").asText();
                String puuid1 = rootNode.get("info").get("participants").get(j).get("puuid").asText();

                boolean win = rootNode.get("info").get("participants").get(j).get("win").asBoolean();
                int kills = rootNode.get("info").get("participants").get(j).get("kills").asInt();
                int deaths = rootNode.get("info").get("participants").get(j).get("deaths").asInt();
                int assists = rootNode.get("info").get("participants").get(j).get("assists").asInt();

                playerStats[j]  = new PlayerStats(puuid1,gameName,tagLine,champId,champName,deaths,kills,assists,win);

                for(int k = 0; k < 7; k++){
                    playerStats[j].setItem(rootNode.get("info").get("participants").get(j).get("item" + k).asInt(), k);
                }
                System.out.println(champId + " " + i);
                matchStats[i].playerStats[j] = playerStats[j];
                matchStats[i].setMatchType(rootNode.get("info").get("gameMode").asText());
            }


        }
        return matchStats;
    }

}
