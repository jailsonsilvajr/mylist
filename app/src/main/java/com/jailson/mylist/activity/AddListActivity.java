package com.jailson.mylist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jailson.mylist.R;

import java.util.HashMap;
import java.util.Map;

public class AddListActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout_name;
    private Button button_add_list;

    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.id_user = getIntent().getStringExtra("id_user");

        init_views();
        click_button_add();
    }

    private void click_button_add() {

        this.button_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AddList(textInputLayout_name.getEditText().getText().toString(), id_user).execute();
            }
        });
    }

    private void init_views() {

        this.textInputLayout_name = findViewById(R.id.editext_add_list_name);
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

    private class AddList extends AsyncTask<Void, Void, Boolean> {

        private String name;
        private String id_user;

        public AddList(String name, String id_user){

            this.name = name;
            this.id_user = id_user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, String> dataObj = new HashMap<>();
            dataObj.put("name", this.name);
            dataObj.put("id_user", this.id_user);
            db.collection("lists").add(dataObj).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {

                    if(task.isSuccessful()){

                        setResult(RESULT_OK);
                        finish();
                    }else{

                        Toast.makeText(AddListActivity.this, "Fail Add", Toast.LENGTH_LONG).show();
                    }
                }
            });

            return null;
        }
    }
}
