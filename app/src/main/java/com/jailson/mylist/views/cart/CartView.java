package com.jailson.mylist.views.cart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.jailson.mylist.mvps.CartMVP;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.presenters.cart.CartPresenter;
import com.jailson.mylist.views.items.EditItemView;

import java.util.List;

public class CartView extends AppCompatActivity implements CartMVP.CartView {

    private CartMVP.CartPresenter presenter;
    private RecyclerView recyclerView_cart;
    private RecyclerCartAdapter recyclerCartAdapter;
    private TextView tvCart_name;
    private com.jailson.mylist.object.List list;
    private java.util.List<Item> items;
    private static final int ACTIVITY_EDITITEM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.presenter = new CartPresenter(this);
        this.list = (com.jailson.mylist.object.List) getIntent().getSerializableExtra("list");

        this.tvCart_name = findViewById(R.id.tvCart_name);
        this.tvCart_name.setText("Cart - " + list.getName());

        getItems();

        this.recyclerView_cart = findViewById(R.id.recycler_view_activity_cart);
        this.recyclerView_cart.setHasFixedSize(true);
        this.recyclerView_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){

            setResult(RESULT_OK);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == ACTIVITY_EDITITEM_REQUEST){

            if(resultCode == RESULT_OK){

                getItems();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getItems(){

        GetItemsCart getItems = new GetItemsCart(this.list.getId(), this.presenter);
        getItems.execute();
    }

    private class RecyclerCartAdapter extends RecyclerView.Adapter<ViewHolderCart>{

        private java.util.List<Item> items;
        private Context context;
        private CartMVP.CartPresenter presenter;

        public RecyclerCartAdapter(java.util.List<Item> items, Context context, CartMVP.CartPresenter presenter) {

            this.items = items;
            this.context = context;
            this.presenter = presenter;
        }

        public void deleteItem(int position){

            DeleteItem deleteItem = new DeleteItem(this.items.get(position).getId(), this.presenter);
            deleteItem.execute();
        }

        public void removeFromCart(int position){

            RemoveIntoCart removeIntoCart = new RemoveIntoCart(this.items.get(position).getId(), this.presenter);
            removeIntoCart.execute();
        }

        @NonNull
        @Override
        public ViewHolderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);
            final ViewHolderCart viewHolderItems = new ViewHolderCart(view);
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
        public void onBindViewHolder(@NonNull ViewHolderCart holder, int position) {

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

    private class ViewHolderCart extends RecyclerView.ViewHolder{

        private TextView textView_name;
        private TextView textView_mark;
        private TextView textView_qtd;
        private TextView textView_price;

        public ViewHolderCart(@NonNull View itemView) {

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

        private RecyclerCartAdapter recyclerCartAdapter;

        private Drawable icon_remove_cart;
        private Drawable icon_delete_list;
        private final ColorDrawable background_remove_cart;
        private final ColorDrawable background_delete_list;

        public Swipe(RecyclerCartAdapter recyclerCartAdapter){

            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.recyclerCartAdapter = recyclerCartAdapter;

            this.icon_remove_cart = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_remove_cart);
            this.icon_delete_list = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_remove_to_list);

            this.background_remove_cart = new ColorDrawable(Color.YELLOW);
            this.background_delete_list = new ColorDrawable(Color.RED);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            if(direction == ItemTouchHelper.LEFT) this.recyclerCartAdapter.deleteItem(position);
            else this.recyclerCartAdapter.removeFromCart(position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View view = viewHolder.itemView;
            int backgroundCornerOffset = 17;

            if(dX > 0){ //right

                int iconAddMargin = (view.getHeight() - icon_remove_cart.getIntrinsicHeight()) / 2;
                int iconAddTop = view.getTop() + (view.getHeight() - icon_remove_cart.getIntrinsicHeight()) / 2;
                int iconAddBottom = iconAddTop + icon_remove_cart.getIntrinsicHeight();

                int iconLeft = view.getLeft() + iconAddMargin;
                int iconRight = view.getLeft() + iconAddMargin + icon_remove_cart.getIntrinsicWidth();

                icon_remove_cart.setBounds(iconLeft, iconAddTop, iconRight, iconAddBottom);
                background_remove_cart.setBounds(view.getLeft(), view.getTop(),
                        view.getLeft() + ((int) dX) + backgroundCornerOffset, view.getBottom());

                background_remove_cart.draw(c);
                icon_remove_cart.draw(c);
            } else if (dX < 0) { //left

                int iconDeleteMargin = (view.getHeight() - icon_delete_list.getIntrinsicHeight()) / 2;
                int iconDeleteTop = view.getTop() + (view.getHeight() - icon_delete_list.getIntrinsicHeight()) / 2;
                int iconDeleteBottom = iconDeleteTop + icon_delete_list.getIntrinsicHeight();

                int iconLeft = view.getRight() - iconDeleteMargin - icon_delete_list.getIntrinsicWidth();
                int iconRight = view.getRight() - iconDeleteMargin;

                icon_delete_list.setBounds(iconLeft, iconDeleteTop, iconRight, iconDeleteBottom);
                background_delete_list.setBounds(view.getRight(), view.getTop(),
                        view.getRight() + ((int) dX) - backgroundCornerOffset, view.getBottom());

                background_delete_list.draw(c);
                icon_delete_list.draw(c);
            }
        }
    }

    private class GetItemsCart extends AsyncTask<Void, Void, Void > {

        private String id_list;
        private CartMVP.CartPresenter presenter;

        public GetItemsCart(String id_list, CartMVP.CartPresenter presenter){

            this.id_list = id_list;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.presenter.getItems(this.id_list);
            return null;
        }
    }

    private class DeleteItem extends AsyncTask<Void, Void, Boolean> {

        private String id_item;
        private CartMVP.CartPresenter presenter;

        public DeleteItem(String id_item, CartMVP.CartPresenter presenter){

            this.id_item = id_item;
            this.presenter = presenter;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            this.presenter.deleteItem(this.id_item);
            return null;
        }
    }

    private class RemoveIntoCart extends AsyncTask<Void, Void, Void>{

        private String id_item;
        private CartMVP.CartPresenter presenter;

        public RemoveIntoCart(String id_item, CartMVP.CartPresenter presenter){

            this.id_item = id_item;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.presenter.removeIntoCart(this.id_item);
            return null;
        }
    }


    @Override
    public void returnGetItems(List<Item> items) {

        if(items != null){

            if(recyclerCartAdapter != null){

                recyclerCartAdapter.setListItems(items);
                recyclerCartAdapter.notifyDataSetChanged();
            }else{

                recyclerCartAdapter = new RecyclerCartAdapter(items, getApplicationContext(), this.presenter);
                recyclerView_cart.setAdapter(recyclerCartAdapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new Swipe(recyclerCartAdapter));
                itemTouchHelper.attachToRecyclerView(recyclerView_cart);
            }
        }else{

            Toast.makeText(this, "Get Items In Cart Fail", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void returnDeleteItem(boolean delete) {

        if(delete) getItems();
        else Toast.makeText(this, "Delete Item Fail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void returnRemoveIntoCart(boolean remove) {

        if(remove) getItems();
        else Toast.makeText(this, "Remove Item Int Cart Fail", Toast.LENGTH_LONG).show();
    }
}
