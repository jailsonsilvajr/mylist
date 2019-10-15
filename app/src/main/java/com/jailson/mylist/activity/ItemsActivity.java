package com.jailson.mylist.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.object.List;
import com.jailson.mylist.service.Service;
import com.jailson.mylist.util.AdapterItens;

import java.text.DecimalFormat;

public class ItemsActivity extends AppCompatActivity {

    private TextView tvItens_nameList;
    private TextView tvItens_priceList;
    private ListView listView_itens;
    private FloatingActionButton fabItems;

    private List list;
    private java.util.List<Item> items;
    private Service service;

    private double value;

    private DecimalFormat df;

    private static final int ACTIVITY_ADDITEM_REQUEST = 1;
    private static final int ACTIVITY_EDITITEM_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.df = new DecimalFormat("#,###.00");

        this.service = new Service();
        this.list = (List) getIntent().getSerializableExtra("list");

        this.tvItens_nameList = findViewById(R.id.tvItens_nameList);
        this.tvItens_priceList = findViewById(R.id.tvItens_priceList);
        this.listView_itens = findViewById(R.id.lvItens_itens);
        this.fabItems = findViewById(R.id.fabItems);

        this.tvItens_nameList.setText(list.getName());

        getItems();

        clicks();
    }

    private void count_value() {

        this.value = 0.0;
        for(int i = 0; i < this.items.size(); i++) this.value += (this.items.get(i).getPrice() * this.items.get(i).getQtd());
        this.tvItens_priceList.setText("R$: " + this.df.format(this.value));
    }

    private void clicks() {

        this.listView_itens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickItem(position);
            }
        });

        this.fabItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickAddItem();
            }
        });
    }

    private void show_items() {

        AdapterItens adapterItens = new AdapterItens(this.items, this);
        this.listView_itens.setAdapter(adapterItens);
    }

    private void getItems(){

        GetItems getItems = new GetItems(this.list.getId());
        getItems.execute();
    }

    private void clickAddItem(){

        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("list", this.list);
        startActivityForResult(intent, ACTIVITY_ADDITEM_REQUEST);
    }



    private void clickItem(int position){

        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("item", this.items.get(position));
        startActivityForResult(intent, ACTIVITY_EDITITEM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == ACTIVITY_ADDITEM_REQUEST){
            if(resultCode == RESULT_OK){

                getItems();
            }
        }else if(requestCode == ACTIVITY_EDITITEM_REQUEST){
            if(resultCode == RESULT_OK){

                getItems();
            }
        }
    }

    private class GetItems extends AsyncTask<Void, Void, java.util.List<Item> >{

        private int id_item;

        public GetItems(int id_item){

            this.id_item = id_item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected java.util.List<Item> doInBackground(Void... voids) {

            return service.getItens(this.id_item);
        }

        @Override
        protected void onPostExecute(java.util.List<Item> result) {

            if(result != null) {

                items = result;
                count_value();
                show_items();
            }
            else Toast.makeText(ItemsActivity.this, "Fail", Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
        }
    }
}
