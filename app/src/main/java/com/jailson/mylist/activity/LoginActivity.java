package com.jailson.mylist.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.jailson.mylist.R;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.progressBar = findViewById(R.id.pbLogin_enter);
        this.progressBar.setVisibility(View.GONE);
    }
}
