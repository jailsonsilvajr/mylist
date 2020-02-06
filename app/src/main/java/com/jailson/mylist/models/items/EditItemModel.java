package com.jailson.mylist.models.items;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jailson.mylist.mvps.EditItemMVP;
import com.jailson.mylist.object.Item;

import java.util.HashMap;
import java.util.Map;

public class EditItemModel implements EditItemMVP.EditItemModel {

    private EditItemMVP.EditItemPresenter presenter;

    public EditItemModel(EditItemMVP.EditItemPresenter presenter){

        this.presenter = presenter;
    }

    @Override
    public void editItem(Item item) {

        Map<String, String> item_map = new HashMap<>();
        item_map.put("name", item.getName());
        item_map.put("mark", item.getMark());
        item_map.put("price", Double.toString(item.getPrice()));
        item_map.put("quantity", Integer.toString(item.getQtd()));
        item_map.put("id_list", item.getId_list());
        item_map.put("into_cart", Integer.toString(item.getInto_cart()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").document(item.getId()).set(item_map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                presenter.returnEditItem(task.isSuccessful());
            }
        });
    }
}
