package com.jailson.mylist.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ItensActivity extends AppCompatActivity {

    private TextView tvItens_nameList;
    private TextView tvItens_priceList;
    private ListView listView_itens;

    private ImageView imgEdit_load;
    private ImageView imgAdd;

    private List list;
    private java.util.List<Item> itens;
    private Service service;

    private double value;

    private DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens);

        this.service = new Service();
        this.list = (List) getIntent().getSerializableExtra("list");

        this.tvItens_nameList = findViewById(R.id.tvItens_nameList);
        this.tvItens_nameList.setText(list.getName());

        this.tvItens_priceList = findViewById(R.id.tvItens_priceList);
        getItems();

        this.listView_itens = findViewById(R.id.lvItens_itens);
        AdapterItens adapterItens = new AdapterItens(this.itens, this);
        this.listView_itens.setAdapter(adapterItens);
        this.listView_itens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickItem(position);
            }
        });
    }

    private boolean getItems(){

        try {

            this.itens = this.service.getItens(this.list.getId(), this.tvItens_priceList);
            if(this.itens != null) return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean addItem(Item item){

        try {

            if(this.service.addItem(item)) return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean deleteItem(Item item){

        try {

            if(service.deleteItem(item)) return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean updateItem(Item item){

        try {

            if(service.updateItem(item)) return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void clickAddItem(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_item_popup);

        this.imgAdd = dialog.findViewById(R.id.imgAdd);
        final EditText etAdd_name = dialog.findViewById(R.id.etAdd_name);
        final EditText etAdd_mark = dialog.findViewById(R.id.etAdd_mark);
        final EditText etAdd_price = dialog.findViewById(R.id.etAdd_price);
        final TextView tvAdd_qtd = dialog.findViewById(R.id.tvAdd_qtd);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });

        ImageView img_close = dialog.findViewById(R.id.imgAdd_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        ImageView img_add = dialog.findViewById(R.id.imgAdd_add);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtd = Integer.parseInt(tvAdd_qtd.getText().toString());
                qtd += 1;
                tvAdd_qtd.setText(qtd+"");
            }
        });

        ImageView img_less = dialog.findViewById(R.id.imgAdd_less);
        img_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtd = Integer.parseInt(tvAdd_qtd.getText().toString());
                qtd -= 1;
                tvAdd_qtd.setText(qtd+"");
            }
        });

        Button btn_save = dialog.findViewById(R.id.btnAdd_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Item item = new Item();
                item.setName(etAdd_name.getText().toString());
                item.setMark(etAdd_mark.getText().toString());
                item.setPrice(Double.parseDouble(etAdd_price.getText().toString()));
                item.setQtd(Integer.parseInt(tvAdd_qtd.getText().toString()));
                item.setId_list(list.getId());

                if(addItem(item)){

                    getItems();
                    updateValue();
                    AdapterItens adapterItens = new AdapterItens(itens, ItensActivity.this);
                    listView_itens.setAdapter(adapterItens);
                    dialog.dismiss();
                }else{

                    Toast.makeText(ItensActivity.this, "Was don't possible add the item.", Toast.LENGTH_LONG).show();
                }


            }
        });

        dialog.show();
    }

    private void clickItem(final int position){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_item_popup);

        this.imgEdit_load = dialog.findViewById(R.id.imgEdit_load);
        final EditText etEdit_name = dialog.findViewById(R.id.etEdit_name);
        final EditText etEdit_mark = dialog.findViewById(R.id.etEdit_mark);
        final EditText etEdit_price = dialog.findViewById(R.id.etEdit_price);
        final TextView tvEdit_qtd = dialog.findViewById(R.id.tvEdit_qtd);

        Item item = itens.get(position);

        etEdit_name.setText(item.getName());
        etEdit_mark.setText(item.getMark());
        etEdit_price.setText(item.getPrice()+"");
        tvEdit_qtd.setText(item.getQtd()+"");

        imgEdit_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });

        ImageView img_close = dialog.findViewById(R.id.imgEdit_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        ImageView img_add = dialog.findViewById(R.id.imgEdit_add);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtd = Integer.parseInt(tvEdit_qtd.getText().toString());
                qtd += 1;
                tvEdit_qtd.setText(qtd+"");
            }
        });

        ImageView img_less = dialog.findViewById(R.id.imgEdit_less);
        img_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtd = Integer.parseInt(tvEdit_qtd.getText().toString());
                qtd -= 1;
                tvEdit_qtd.setText(qtd+"");
            }
        });

        Button btn_delete = dialog.findViewById(R.id.btnEdit_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(deleteItem(itens.get(position))){

                    getItems();
                    updateValue();
                    AdapterItens adapterItens = new AdapterItens(itens, ItensActivity.this);
                    listView_itens.setAdapter(adapterItens);
                    dialog.dismiss();
                }else{

                    Toast.makeText(ItensActivity.this, "Was do not possible delete the item.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btn_save = dialog.findViewById(R.id.btnEdit_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itens.get(position).setName(etEdit_name.getText().toString());
                itens.get(position).setMark(etEdit_mark.getText().toString());
                itens.get(position).setPrice(Double.parseDouble(etEdit_price.getText().toString()));
                itens.get(position).setQtd(Integer.parseInt(tvEdit_qtd.getText().toString()));

                if(updateItem(itens.get(position))){

                    getItems();
                    updateValue();
                    AdapterItens adapterItens = new AdapterItens(itens, ItensActivity.this);
                    listView_itens.setAdapter(adapterItens);
                    dialog.dismiss();
                }else{

                    Toast.makeText(ItensActivity.this, "Was do not possible update the item.", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(data != null){

            Bundle bundle = data.getExtras();
            if(bundle != null){

                Bitmap img = (Bitmap) bundle.get("data");
                //img.createScaledBitmap(img, 150, 150, false);
                if(this.imgEdit_load != null){

                    this.imgEdit_load.setImageBitmap(img);
                    this.imgEdit_load = null;
                }
                else{

                    this.imgAdd.setImageBitmap(img);
                    this.imgAdd = null;
                }
            }
        }
    }

    private void updateValue(){

        value = 0;
        for(int i = 0; i < itens.size(); i++) value += (itens.get(i).getPrice() * itens.get(i).getQtd());
        df = new DecimalFormat("#,###.00");
        tvItens_priceList.setText("R$: " + df.format(value));
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
