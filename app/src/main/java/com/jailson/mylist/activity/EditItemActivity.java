package com.jailson.mylist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.service.Service;

public class EditItemActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_mark;
    private TextInputLayout textInputLayout_price;
    private ImageView imageview_item_add;
    private ImageView imageview_item_less;
    private TextView textview_item_qtd;
    private Button button_item_save;

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
                        String.valueOf(textInputLayout_name.getEditText().getText()),
                        String.valueOf(textInputLayout_mark.getEditText().getText()),
                        Double.parseDouble(String.valueOf(textInputLayout_price.getEditText().getText())),
                        Integer.parseInt(String.valueOf(textview_item_qtd.getText())),
                        item.getId_list(),
                        item.getInto_cart(),
                        item.getUrl_img());

                UpdateItem updateItem = new UpdateItem(item_temp);
                updateItem.execute();
            }
        });
    }

    private void set_views() {

        this.textInputLayout_name.getEditText().setText(this.item.getName());
        this.textInputLayout_mark.getEditText().setText(this.item.getMark());
        this.textInputLayout_price.getEditText().setText(Double.toString(this.item.getPrice()));
        this.textview_item_qtd.setText(Integer.toString(this.item.getQtd()));
    }

    private void init_views() {

        this.textInputLayout_name = findViewById(R.id.edittext_item_name);
        this.textInputLayout_mark = findViewById(R.id.edittext_item_mark);
        this.textInputLayout_price = findViewById(R.id.edittext_item_price);
        this.imageview_item_add = findViewById(R.id.imageview_item_add);
        this.imageview_item_less= findViewById(R.id.imageview_item_less);
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

            if(aBoolean){

                setResult(RESULT_OK);
                finish();
            }else{

                Toast.makeText(EditItemActivity.this, "Fail add", Toast.LENGTH_LONG).show();
            }

            super.onPostExecute(aBoolean);
        }
    }


}
