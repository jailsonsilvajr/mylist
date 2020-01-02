package com.jailson.mylist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jailson.mylist.R;
import com.jailson.mylist.firebase.FireConnection;
import com.jailson.mylist.object.User;
import com.jailson.mylist.service.Service;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private Button button_register;
    private ProgressBar progressBar_register;

    private Service service;

    private FirebaseAuth firebaseAuth;

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

                String name = textInputLayout_name.getEditText().getText().toString();
                String email = textInputLayout_email.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();

                new Register(name, email, password).execute();

                //createUser(email, password); // ---> FIREBASE <---
            }
        });
    }

    private void createUser(String email, String password){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Intent intent = new Intent(getApplicationContext(), ListsActivity.class);
                            intent.putExtra("id_user", firebaseAuth.getCurrentUser().getUid());
                            startActivity(intent);
                            finish();

                        }else{

                            Log.i("MYFIREBASE", "Register Fail");
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FireConnection.getFirebaseAuth();
    }

    private void init_views() {

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

    private class Register extends AsyncTask<String, Void, Boolean>{

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
        protected Boolean doInBackground(String... strings) {

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, String> dataObj = new HashMap<>();
                                dataObj.put("name", name);
                                db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                                        .set(dataObj);

                                SharedPreferences sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("id", firebaseAuth.getCurrentUser().getUid());
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), ListsActivity.class);
                                intent.putExtra("id_user", firebaseAuth.getCurrentUser().getUid());
                                startActivity(intent);
                                finish();

                            }else{

                                Toast.makeText(getApplicationContext(), "Register Fail", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            return true;
        }
    }
}
