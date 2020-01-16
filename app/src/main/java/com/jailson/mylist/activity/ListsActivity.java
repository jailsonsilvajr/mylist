package com.jailson.mylist.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jailson.mylist.R;
import com.jailson.mylist.util.AdapterLists;
import com.jailson.mylist.views.login.LoginView;

import java.util.ArrayList;
import java.util.List;

public class ListsActivity extends AppCompatActivity {

    private ListView lvLists_lists;
    private FloatingActionButton fabLists;
    private List<com.jailson.mylist.object.List> lists;
    private AdapterLists adapterLists;

    private static final int ACTIVITY_ADDLIST_REQUEST = 1;

    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        this.id_user = getIntent().getStringExtra("id_user");
        get_profile(this.id_user);

        init_views();
        get_lists();
        clicks();
    }

    private void get_profile(String id_user) {

        new GetProfile(id_user).execute();
    }

    private void clicks() {

        this.lvLists_lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                com.jailson.mylist.object.List list = lists.get(position);
                Intent intent = new Intent(ListsActivity.this, ItemsActivity.class);
                intent.putExtra("list", list);
                startActivity(intent);
            }
        });

        this.fabLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click_add_list();
            }
        });
    }

    private void show_lists() {

        this.adapterLists = new AdapterLists(this.lists, this);
        this.lvLists_lists.setAdapter(adapterLists);
    }

    private void get_lists() {

        new GetLists(this.id_user).execute();
    }

    private void init_views() {

        this.lvLists_lists = findViewById(R.id.lvLists_lists);
        this.fabLists = findViewById(R.id.fabLists);
    }

    private void click_add_list() {

        Intent intent = new Intent(this, AddListActivity.class);
        intent.putExtra("id_user", this.id_user);
        startActivityForResult(intent, ACTIVITY_ADDLIST_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == ACTIVITY_ADDLIST_REQUEST){
            if( resultCode == RESULT_OK){

                get_lists();
                show_lists();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.menuList_logout){

            SharedPreferences sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit().remove("id");
            editor.apply();

            sharedPreferences = getSharedPreferences("name", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit().remove("name");
            editor.apply();

            sharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit().remove("email");
            editor.apply();

            Intent intent = new Intent(ListsActivity.this, LoginView.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetLists extends AsyncTask<Void, Void, List<com.jailson.mylist.object.List> >{

        private String id_user;

        public GetLists(String id_user){

            this.id_user = id_user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<com.jailson.mylist.object.List> doInBackground(Void... voids) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference listsRef = db.collection("lists");
            Query query = listsRef.whereEqualTo("id_user", this.id_user);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){

                        List<com.jailson.mylist.object.List> list_temp = new ArrayList<>();
                        for(QueryDocumentSnapshot document : task.getResult()){

                            com.jailson.mylist.object.List list = new com.jailson.mylist.object.List(
                                    document.getId(),
                                    document.get("name").toString(),
                                    document.get("id_user").toString()
                            );
                            list_temp.add(list);
                        }
                        lists = list_temp;
                        if(lists.size() > 0) show_lists();
                    }else{

                        Toast.makeText(ListsActivity.this, "Não foi possível obter as listas.", Toast.LENGTH_LONG).show();
                    }
                }
            });

            return null;
        }
    }

    private class GetProfile extends AsyncTask<Void, Void, String>{

        private String id;

        public GetProfile(String id){

            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        @Override
        protected String doInBackground(Void... voids) {


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("users").document(getId());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){

                            Toast.makeText(ListsActivity.this, "Welcome, " + document.get("name").toString(), Toast.LENGTH_LONG).show();
                        }else{

                            Toast.makeText(ListsActivity.this, "Welcome, Anônimo", Toast.LENGTH_LONG).show();
                        }
                    }else{

                        Log.i("GetName", "Exception: " + task.getException());
                    }
                }
            });

            return null;
        }
    }
}
