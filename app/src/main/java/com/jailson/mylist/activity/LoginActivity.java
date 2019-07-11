package com.jailson.mylist.activity;

import android.support.v7.app.AppCompatActivity;
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

    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.service = new Service();

        this.progressBar = findViewById(R.id.pbLogin_enter);
        this.progressBar.setVisibility(View.GONE);

        this.editText_email = findViewById(R.id.etLogin_email);
        this.editText_password = findViewById(R.id.etLogin_password);
        this.button_register = findViewById(R.id.btnLogin_register);
        this.button_enter = findViewById(R.id.btnLogin_enter);

        this.button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickBtnRegister();
            }
        });

        this.button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickBtnEnter();
            }
        });
    }

    private void clickBtnRegister(){}

    private void clickBtnEnter(){

        this.progressBar.setVisibility(View.VISIBLE);

        String email = this.editText_email.getText().toString();
        String password = this.editText_password.getText().toString();
        try {

            User user = this.service.login(email, password);
            if(user != null){

                Toast.makeText(this, user.getName(), Toast.LENGTH_LONG).show();
            }else{

                Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
            }
            this.progressBar.setVisibility(View.GONE);
        } catch (ExecutionException e) {

            Log.i("Login: ", e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {

            Log.i("Login: ", e.getMessage());
        }
    }
}
