package com.jailson.mylist.views.login;

import com.jailson.mylist.mvps.LoginMVP;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.jailson.mylist.R;
import com.jailson.mylist.presenters.login.LoginPresenter;
import com.jailson.mylist.views.lists.ListsView;
import com.jailson.mylist.views.register.RegisterView;

public class LoginView extends AppCompatActivity implements LoginMVP.LoginView {

    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private Button button_register;
    private Button button_enter;
    private ProgressBar progressBar_login;

    private LoginMVP.LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.presenter = new LoginPresenter(this);

        init_views();

        click_button_register();
        click_button_enter();
    }

    @Override
    public void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void doLogin(String id_user) {

        SharedPreferences sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id_user);
        editor.apply();

        Intent intent = new Intent(LoginView.this, ListsView.class);
        intent.putExtra("id_user", id_user);
        startActivity(intent);
        finish();
    }

    private void click_button_enter() {

        this.button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = textInputLayout_email.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();

                new LoginView.Login(email, password,  presenter, progressBar_login).execute();
            }
        });
    }

    private void click_button_register() {

        this.button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginView.this, RegisterView.class);
                startActivity(intent);
            }
        });
    }

    private void init_views() {

        this.textInputLayout_email = findViewById(R.id.etLogin_email);
        this.textInputLayout_password = findViewById(R.id.etLogin_password);
        this.button_register = findViewById(R.id.btnLogin_register);
        this.button_enter = findViewById(R.id.btnLogin_enter);
        this.progressBar_login = findViewById(R.id.progressBar_login);
        this.progressBar_login.setVisibility(View.GONE);
    }

    private class Login extends AsyncTask<Void, Void, Void>{

        private String email;
        private String password;
        private LoginMVP.LoginPresenter presenter;
        private ProgressBar progressBar;

        public Login(String email, String password, LoginMVP.LoginPresenter presenter, ProgressBar progressBar){

            this.email = email;
            this.password = password;
            this.presenter = presenter;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {

            this.progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            this.presenter.doLogin(this.email, this.password);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            this.progressBar.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }
}