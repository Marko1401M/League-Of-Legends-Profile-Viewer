package com.example.demo;

import com.example.demo.Model.MatchStats;
import com.example.demo.Model.Player;
import com.example.demo.Model.RankedSummoner;
import com.example.demo.Model.Summoner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:5173")
@RestController
@RequestMapping("/t")
public class RiotApiController {

    private UserService userService;
    private final RiotApiService riotApiService;
    public Player player;
    public Summoner summoner;
    public RankedSummoner rankedSummoner;
    public String matchID;
    public RiotApiController(RiotApiService riotApiService){
        this.riotApiService = riotApiService;
    }

    @GetMapping("lol/account/{gameName}/{tagLine}")
    public Player getPlayer(@PathVariable String gameName, @PathVariable String tagLine) throws JsonProcessingException {
        String temp = riotApiService.getPlayer(gameName,tagLine);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(temp);

        player = new Player(rootNode.get("tagLine").asText(), rootNode.get("gameName").asText(), rootNode.get("puuid").asText());

        return player;
    }

    @GetMapping("lol/get_account")
    public Player getPlayer(){

        System.out.println(player.getGameName());
        return player;
    }

    @GetMapping("lol/summoner/{gameName}/{tagLine}")
    public Summoner getSummoner(@PathVariable String gameName, @PathVariable String tagLine) throws JsonProcessingException {
        String temp = riotApiService.getSummoner(gameName, tagLine);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(temp);
        System.out.println(rootNode);
        summoner = new Summoner(tagLine, gameName, rootNode.get("puuid").asText(), rootNode.get("id").asText(), rootNode.get("accountId").asText(), rootNode.get("summonerLevel").asInt(), rootNode.get("profileIconId").asText());

        return summoner;
    }

    @GetMapping("lol/get_summoner")
    public Summoner getSummoner(){
        return summoner;
    }

    @GetMapping("lol/ranked_summoner/{gameName}/{tagLine}")
    public RankedSummoner getRankedSummoner(@PathVariable String gameName, @PathVariable String tagLine) throws JsonProcessingException {

        String temp0 = riotApiService.getPlayer(gameName, tagLine);

        ObjectMapper objectMapper0 = new ObjectMapper();
        JsonNode rootNode0 = objectMapper0.readTree(temp0);
        player = new Player(rootNode0.get("tagLine").asText(), rootNode0.get("gameName").asText(), rootNode0.get("puuid").asText());


        String temp1 = riotApiService.getSummonerByPuuid(player.getPuuid());

        ObjectMapper objectMapper2 = new ObjectMapper();
        JsonNode rootNode2 = objectMapper2.readTree(temp1);


        summoner = new Summoner(player, rootNode2.get("id").asText(), rootNode2.get("accountId").asText(), rootNode2.get("summonerLevel").asInt(), rootNode2.get("profileIconId").asText());
        String temp = riotApiService.getRankedSummonerById(rootNode2.get("id").asText());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(temp);



        for(int i = 0; i < 3; i++){
            if(rootNode.get(i) != null){
                if(rootNode.get(i).get("queueType").asText().equals("RANKED_SOLO_5x5")){
                    rankedSummoner = new RankedSummoner(tagLine, gameName, player.getPuuid(), summoner.getId(), summoner.getAccountId(), summoner.getLevel(), summoner.getSummonerIconId(), rootNode.get(i).get("tier").asText(),rootNode.get(i).get("rank").asText(),rootNode.get(i).get("leaguePoints").asInt());
                    return rankedSummoner;
                }
            }
        }

        rankedSummoner = new RankedSummoner(tagLine, gameName, player.getPuuid(), summoner.getId(), summoner.getAccountId(), summoner.getLevel(), summoner.getSummonerIconId(), "UNRANKED", "?", 0);

        return rankedSummoner;
    }

    @GetMapping("lol/get_ranked_summoner")
    public RankedSummoner getRankedSummoner() throws JsonProcessingException {
        //riotApiService.getMatchHistory(rankedSummoner.getPuuid());
        return rankedSummoner;
    }

    @GetMapping("lol/get_match_history/{puuid}")
    public MatchStats[] getMatchStats(@PathVariable String puuid) throws JsonProcessingException {
        return riotApiService.getMatchHistory(puuid);
    }
    public void setPlayerGNTL(String gameName, String tagLine){
        this.player.setGameName(gameName);
        this.player.setTagLine(tagLine);
    }
    @GetMapping("lol/set_match_id/{id}")
    public void setMatchId(@PathVariable String id){
        matchID = id;
    }

    @GetMapping("lol/get_match_details")
    public String getMatchDetails() throws JsonProcessingException {
        String temp = riotApiService.getMatch(matchID);
        return temp; //Will be parsed in JS
    }
}
