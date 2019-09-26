package com.jailson.mylist.http;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jailson.mylist.object.Item;

import org.json.JSONObject;

import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GetItems extends AsyncTask <Void, Void, List<Item> > {

    private String url;
    private int id_list;

    public GetItems(String url, int id_list){

        this.url = url;
        this.id_list = id_list;
    }

    @Override
    protected List<Item> doInBackground(Void... voids) {

        try {

            URL link = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_list", this.id_list);

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject.toString());

            connection.connect();

            if(connection.getResponseCode() == 200){

                Scanner scanner = new Scanner(connection.getInputStream());
                String itemJsonStr = scanner.next();
                while(scanner.hasNext()){

                    itemJsonStr += " ";
                    itemJsonStr += scanner.next();
                }
                Gson gson = new Gson();
                Item[] items = gson.fromJson(itemJsonStr, Item[].class);
                return new ArrayList<>(Arrays.asList(items));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
