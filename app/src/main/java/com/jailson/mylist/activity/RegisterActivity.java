package com.jailson.mylist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.jailson.mylist.R;
import com.jailson.mylist.object.User;
import com.jailson.mylist.service.Service;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private Button button_register;
    private ProgressBar progressBar_register;

    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.service = new Service();

        init_views();
        this.progressBar_register.setVisibility(View.GONE);

        click_button_register();
    }

    private void click_button_register() {

        this.button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String name = editext_name.getText().toString();
                String name = textInputLayout_name.getEditText().getText().toString();
                String email = textInputLayout_email.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();

                new Register(name, email, password).execute();
            }
        });
    }

    private void init_views() {

        //this.editext_name = findViewById(R.id.etRegister_name);
        this.textInputLayout_name = findViewById(R.id.etRegister_name);
        this.textInputLayout_email = findViewById(R.id.etRegister_email);
        this.textInputLayout_password = findViewById(R.id.etRegister_password);
        this.button_register = findViewById(R.id.btnRegister_register);
        this.progressBar_register = findViewById(R.id.progressBar_register);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class Register extends AsyncTask<String, Void, User>{

        private String name;
        private String email;
        private String password;

        public Register(String name, String email, String password){

            this.name = name;
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {

            progressBar_register.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(String... strings) {

            return service.register_user(this.name, this.email, this.password);
        }

        @Override
        protected void onPostExecute(User user) {

            progressBar_register.setVisibility(View.GONE);
            if(user != null){

                Intent intent = new Intent(RegisterActivity.this, ListsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }else{

                Toast.makeText(RegisterActivity.this, "Register Fail!", Toast.LENGTH_LONG);
            }
            super.onPostExecute(user);
        }
    }
}
