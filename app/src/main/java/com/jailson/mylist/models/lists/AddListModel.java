package com.jailson.mylist.models.lists;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jailson.mylist.mvps.AddListMVP;
import com.jailson.mylist.object.List;

import java.util.HashMap;
import java.util.Map;

public class AddListModel implements AddListMVP.AddListModel {

    public AddListMVP.AddListPresenter presenter;

    public AddListModel(AddListMVP.AddListPresenter presenter){

        this.presenter = presenter;
    }

    @Override
    public void addList(List list) {

        Map<String, String> list_map = new HashMap<>();
        list_map.put("name", list.getName());
        list_map.put("id_user", list.getId_user());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("lists").add(list_map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                presenter.returnAddList(task.isSuccessful());
            }
        });
    }
}
