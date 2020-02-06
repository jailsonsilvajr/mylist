package com.jailson.mylist.models.items;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jailson.mylist.mvps.AddItemMVP;
import com.jailson.mylist.object.Item;

import java.util.HashMap;
import java.util.Map;

public class AddItemModel implements AddItemMVP.AddItemModel {

    private AddItemMVP.AddItemPresenter presenter;

    public  AddItemModel(AddItemMVP.AddItemPresenter presenter){

        this.presenter = presenter;
    }

    @Override
    public void addItem(Item item) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, String> item_map = new HashMap<>();
        item_map.put("name", item.getName());
        item_map.put("mark", item.getMark());
        item_map.put("price", Double.toString(item.getPrice()));
        item_map.put("quantity", Integer.toString(item.getQtd()));
        item_map.put("id_list", item.getId_list());
        item_map.put("into_cart", Integer.toString(item.getInto_cart()));

        db.collection("items").add(item_map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                presenter.returnAddItem(task.isSuccessful());
            }
        });
    }
}
