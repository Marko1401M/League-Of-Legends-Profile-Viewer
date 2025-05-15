package com.example.demo;

import com.example.demo.DB.DataBase;
import com.example.demo.Model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins="http://localhost:5173")
@RestController
@RequestMapping("/t")
public class MainController {
        UserService userService;
        DataBase dataBase;
        int currentLeaderBoard;
        public MainController(JdbcClient jdbcClient, RiotApiService riotApiService){
            this.dataBase = new DataBase(jdbcClient, riotApiService);
            currentLeaderBoard = -1;
        }

        @PostMapping("/login/{username}/{password}")
        public String logIn(@PathVariable String username, @PathVariable String password) throws JsonProcessingException {
            if(dataBase.logIn(username, password)) {
                UserService.setUser(new User(username));
                System.out.println(UserService.getUser().getUsername());
                UserService.getUser().setPlayer(dataBase.getPlayer(username));

                return "{\"result\":\"OK\"}";
            }
            return "{\"result\":\"BAD\"}";
        }

        @GetMapping("logout")
        public String logOut(){
            UserService.setUser(null);
            return "{\"result\":\"OK\"}";
        }

        @GetMapping("/checkLogin")
        public User checkLogin(){
            return UserService.getUser();

        }

        @GetMapping("/checkLogin2")
        public String checkLogin2(){
            if(UserService.getUser() == null) return "{\"result\":\"BAD\"}";
            return "{\"result\":\"OK\"}";
        }

        @PostMapping("/register/{username}/{password}/{gameName}/{tagLine}")
        public String register(@PathVariable String username, @PathVariable String password, @PathVariable String gameName, @PathVariable String tagLine){
            if(dataBase.register(username,password,gameName,tagLine)) return "{\"result\":\"OK\"}";
            return "{\"result\":\"BAD\"}";
        }

        @GetMapping("/get_leaderboard")
        public List<Map<String, Object>> getLeaderBoard(){
            return dataBase.getFromLeaderboard(currentLeaderBoard);

        }

        @GetMapping("/get_leaderboards")
        public List<Map<String, Object>> getLeaderBoards(){
            return dataBase.getLeaderBoards();
        }

        @GetMapping("/create_leaderboard/{username}/{name}")
        public String createLeaderboard(@PathVariable String username, @PathVariable String name){
            dataBase.createLeaderboard(name,username);
            return "{\"result\":\"OK\"}";
        }

        @GetMapping("/join_leaderboard/{username}/{id}")
        public String joinLeaderBoard(@PathVariable String username, @PathVariable int id) throws JsonProcessingException {
            if(dataBase.joinLeaderboard(username,id)) return "{\"result\":\"OK\"}";
            return "{\"result\":\"BAD\"}";
        }

        @GetMapping("/set_leaderboard/{id}")
        public String joinLeaderBoard(@PathVariable int id){
            currentLeaderBoard = id;
            return "{\"result\":\"OK\"}";
        }

        @GetMapping("/lol/change_details/{gameName}/{tagLine}")
        public String changeLoLDetails(@PathVariable String gameName, @PathVariable String tagLine){
            dataBase.changeLoLDetails(gameName, tagLine, UserService.getUser());
            UserService.getUser().getPlayer().setTagLine(tagLine);
            UserService.getUser().getPlayer().setGameName(gameName);
            return "{\"result\":\"OK\"}";
        }
}
