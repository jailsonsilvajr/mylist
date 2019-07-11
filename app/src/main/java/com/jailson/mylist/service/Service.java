package com.jailson.mylist.service;

import com.jailson.mylist.http.Login;
import com.jailson.mylist.object.User;

import java.util.concurrent.ExecutionException;

public class Service {

    private final String url = "http://192.168.0.109/mylist/";

    public User login(String email, String password) throws ExecutionException, InterruptedException {

        Login login = new Login(this.url + "user/login.php", email, password);
        return login.execute().get();
    }
}
