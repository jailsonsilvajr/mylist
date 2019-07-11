package com.jailson.mylist.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    private void clickBtnRegister(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.register_popup);

        ImageView imgRegister_close = dialog.findViewById(R.id.imgRegister_close);
        final EditText etRegister_name = dialog.findViewById(R.id.etRegister_name);
        final EditText etRegister_email = dialog.findViewById(R.id.etRegister_email);
        final EditText etRegister_password = dialog.findViewById(R.id.etRegister_password);
        Button btnRegister_register = dialog.findViewById(R.id.btnRegister_register);

        imgRegister_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnRegister_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etRegister_name.getText().toString();
                String email = etRegister_email.getText().toString();
                String password = etRegister_password.getText().toString();

                try {

                    User user = service.register_user(name, email, password);
                    if(user != null){

                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }else{

                        Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private void clickBtnEnter(){

        String email = this.editText_email.getText().toString();
        String password = this.editText_password.getText().toString();
        try {

            User user = this.service.login(email, password, this.progressBar);
            if(user != null){

                Intent intent = new Intent(this, ListsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }else{

                Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
            }
        } catch (ExecutionException e) {

            Log.i("Login: ", e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {

            Log.i("Login: ", e.getMessage());
        }
    }
}
