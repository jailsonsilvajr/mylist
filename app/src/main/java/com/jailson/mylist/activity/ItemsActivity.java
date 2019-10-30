package com.jailson.mylist.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.object.List;
import com.jailson.mylist.service.Service;

import java.text.DecimalFormat;

public class ItemsActivity extends AppCompatActivity {

    private TextView tvItens_nameList;
    private TextView tvItens_priceList;
    private RecyclerView recyclerView_items;
    private FloatingActionButton fabItems;

    private RecyclerItemsAdapter recyclerItemsAdapter;
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

        this.fabItems = findViewById(R.id.fabItems);

        this.tvItens_nameList.setText(list.getName());

        getItems();

        this.recyclerView_items = findViewById(R.id.recycler_view_activity_items);
        this.recyclerView_items.setHasFixedSize(true);
        this.recyclerView_items.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        this.fabItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickAddItem();
            }
        });
    }

    private void count_value() {

        this.value = 0.0;
        for(int i = 0; i < this.items.size(); i++) this.value += (this.items.get(i).getPrice() * this.items.get(i).getQtd());
        if(this.value != 0.0) this.tvItens_priceList.setText("R$: " + this.df.format(this.value));
        else this.tvItens_priceList.setText("R$: 0");
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

                recyclerItemsAdapter = new RecyclerItemsAdapter(items, getApplicationContext(), service);
                recyclerView_items.setAdapter(recyclerItemsAdapter);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new Swipe(recyclerItemsAdapter));
                itemTouchHelper.attachToRecyclerView(recyclerView_items);
            }
            else Toast.makeText(ItemsActivity.this, "Fail", Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
        }
    }

    private class DeleteItem extends AsyncTask<Void, Void, Boolean>{

        private Item item;
        private int position;

        public DeleteItem(Item item, int position){

            this.item = item;
            this.position = position;
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

            items.remove(position);
            recyclerItemsAdapter.notifyItemRemoved(position);
            count_value();

            super.onPostExecute(result);
        }
    }

    private class RecyclerItemsAdapter extends RecyclerView.Adapter<ViewHolderItems>{

        private java.util.List<Item> items;
        private Context context;
        private Service service;

        public RecyclerItemsAdapter(java.util.List<Item> items, Context context, Service service) {

            this.items = items;
            this.context = context;
            this.service = service;
        }

        public void deleteItem(int position){

            DeleteItem deleteItem = new DeleteItem(items.get(position), position);
            deleteItem.execute();
        }



        @NonNull
        @Override
        public ViewHolderItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);
            final ViewHolderItems viewHolderItems = new ViewHolderItems(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickItemRecyclerView(items.get(viewHolderItems.getAdapterPosition()));
                }
            });

            return viewHolderItems;
        }

        private void clickItemRecyclerView(Item item) {

            Intent intent = new Intent(this.context, EditItemActivity.class);
            intent.putExtra("item", item);
            startActivityForResult(intent, ACTIVITY_EDITITEM_REQUEST);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderItems holder, int position) {

            Item item = this.items.get(position);
            holder.getTextView_name().setText(item.getName());
            holder.getTextView_mark().setText(item.getMark());
            holder.getTextView_qtd().setText(item.getQtd()+"");
            holder.getTextView_price().setText(Double.toString(item.getPrice()));
        }

        @Override
        public int getItemCount() {

            return this.items.size();
        }
    }

    private class ViewHolderItems extends RecyclerView.ViewHolder{

        private TextView textView_name;
        private TextView textView_mark;
        private TextView textView_qtd;
        private TextView textView_price;

        public ViewHolderItems(@NonNull View itemView) {

            super(itemView);
            this.textView_name = itemView.findViewById(R.id.textview_name_iten);
            this.textView_mark = itemView.findViewById(R.id.textview_mark_iten);
            this.textView_qtd = itemView.findViewById(R.id.textview_qtd_iten);
            this.textView_price = itemView.findViewById(R.id.textview_price_iten);
        }

        public TextView getTextView_name() {
            return textView_name;
        }

        public TextView getTextView_mark() {
            return textView_mark;
        }

        public TextView getTextView_qtd() {
            return textView_qtd;
        }

        public TextView getTextView_price() {
            return textView_price;
        }
    }

    private class Swipe extends ItemTouchHelper.SimpleCallback{

        private RecyclerItemsAdapter recyclerItemsAdapter;

        private Drawable icon_remove_cart;
        private Drawable icon_add_cart;
        private final ColorDrawable background_remove;
        private final ColorDrawable background_add;

        public Swipe(RecyclerItemsAdapter recyclerItemsAdapter){

            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.recyclerItemsAdapter = recyclerItemsAdapter;

            this.icon_remove_cart = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_remove_cart);

            this.icon_add_cart = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_add_cart);

            this.background_remove = new ColorDrawable(Color.RED);
            this.background_add = new ColorDrawable(Color.GREEN);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            if(direction == ItemTouchHelper.LEFT) this.recyclerItemsAdapter.deleteItem(position);
            else this.recyclerItemsAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View view = viewHolder.itemView;

            if(dX > 0){ //right

                int iconAddMargin = (view.getHeight() - icon_add_cart.getIntrinsicHeight()/2);
                int iconAddTop = view.getTop() + (view.getHeight() - icon_add_cart.getIntrinsicHeight()) / 2;
                int iconAddBottom = iconAddTop + icon_add_cart.getIntrinsicHeight();

                int iconLeft = view.getLeft() + iconAddMargin;
                int iconRight = view.getLeft() + iconAddMargin + icon_add_cart.getIntrinsicWidth();
                icon_add_cart.setBounds(iconLeft, iconAddTop, iconRight, iconAddBottom);

                background_add.setBounds(view.getLeft(), view.getTop(),
                        view.getLeft() + ((int) dX), view.getBottom());

                background_add.draw(c);
                icon_add_cart.draw(c);
            } else if (dX < 0) { //left

                int iconDeleteMargin = (view.getHeight() - icon_remove_cart.getIntrinsicHeight()) / 2;
                int iconDeleteTop = view.getTop() + (view.getHeight() - icon_remove_cart.getIntrinsicHeight()) / 2;
                int iconDeleteBottom = iconDeleteTop + icon_remove_cart.getIntrinsicHeight();

                int iconLeft = view.getRight() - iconDeleteMargin - icon_remove_cart.getIntrinsicWidth();
                int iconRight = view.getRight() - iconDeleteMargin;
                icon_remove_cart.setBounds(iconLeft, iconDeleteTop, iconRight, iconDeleteBottom);

                background_remove.setBounds(view.getRight(), view.getTop(),
                        view.getRight() + ((int) dX), view.getBottom());

                background_remove.draw(c);
                icon_remove_cart.draw(c);
            }
        }
    }
}
