package com.example.demo;

import com.example.demo.Model.User;
import org.springframework.stereotype.Service;

public class UserService {
    private static User user = null;

    public UserService(){

    }

    public static User getUser(){
        return user;
    }

    public static void setUser(User user1){
        user = user1;
    }

}
