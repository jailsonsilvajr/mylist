package com.jailson.mylist.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jailson.mylist.R;
import com.jailson.mylist.object.User;
import com.jailson.mylist.service.Service;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText editText_email;
    private EditText editText_password;
    private Button button_register;
    private Button button_enter;

    private final Service service = new Service();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init_views();
        this.progressBar.setVisibility(View.GONE);

        click_button_register();
        click_button_enter();
    }

    private void click_button_enter() {

        this.button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();
                try {

                    User user = service.login(email, password, progressBar);
                    if(user != null){

                        Intent intent = new Intent(LoginActivity.this, ListsActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }else{

                        Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {

                    Log.i("Login: ", e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {

                    Log.i("Login: ", e.getMessage());
                }
            }
        });
    }

    private void click_button_register() {

        this.button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init_views() {

        this.progressBar = findViewById(R.id.pbLogin_enter);
        this.editText_email = findViewById(R.id.etLogin_email);
        this.editText_password = findViewById(R.id.etLogin_password);
        this.button_register = findViewById(R.id.btnLogin_register);
        this.button_enter = findViewById(R.id.btnLogin_enter);
    }
}
