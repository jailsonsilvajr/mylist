package com.jailson.mylist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.jailson.mylist.R;
import com.jailson.mylist.views.lists.ListsView;
import com.jailson.mylist.views.login.LoginView;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.progressBar_splash = findViewById(R.id.progressBar_splash);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarLogin();
            }
        }, 2000);
    }

    private void mostrarLogin() {

        SharedPreferences sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");

        if(id.equals("")) {

            Intent intent = new Intent(SplashActivity.this, LoginView.class);
            startActivity(intent);
            finish();
        }else{

            Intent intent = new Intent(SplashActivity.this, ListsView.class);
            intent.putExtra("id_user", id);
            startActivity(intent);
            finish();
        }
    }
}
