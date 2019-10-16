package com.jailson.mylist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.jailson.mylist.R;
import com.jailson.mylist.object.User;

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
        int id = sharedPreferences.getInt("id", -1);

        if(id == -1) {

            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{

            sharedPreferences = getSharedPreferences("name", Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("name", "");
            sharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
            String email = sharedPreferences.getString("email", "");

            User user = new User(id, name, email, "");

            Intent intent = new Intent(SplashActivity.this, ListsActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }
    }
}
