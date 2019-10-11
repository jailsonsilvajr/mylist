package com.jailson.mylist.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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

    private EditText editText_email;
    private EditText editText_password;
    private Button button_register;
    private Button button_enter;
    private ProgressBar progressBar_login;

    private final Service service = new Service();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init_views();
        progressBar_login.setVisibility(View.GONE);

        click_button_register();
        click_button_enter();
    }

    private void click_button_enter() {

        this.button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();

                new LoginActivity.Login(email, password).execute();
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

        this.editText_email = findViewById(R.id.etLogin_email);
        this.editText_password = findViewById(R.id.etLogin_password);
        this.button_register = findViewById(R.id.btnLogin_register);
        this.button_enter = findViewById(R.id.btnLogin_enter);
        this.progressBar_login = findViewById(R.id.progressBar_login);
    }

    private class Login extends AsyncTask<String, Void, User>{

        private String email;
        private String password;

        public Login(String email, String password){

            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {

            progressBar_login.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(String... strings) {

            return service.login(this.email, this.password);
        }

        @Override
        protected void onPostExecute(User user) {

            progressBar_login.setVisibility(View.GONE);
            if(user != null){

                Intent intent = new Intent(LoginActivity.this, ListsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }else{

                Toast.makeText(LoginActivity.this, "Login Fail!", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(user);
        }
    }
}
