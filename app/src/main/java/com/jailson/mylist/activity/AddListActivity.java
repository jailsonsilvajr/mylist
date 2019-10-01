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

public class AddListActivity extends AppCompatActivity {

    private EditText editText_add_list_name;
    private Button button_add_list;

    private Service service;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.service = new Service();
        this.user = (User) getIntent().getSerializableExtra("user");

        init_views();
        click_button_add();
    }

    private void click_button_add() {

        this.button_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if(service.addList(editText_add_list_name.getText().toString(), user.getId())){

                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                        finish();
                    }else{

                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
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

        this.editText_add_list_name = findViewById(R.id.editext_add_list_name);
        this.button_add_list = findViewById(R.id.button_add_list);
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
