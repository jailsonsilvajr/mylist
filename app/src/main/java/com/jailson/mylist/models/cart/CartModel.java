package com.jailson.mylist.models.cart;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jailson.mylist.mvps.CartMVP;
import com.jailson.mylist.object.Item;

import java.util.ArrayList;
import java.util.List;

public class CartModel implements CartMVP.CartModel {

    private CartMVP.CartPresenter presenter;

    public CartModel(CartMVP.CartPresenter presenter){

        this.presenter = presenter;
    }

    @Override
    public void getItems(String id_list) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").whereEqualTo("id_list", id_list).whereEqualTo("into_cart", "1").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<Item> items = null;
                if(task.isSuccessful()){

                    items = new ArrayList<>();
                    for(DocumentSnapshot document : task.getResult()){

                        Item item = new Item(
                                document.getId().toString(),
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

                presenter.returnDeleteItem(task.isSuccessful());
            }
        });
    }

    @Override
    public void removeIntoCart(String id_item) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").document(id_item).update("into_cart", "0").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                presenter.returnRemoveIntoCart(task.isSuccessful());
            }
        });
    }
}
