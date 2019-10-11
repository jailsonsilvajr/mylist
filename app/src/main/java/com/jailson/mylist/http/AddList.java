package com.jailson.mylist.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddList {

    private String url;
    private String name;
    private int id_user;

    public AddList(String url, String name, int id_user){

        this.url = url;
        this.name = name;
        this.id_user = id_user;
    }

    public boolean do_addList(){

        try {

            URL link = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();

            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", this.name);
            jsonObject.put("id_user", this.id_user);

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject.toString());

            connection.connect();

            if(connection.getResponseCode() == 200) return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
