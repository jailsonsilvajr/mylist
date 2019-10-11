package com.jailson.mylist.http;

import android.util.Log;

import com.google.gson.Gson;
import com.jailson.mylist.object.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Login{

    private String url;
    private String email;
    private String password;

    public Login(String url, String email, String password){

        this.url = url;
        this.email = email;
        this.password = password;
    }

    public User do_login(){

        try {

            URL link = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", this.email);
            jsonObject.put("password", this.password);

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject.toString());

            connection.connect();

            if(connection.getResponseCode() == 200){

                String userJsonStr;
                Scanner scanner = new Scanner(connection.getInputStream());
                userJsonStr = scanner.next();
                while(scanner.hasNext()){

                    userJsonStr += " ";
                    userJsonStr += scanner.next();
                }

                Gson gson = new Gson();
                User user = gson.fromJson(userJsonStr, User.class);

                return user;
            }

        } catch (MalformedURLException e) {

            Log.i("Login", e.getMessage());
        } catch (IOException e) {

            Log.i("Login", e.getMessage());
        } catch (JSONException e) {

            Log.i("Login", e.getMessage());
        }

        return null;
    }
}
