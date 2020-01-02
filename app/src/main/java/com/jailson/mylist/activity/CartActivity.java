package com.jailson.mylist.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;
import com.jailson.mylist.object.List;
import com.jailson.mylist.service.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView_cart;
    private RecyclerCartAdapter recyclerCartAdapter;

    private TextView tvCart_name;

    private List list;

    private java.util.List<Item> items;

    private Service service;

    private static final int ACTIVITY_EDITITEM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.service = new Service();
        this.list = (List) getIntent().getSerializableExtra("list");

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

        GetItemsCart getItems = new GetItemsCart(this.list.getId());
        getItems.execute();
    }

    private class RecyclerCartAdapter extends RecyclerView.Adapter<ViewHolderCart>{

        private java.util.List<Item> items;
        private Context context;
        private Service service;

        public RecyclerCartAdapter(java.util.List<Item> items, Context context, Service service) {

            this.items = items;
            this.context = context;
            this.service = service;
        }

        public void deleteItem(int position){

            DeleteItem deleteItem = new DeleteItem(this.items.get(position), position);
            deleteItem.execute();
        }

        public void removeFromCart(int position){

            this.items.get(position).setInto_cart(0);
            UpdateItem updateItem = new UpdateItem(this.items.get(position), position);
            updateItem.execute();
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

            Intent intent = new Intent(this.context, EditItemActivity.class);
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

    private class GetItemsCart extends AsyncTask<Void, Void, java.util.List<Item> >{

        private String id_list;

        public GetItemsCart(String id_list){

            this.id_list = id_list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected java.util.List<Item> doInBackground(Void... voids) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("items").whereEqualTo("id_list", id_list).whereEqualTo("into_cart", "1")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                java.util.List<Item> items_into_cart_temp = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Item item = new Item(
                                            document.getId(),
                                            document.get("name").toString(),
                                            document.get("mark").toString(),
                                            Double.parseDouble(document.get("price").toString()),
                                            Integer.parseInt(document.get("quantity").toString()),
                                            document.get("id_list").toString(),
                                            Integer.parseInt(document.get("into_cart").toString()),
                                            null
                                    );
                                    items_into_cart_temp.add(item);
                                }
                                items = items_into_cart_temp;
                                if(recyclerCartAdapter != null){

                                    recyclerCartAdapter.setListItems(items);
                                    recyclerCartAdapter.notifyDataSetChanged();
                                }else{

                                    recyclerCartAdapter = new RecyclerCartAdapter(items, getApplicationContext(), service);
                                    recyclerView_cart.setAdapter(recyclerCartAdapter);
                                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new Swipe(recyclerCartAdapter));
                                    itemTouchHelper.attachToRecyclerView(recyclerView_cart);
                                }

                            }else{

                                Toast.makeText(CartActivity.this, "Fail", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            return null;
        }
    }

    private class DeleteItem extends AsyncTask<Void, Void, Boolean> {

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

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("items").document(this.item.getId()).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                items.remove(position);
                                recyclerCartAdapter.notifyItemRemoved(position);
                            }else{


                            }
                        }
                    });

            return null;
        }
    }

    private class UpdateItem extends AsyncTask<Void, Void, Boolean>{

        private Item item;
        private int position;

        public UpdateItem(Item item, int position){

            this.item = item;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, String> map = new HashMap<>();
            map.put("name", item.getName());
            map.put("mark", item.getMark());
            map.put("price", Double.toString(item.getPrice()));
            map.put("quantity", Integer.toString(item.getQtd()));
            map.put("id_list", item.getId_list());
            map.put("into_cart", Integer.toString(item.getInto_cart()));

            db.collection("items").document(item.getId()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        getItems();
                    }else{

                        item.setInto_cart(1);
                    }
                    recyclerCartAdapter.notifyDataSetChanged();
                }
            });

            return null;
        }
    }
}
