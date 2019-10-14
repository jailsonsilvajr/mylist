package com.jailson.mylist.http;

import com.jailson.mylist.object.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateItem {

    private String url;
    private Item item;

    public UpdateItem (String url, Item item){

        this.url = url;
        this.item = item;
    }

    public boolean do_update(){

        try {

            URL link = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", this.item.getId());
            jsonObject.put("name", this.item.getName());
            jsonObject.put("mark", this.item.getMark());
            jsonObject.put("price", this.item.getPrice());
            jsonObject.put("quantity", this.item.getQtd());
            jsonObject.put("id_list", this.item.getId_list());

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject.toString());

            connection.connect();

            if(connection.getResponseCode() == 200){

                return true;
            }
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
