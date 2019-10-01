package com.jailson.mylist.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.object.List;
import com.jailson.mylist.service.Service;

import java.util.concurrent.ExecutionException;

public class AddItemActivity extends AppCompatActivity {

    private ImageView imageview_add_item;
    private TextView textview_add_item_name_img;
    private EditText edittext_add_item_name;
    private EditText edittext_add_item_mark;
    private EditText edittext_add_item_price;
    private ImageView imageview_add_item_button_add_qtd;
    private ImageView imageview_add_item_button_less_qtd;
    private TextView textview_add_item_qtd;
    private Button button_add_item_save;

    private List list;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        this.service = new Service();
        this.list = (List) getIntent().getSerializableExtra("list");

        init_views();
        load_img();
        click_button_save();
        add_less_qtd();
    }

    private void add_less_qtd() {

        this.imageview_add_item_button_add_qtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtd = Integer.parseInt(textview_add_item_qtd.getText().toString());
                qtd += 1;
                textview_add_item_qtd.setText(qtd+"");
            }
        });

        this.imageview_add_item_button_less_qtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qtd = Integer.parseInt(textview_add_item_qtd.getText().toString());
                qtd -= 1;
                if(qtd < 1) qtd = 1;
                textview_add_item_qtd.setText(qtd+"");
            }
        });
    }

    private void click_button_save() {

        this.button_add_item_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Item item = new Item(0,
                        edittext_add_item_name.getText().toString(),
                        edittext_add_item_mark.getText().toString(),
                        Double.parseDouble(edittext_add_item_price.getText().toString()),
                        Integer.parseInt(textview_add_item_qtd.getText().toString()),
                        list.getId(),
                        "url_img");

                if(add_item(item)){

                    Toast.makeText(AddItemActivity.this, "Success", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                }else{

                    Toast.makeText(AddItemActivity.this, "Fail", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void load_img() {

        this.imageview_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(data != null){

            Bundle bundle = data.getExtras();
            if(bundle != null){

                Bitmap img = (Bitmap) bundle.get("data");
                //img.createScaledBitmap(img, 150, 150, false);
                this.imageview_add_item.setImageBitmap(img);
            }
        }
    }

    private void init_views() {

        this.imageview_add_item = findViewById(R.id.imageview_add_item);
        this.textview_add_item_name_img = findViewById(R.id.textview_add_item_name_img);
        this.edittext_add_item_name = findViewById(R.id.edittext_add_item_name);
        this.edittext_add_item_mark = findViewById(R.id.edittext_add_item_mark);
        this.edittext_add_item_price = findViewById(R.id.edittext_add_item_price);
        this.imageview_add_item_button_add_qtd = findViewById(R.id.imageview_add_item_button_add_qtd);
        this.imageview_add_item_button_less_qtd = findViewById(R.id.imageview_add_item_button_less_qtd);
        this.textview_add_item_qtd = findViewById(R.id.textview_add_item_qtd);
        this.button_add_item_save = findViewById(R.id.button_add_item_save);
    }

    private boolean add_item(Item item){

        try {

            if(this.service.addItem(item)) return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
