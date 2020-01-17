package com.jailson.mylist.models.lists;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jailson.mylist.mvps.ListMVP;
import com.jailson.mylist.object.List;

import java.util.ArrayList;

public class ListsModel implements ListMVP.ListModel {

    private ListMVP.ListPresenter presenter;
    private java.util.List<List> lists;

    public ListsModel(ListMVP.ListPresenter presenter){

        this.presenter = presenter;
        this.lists = new ArrayList<>();
    }

    @Override
    public void getLists(String id_user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference listsRef = db.collection("lists");
        Query query = listsRef.whereEqualTo("id_user", id_user);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    lists.clear();
                    for(QueryDocumentSnapshot document : task.getResult()){

                        com.jailson.mylist.object.List list = new com.jailson.mylist.object.List(
                                document.getId(),
                                document.get("name").toString(),
                                document.get("id_user").toString()
                        );
                        lists.add(list);
                    }
                    presenter.showLists(lists);
                }else{

                    presenter.showToast("Não foi possível obter as listas");
                }
            }
        });
    }

    @Override
    public void deleteList(final int position) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Deletar os items da lista:
        db.collection("items").whereEqualTo("id_list", lists.get(position).getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            for(DocumentSnapshot document : task.getResult()){

                                db.collection("items").document(document.getId()).delete();
                            }
                        }
                    }
                });
        //Deletar a lista:
        db.collection("lists").document(lists.get(position).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    lists.remove(position);
                    presenter.showLists(lists);
                }else{

                    presenter.showToast("Não foi possível deletar a lista " + lists.get(position).getName());
                }
            }
        });
    }
}
