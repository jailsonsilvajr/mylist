package com.jailson.mylist.views.items;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.jailson.mylist.R;
import com.jailson.mylist.mvps.AddItemMVP;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.object.List;
import com.jailson.mylist.presenters.items.AddItemPresenter;

public class AddItemView extends AppCompatActivity implements AddItemMVP.AddItemView {

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_mark;
    private TextInputLayout textInputLayout_price;
    private ImageView imageview_item_add;
    private ImageView imageview_item_less;
    private TextView textview_item_qtd;
    private Button button_item_save;

    private List list;

    private AddItemMVP.AddItemPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.list = (List) getIntent().getSerializableExtra("list");

        this.presenter = new AddItemPresenter(this);

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

                AddItem addItem = new AddItem(item, presenter);
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

    private class AddItem extends AsyncTask<Void, Void, Void> {

        private Item item;
        private AddItemMVP.AddItemPresenter presenter;

        public AddItem(Item item, AddItemMVP.AddItemPresenter presenter){

            this.item = item;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.presenter.addItem(this.item);
            return null;
        }
    }

    @Override
    public void returnAddItem(boolean add) {

        if(add){

            setResult(RESULT_OK);
            finish();
        }else{

            Toast.makeText(this, "Add Item Fail", Toast.LENGTH_LONG).show();
        }
    }
}
