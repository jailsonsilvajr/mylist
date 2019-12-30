package com.jailson.mylist.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jailson.mylist.R;
import com.jailson.mylist.firebase.FireConnection;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private Button button_register;
    private Button button_enter;
    private ProgressBar progressBar_login;

    private FirebaseAuth firebaseAuth;

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

                String email = textInputLayout_email.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();

                new LoginActivity.Login(email, password).execute();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FireConnection.getFirebaseAuth();
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

        this.textInputLayout_email = findViewById(R.id.etLogin_email);
        this.textInputLayout_password = findViewById(R.id.etLogin_password);
        this.button_register = findViewById(R.id.btnLogin_register);
        this.button_enter = findViewById(R.id.btnLogin_enter);
        this.progressBar_login = findViewById(R.id.progressBar_login);
    }

    private class Login extends AsyncTask<String, Void, Boolean>{

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
        protected Boolean doInBackground(String... strings) {

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                SharedPreferences sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("id", firebaseAuth.getCurrentUser().getUid());
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, ListsActivity.class);
                                intent.putExtra("id_user", firebaseAuth.getCurrentUser().getUid());
                                startActivity(intent);
                                finish();
                            }else{

                                Toast.makeText(LoginActivity.this, "Login Fail!", Toast.LENGTH_LONG).show();
                            }
                            progressBar_login.setVisibility(View.GONE);
                        }
                    });
            return true;
        }
    }
}
