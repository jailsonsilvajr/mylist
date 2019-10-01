package com.jailson.mylist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jailson.mylist.R;
import com.jailson.mylist.object.User;
import com.jailson.mylist.service.Service;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    private EditText editext_name;
    private EditText editext_email;
    private EditText editext_password;
    private Button button_register;

    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.service = new Service();

        init_views();
        click_button_register();
    }

    private void click_button_register() {

        this.button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editext_name.getText().toString();
                String email = editext_email.getText().toString();
                String password = editext_password.getText().toString();

                try {

                    User user = service.register_user(name, email, password);
                    if(user != null){

                        Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_LONG).show();
                        finish();
                    }else{

                        Toast.makeText(RegisterActivity.this, "Fail", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init_views() {

        this.editext_name = findViewById(R.id.etRegister_name);
        this.editext_email = findViewById(R.id.etRegister_email);
        this.editext_password = findViewById(R.id.etRegister_password);
        this.button_register = findViewById(R.id.btnRegister_register);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
