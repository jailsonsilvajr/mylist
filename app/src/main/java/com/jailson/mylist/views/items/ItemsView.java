package com.jailson.mylist.views.items;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jailson.mylist.R;
import com.jailson.mylist.activity.EditItemActivity;
import com.jailson.mylist.mvps.ItemMVP;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.presenters.items.ItemsPresenter;
import com.jailson.mylist.views.cart.CartView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemsView extends AppCompatActivity implements ItemMVP.ItemView {

    private TextView tvItens_nameList;
    private RecyclerView recyclerView_items;
    private TextView textview_price_into_cart;
    private LinearLayout linearlayout_price_into_cart;

    private RecyclerItemsAdapter recyclerItemsAdapter;
    private com.jailson.mylist.object.List list;
    private java.util.List<Item> items;
    private java.util.List<Item> items_in_cart;

    private double value;

    private DecimalFormat df;

    private static final int ACTIVITY_ADDITEM_REQUEST = 1;
    private static final int ACTIVITY_EDITITEM_REQUEST = 2;
    private static final int ACTIVITY_CART_REQUEST = 3;

    private ItemMVP.ItemPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.df = new DecimalFormat("#,###.00");

        this.list = (com.jailson.mylist.object.List) getIntent().getSerializableExtra("list");

        this.tvItens_nameList = findViewById(R.id.tvItens_nameList);
        this.textview_price_into_cart = findViewById(R.id.textview_price_into_cart);

        this.tvItens_nameList.setText(list.getName());

        this.presenter = new ItemsPresenter(this);

        this.items = new ArrayList<>();
        this.items_in_cart = new ArrayList<>();

        this.recyclerView_items = findViewById(R.id.recycler_view_activity_items);
        this.recyclerView_items.setHasFixedSize(true);
        this.recyclerView_items.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        this.linearlayout_price_into_cart = findViewById(R.id.linearlayout_price_into_cart);
        this.linearlayout_price_into_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), CartView.class);
                intent.putExtra("list", list);
                startActivityForResult(intent, ACTIVITY_CART_REQUEST);
            }
        });

        getItems();
    }

    private void count_value() {

        this.value = 0.0;
        for(int i = 0; i < this.items_in_cart.size(); i++) {

            this.value += (this.items_in_cart.get(i).getPrice() * this.items_in_cart.get(i).getQtd());
        }
        if(this.value != 0.0) this.textview_price_into_cart.setText("R$: " + this.df.format(this.value));
        else this.textview_price_into_cart.setText("R$: 0.00");
    }

    private void getItems(){

        GetItems getItems = new GetItems(this.list.getId(), this.presenter);
        getItems.execute();
    }

    private void clickAddItem(){

        Intent intent = new Intent(this, AddItemView.class);
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
        }else if(requestCode == ACTIVITY_CART_REQUEST){
            if(resultCode == RESULT_OK){

                getItems();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuItem_add: {

                clickAddItem();
                return true;
            }
            case android.R.id.home: {

                finish();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void returnGetItems(List<Item> items) {

        if(items != null){

            this.items.clear();
            this.items_in_cart.clear();
            for(int i = 0; i < items.size(); i++){

                if(items.get(i).getInto_cart() == 0) this.items.add(items.get(i));
                else this.items_in_cart.add(items.get(i));
            }

            count_value();
            if(recyclerItemsAdapter != null){

                recyclerItemsAdapter.setListItems(this.items);
                recyclerItemsAdapter.notifyDataSetChanged();
            }else{

                recyclerItemsAdapter = new RecyclerItemsAdapter(this.items, getApplicationContext(), this.presenter);
                recyclerView_items.setAdapter(recyclerItemsAdapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new Swipe(recyclerItemsAdapter));
                itemTouchHelper.attachToRecyclerView(recyclerView_items);
            }
        }else{

            Toast.makeText(this, "Get Items Fail", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void returnDelete(boolean delete) {

        if(delete) getItems();
        else Toast.makeText(this, "Delete Item Fail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void returnInsertIntoCart(boolean insert) {

        if(insert) getItems();
        else Toast.makeText(this, "Insert Into Cart Fail", Toast.LENGTH_LONG).show();
    }

    private class RecyclerItemsAdapter extends RecyclerView.Adapter<ViewHolderItems>{

        private java.util.List<Item> items;
        private Context context;
        private ItemMVP.ItemPresenter presenter;

        public RecyclerItemsAdapter(java.util.List<Item> items, Context context, ItemMVP.ItemPresenter presenter) {

            this.items = items;
            this.context = context;
            this.presenter = presenter;
        }

        public void deleteItem(int position){

            DeleteItem deleteItem = new DeleteItem(this.items.get(position).getId(), this.presenter);
            deleteItem.execute();
        }

        public void addIntoCart(int position){

            this.items.get(position).setInto_cart(1);
            InsertIntoCart insertIntoCart = new InsertIntoCart(this.items.get(position), this.presenter);
            insertIntoCart.execute();
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

            Intent intent = new Intent(this.context, EditItemView.class);
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

        public void setListItems(java.util.List<Item> items){

            this.items = items;
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

            this.icon_remove_cart = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_remove_to_list);

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
            else this.recyclerItemsAdapter.addIntoCart(position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View view = viewHolder.itemView;
            int backgroundCornerOffset = 17;

            if(dX > 0){ //right

                int iconAddMargin = (view.getHeight() - icon_add_cart.getIntrinsicHeight()) / 2;
                int iconAddTop = view.getTop() + (view.getHeight() - icon_add_cart.getIntrinsicHeight()) / 2;
                int iconAddBottom = iconAddTop + icon_add_cart.getIntrinsicHeight();

                int iconLeft = view.getLeft() + iconAddMargin;
                int iconRight = view.getLeft() + iconAddMargin + icon_add_cart.getIntrinsicWidth();

                icon_add_cart.setBounds(iconLeft, iconAddTop, iconRight, iconAddBottom);
                background_add.setBounds(view.getLeft(), view.getTop(),
                        view.getLeft() + ((int) dX) + backgroundCornerOffset, view.getBottom());

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
                        view.getRight() + ((int) dX) - backgroundCornerOffset, view.getBottom());

                background_remove.draw(c);
                icon_remove_cart.draw(c);
            }
        }
    }

    private class GetItems extends AsyncTask<Void, Void, Void > {

        private String id_list;
        private ItemMVP.ItemPresenter presenter;

        public GetItems(String id_list, ItemMVP.ItemPresenter presenter){

            this.id_list = id_list;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.presenter.getItems(this.id_list);
            return null;
        }
    }

    private class DeleteItem extends AsyncTask<Void, Void, Void>{

        private String id_item;
        private ItemMVP.ItemPresenter presenter;

        public DeleteItem(String id_item, ItemMVP.ItemPresenter presenter){

            this.id_item = id_item;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.presenter.deleteItem(id_item);
            return null;
        }
    }

    private class InsertIntoCart extends AsyncTask<Void, Void, Void>{

        private Item item;
        private ItemMVP.ItemPresenter presenter;

        public InsertIntoCart(Item item, ItemMVP.ItemPresenter presenter){

            this.item = item;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.presenter.insertInCart(this.item.getId(), 1);
            return null;
        }
    }
}
