package com.jailson.mylist.http;

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

public class RegisterUser {

    private String url;
    private String name;
    private String email;
    private String password;

    public RegisterUser(String url, String name, String email, String password){

        this.url = url;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User do_register(){

        try {

            URL link = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", this.name);
            jsonObject.put("email", this.email);
            jsonObject.put("password", this.password);

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject.toString());

            connection.connect();

            if(connection.getResponseCode() == 200){

                Scanner scanner = new Scanner(connection.getInputStream());
                String userJsonStr = scanner.next();
                while(scanner.hasNext()){

                    userJsonStr += " ";
                    userJsonStr += scanner.next();
                }

                Gson gson = new Gson();
                User user = gson.fromJson(userJsonStr, User.class);
                return user;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
