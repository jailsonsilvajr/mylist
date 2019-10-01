package com.jailson.mylist.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.object.List;
import com.jailson.mylist.service.Service;
import com.jailson.mylist.util.AdapterItens;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class ItemsActivity extends AppCompatActivity {

    private TextView tvItens_nameList;
    private TextView tvItens_priceList;
    private ListView listView_itens;

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

        this.df = new DecimalFormat("#,###.00");

        this.service = new Service();
        this.list = (List) getIntent().getSerializableExtra("list");

        getItems();
        count_value();
        init_views();
        show_items();
        click_item();
    }

    private void count_value() {

        for(int i = 0; i < this.items.size(); i++) this.value += (this.items.get(i).getPrice() * this.items.get(i).getQtd());

    }

    private void click_item() {

        this.listView_itens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickItem(position);
            }
        });
    }

    private void show_items() {

        AdapterItens adapterItens = new AdapterItens(this.items, this);
        this.listView_itens.setAdapter(adapterItens);
    }

    private void init_views() {

        this.tvItens_nameList = findViewById(R.id.tvItens_nameList);
        this.tvItens_priceList = findViewById(R.id.tvItens_priceList);
        this.listView_itens = findViewById(R.id.lvItens_itens);

        this.tvItens_nameList.setText(list.getName());
        this.tvItens_priceList.setText("R$: " + this.df.format(this.value));
    }

    private boolean getItems(){

        try {

            this.items = this.service.getItens(this.list.getId());
            if(this.items != null) return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
        return false;
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
                count_value();
                init_views();
                AdapterItens adapterItens = new AdapterItens(items, ItemsActivity.this);
                this.listView_itens.setAdapter(adapterItens);
            }
        }else if(requestCode == ACTIVITY_EDITITEM_REQUEST){
            if(resultCode == RESULT_OK){

                getItems();
                count_value();
                init_views();
                AdapterItens adapterItens = new AdapterItens(items, ItemsActivity.this);
                this.listView_itens.setAdapter(adapterItens);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.menuItem_add){

            clickAddItem();
        }

        return super.onOptionsItemSelected(item);
    }
}
