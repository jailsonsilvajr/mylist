package com.jailson.mylist.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jailson.mylist.R;
import com.jailson.mylist.service.Service;

import java.util.List;

public class AdapterLists extends BaseAdapter {

    private List<com.jailson.mylist.object.List> lists;
    private final Activity activity;

    private Service service;

    public AdapterLists(List<com.jailson.mylist.object.List> lists, Activity activity){

        this.lists = lists;
        this.activity = activity;
        this.service = new Service();
    }

    @Override
    public int getCount() {

        return this.lists.size();
    }

    @Override
    public Object getItem(int position) {

        return this.lists.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = this.activity.getLayoutInflater().inflate(R.layout.listview_lists, parent, false);

        com.jailson.mylist.object.List list = this.lists.get(position);

        TextView textView = view.findViewById(R.id.tvLists_name);
        textView.setText(list.getName());

        ImageView img_close = view.findViewById(R.id.imgLists_delete);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DeleteList(position).execute();
            }
        });

        return view;
    }

    private class DeleteList extends AsyncTask<Void, Void, Boolean>{

        private int position;

        public DeleteList(int position){

            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

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

            db.collection("lists").document(lists.get(position).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        lists.remove(position);
                        AdapterLists adapterLists = AdapterLists.this;
                        adapterLists.notifyDataSetChanged();
                    }else{


                    }
                }
            });
            return null;
        }
    }
}
