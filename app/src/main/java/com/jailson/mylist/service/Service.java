package com.jailson.mylist.service;

import android.widget.ProgressBar;

import com.jailson.mylist.http.Login;
import com.jailson.mylist.http.RegisterUser;
import com.jailson.mylist.object.User;

import java.util.concurrent.ExecutionException;

public class Service {

    private final String url = "http://192.168.0.109/mylist/";

    public User login(String email, String password, ProgressBar progressBar) throws ExecutionException, InterruptedException {

        Login login = new Login(this.url + "user/login.php", email, password, progressBar);
        return login.execute().get();
    }

    public User register_user(String name, String email, String password) throws ExecutionException, InterruptedException {

        RegisterUser registerUser = new RegisterUser(this.url + "user/register.php", name, email, password);
        return registerUser.execute().get();
    }
}
