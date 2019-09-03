package com.jailson.mylist.http;

import android.os.AsyncTask;

import com.jailson.mylist.object.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddItem extends AsyncTask<Void, Void, Boolean> {

    private String url;
    private Item item;

    public AddItem(String url, Item item){

        this.url = url;
        this.item = item;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {

            URL link = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", item.getName());
            jsonObject.put("mark", item.getMark());
            jsonObject.put("price", item.getPrice());
            jsonObject.put("quantity", item.getQtd());
            jsonObject.put("id_list", item.getId_list());

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
