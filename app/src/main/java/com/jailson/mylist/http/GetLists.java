package com.jailson.mylist.http;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GetLists extends AsyncTask<Void, Void, List<com.jailson.mylist.object.List> > {

    private String url;
    private int id_user;

    public GetLists(String url, int id_user){

        this.url = url;
        this.id_user = id_user;
    }

    @Override
    protected List<com.jailson.mylist.object.List> doInBackground(Void... voids) {

        try {

            URL link = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_user", this.id_user);

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(jsonObject.toString());

            connection.connect();

            if(connection.getResponseCode() == 200){

                Scanner scanner = new Scanner(connection.getInputStream());
                String listJsonStr = scanner.next();
                while(scanner.hasNext()){

                    listJsonStr += " ";
                    listJsonStr += scanner.next();
                }

                Gson gson = new Gson();
                com.jailson.mylist.object.List[] lists = gson.fromJson(listJsonStr, com.jailson.mylist.object.List[].class);
                return new ArrayList<>(Arrays.asList(lists));
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
