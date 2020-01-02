package com.jailson.mylist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.object.List;
import com.jailson.mylist.service.Service;

import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_mark;
    private TextInputLayout textInputLayout_price;
    private ImageView imageview_item_add;
    private ImageView imageview_item_less;
    private TextView textview_item_qtd;
    private Button button_item_save;

    private List list;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.service = new Service();
        this.list = (List) getIntent().getSerializableExtra("list");

        init_views();
        click_button_save();
        add_less_qtd();
    }

    private void add_less_qtd() {

        this.imageview_item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtd = Integer.parseInt(String.valueOf(textview_item_qtd.getText()));
                qtd += 1;
                textview_item_qtd.setText(Integer.toString(qtd));
            }
        });

        this.imageview_item_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtd = Integer.parseInt(String.valueOf(textview_item_qtd.getText()));
                qtd -= 1;
                if(qtd < 1) qtd = 1;
                textview_item_qtd.setText(Integer.toString(qtd));
            }
        });
    }

    private void click_button_save() {

        this.button_item_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price = textInputLayout_price.getEditText().getText().toString();
                if(price.length() <= 0) price = "0";

                Item item = new Item("",
                        textInputLayout_name.getEditText().getText().toString(),
                        textInputLayout_mark.getEditText().getText().toString(),
                        Double.parseDouble(price),
                        Integer.parseInt(String.valueOf(textview_item_qtd.getText())),
                        list.getId(),
                        0,
                        "url_img");

                AddItem addItem = new AddItem(item);
                addItem.execute();
            }
        });
    }

    private void init_views() {

        this.textInputLayout_name = findViewById(R.id.edittext_item_name);
        this.textInputLayout_mark = findViewById(R.id.edittext_item_mark);
        this.textInputLayout_price = findViewById(R.id.edittext_item_price);
        this.imageview_item_add = findViewById(R.id.imageview_item_add);
        this.imageview_item_less = findViewById(R.id.imageview_item_less);
        this.textview_item_qtd = findViewById(R.id.textview_item_qtd);
        this.button_item_save = findViewById(R.id.button_item_save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class AddItem extends AsyncTask<Void, Void, Boolean>{

        private Item item;

        public AddItem(Item item){

            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, String> map = new HashMap<>();
            map.put("name", item.getName());
            map.put("mark", item.getMark());
            map.put("price", Double.toString(item.getPrice()));
            map.put("quantity", Integer.toString(item.getQtd()));
            map.put("id_list", item.getId_list());
            map.put("into_cart", Integer.toString(item.getInto_cart()));

            db.collection("items").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {

                    if(task.isSuccessful()){

                        setResult(RESULT_OK);
                        finish();
                    }else{

                        Toast.makeText(AddItemActivity.this, "Fail add", Toast.LENGTH_LONG).show();
                    }
                }
            });

            return null;
        }
    }
}
