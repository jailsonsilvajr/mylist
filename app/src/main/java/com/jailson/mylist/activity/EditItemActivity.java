package com.jailson.mylist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.service.Service;

import java.util.concurrent.ExecutionException;

public class EditItemActivity extends AppCompatActivity {

    private ImageView imageview_item_load;
    private TextView textview_item_name_img;
    private EditText editext_item_name;
    private EditText editext_item_mark;
    private EditText editext_item_price;
    private ImageView imageview_item_add;
    private ImageView imageview_item_less;
    private TextView textview_item_qtd;
    private Button button_item_save;
    private Button button_item_delete;

    private Item item;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.item = (Item) getIntent().getSerializableExtra("item");
        this.service = new Service();

        init_views();
        set_views();
        events_click_button();
    }

    private void events_click_button() {

        this.imageview_item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(String.valueOf(textview_item_qtd.getText()));
                quantity += 1;
                textview_item_qtd.setText(Integer.toString(quantity));
            }
        });

        this.imageview_item_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(String.valueOf(textview_item_qtd.getText()));
                quantity -= 1;
                if(quantity < 1) quantity = 1;
                textview_item_qtd.setText(Integer.toString(quantity));
            }
        });

        this.button_item_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Item item_temp = new Item(item.getId(),
                        String.valueOf(editext_item_name.getText()),
                        String.valueOf(editext_item_mark.getText()),
                        Double.parseDouble(String.valueOf(editext_item_price.getText())),
                        Integer.parseInt(String.valueOf(textview_item_qtd.getText())),
                        item.getId_list(),
                        item.getUrl_img());

                UpdateItem updateItem = new UpdateItem(item_temp);
                updateItem.execute();
            }
        });

        this.button_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteItem deleteItem = new DeleteItem(item);
                deleteItem.execute();
            }
        });
    }

    private void set_views() {

        this.editext_item_name.setText(this.item.getName());
        this.editext_item_mark.setText(this.item.getMark());
        this.editext_item_price.setText(Double.toString(this.item.getPrice()));
        this.textview_item_qtd.setText(Integer.toString(this.item.getQtd()));
    }

    private void init_views() {

        this.imageview_item_load = findViewById(R.id.imageview_item_load);
        this.textview_item_name_img = findViewById(R.id.textview_item_name_img);
        this.editext_item_name = findViewById(R.id.edittext_item_name);
        this.editext_item_mark = findViewById(R.id.edittext_item_mark);
        this.editext_item_price = findViewById(R.id.edittext_item_price);
        this.imageview_item_add = findViewById(R.id.imageview_item_add);
        this.imageview_item_less= findViewById(R.id.imageview_item_less);
        this.textview_item_qtd = findViewById(R.id.textview_item_qtd);
        this.button_item_save = findViewById(R.id.button_item_save);
        this.button_item_delete = findViewById(R.id.button_item_delete);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class DeleteItem extends AsyncTask<Void, Void, Boolean>{

        private Item item;

        public DeleteItem(Item item){

            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return service.deleteItem(this.item);
        }

        @Override
        protected void onPostExecute(Boolean result) {

            setResult(RESULT_OK);
            finish();

            super.onPostExecute(result);
        }
    }

    private class UpdateItem extends AsyncTask<Void, Void, Boolean>{

        private Item item;

        public UpdateItem(Item item){

            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            return service.updateItem(this.item);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            setResult(RESULT_OK);
            finish();

            super.onPostExecute(aBoolean);
        }
    }


}
