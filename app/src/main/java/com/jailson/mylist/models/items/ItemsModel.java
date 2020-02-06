package com.jailson.mylist.models.items;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jailson.mylist.mvps.ItemMVP;
import com.jailson.mylist.object.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsModel implements ItemMVP.ItemModel {

    private ItemMVP.ItemPresenter presenter;

    public ItemsModel(ItemMVP.ItemPresenter presenter){

        this.presenter = presenter;
    }

    @Override
    public void getItems(String id_list) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference itemsRef = db.collection("items");
        final Query query = itemsRef.whereEqualTo("id_list", id_list);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<Item> items = null;
                if(task.isSuccessful()){

                    items = new ArrayList<>();
                    for(QueryDocumentSnapshot document : task.getResult()){

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
                        items.add(item);
                    }
                }
                presenter.returnGetItems(items);
            }
        });
    }

    @Override
    public void deleteItem(String id_item) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").document(id_item).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                presenter.returnDelete(task.isSuccessful());
            }
        });
    }

    @Override
    public void insertInCart(String id_item, int insert) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").document(id_item).update("into_cart", Integer.toString(insert)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                presenter.returnInsertIntoCart(task.isSuccessful());
            }
        });
    }
}
