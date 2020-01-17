package com.jailson.mylist.views.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.jailson.mylist.R;
import com.jailson.mylist.mvps.RegisterMVP;
import com.jailson.mylist.presenters.register.RegisterPresenter;
import com.jailson.mylist.views.lists.ListsView;

public class RegisterView extends AppCompatActivity implements RegisterMVP.RegisterView {

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private Button button_register;
    private ProgressBar progressBar_register;

    private RegisterMVP.RegisterPresenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.presenter = new RegisterPresenter(this);

        init_views();

        click_button_register();
    }

    @Override
    public void goViewLists(String id_user) {

        SharedPreferences sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id_user);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), ListsView.class);
        intent.putExtra("id_user", id_user);
        startActivity(intent);
        finish();
    }

    @Override
    public void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void init_views() {

        this.textInputLayout_name = findViewById(R.id.etRegister_name);
        this.textInputLayout_email = findViewById(R.id.etRegister_email);
        this.textInputLayout_password = findViewById(R.id.etRegister_password);
        this.button_register = findViewById(R.id.btnRegister_register);
        this.progressBar_register = findViewById(R.id.progressBar_register);
        this.progressBar_register.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void click_button_register() {

        this.button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = textInputLayout_name.getEditText().getText().toString();
                String email = textInputLayout_email.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();

                new Register(name, email, password, presenter, progressBar_register).execute();
            }
        });
    }

    private class Register extends AsyncTask<Void, Void, Void> {

        private String name;
        private String email;
        private String password;

        private RegisterMVP.RegisterPresenter presenter;
        private ProgressBar progressBar;

        public Register(String name, String email, String password, RegisterMVP.RegisterPresenter presenter, ProgressBar progressBar){

            this.name = name;
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

            this.presenter.doRegister(name, email, password);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            this.progressBar.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }
}
