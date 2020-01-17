package com.jailson.mylist.views.lists;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jailson.mylist.R;
import com.jailson.mylist.activity.AddListActivity;
import com.jailson.mylist.activity.ItemsActivity;
import com.jailson.mylist.mvps.ListMVP;
import com.jailson.mylist.object.List;
import com.jailson.mylist.presenters.lists.ListsPresenter;
import com.jailson.mylist.views.login.LoginView;

public class ListsView extends AppCompatActivity implements ListMVP.ListView {

    private ListMVP.ListPresenter presenter;

    private ListView lvLists_lists;
    private FloatingActionButton fabLists;
    private java.util.List<List> lists;
    private AdapterLists adapterLists;

    private static final int ACTIVITY_ADDLIST_REQUEST = 1;

    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        this.id_user = getIntent().getStringExtra("id_user");
        this.presenter = new ListsPresenter(this);

        init_views();
        get_lists();
        clicks();
    }

    @Override
    public void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLists(java.util.List<List> lists) {

        this.lists = lists;
        if(this.adapterLists != null) this.adapterLists.notifyDataSetChanged();
        else{

            this.adapterLists = new AdapterLists(this.lists, this, this.presenter);
            this.lvLists_lists.setAdapter(adapterLists);
        }
    }

    private void clicks() {

        this.lvLists_lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                com.jailson.mylist.object.List list = lists.get(position);
                Intent intent = new Intent(ListsView.this, ItemsActivity.class);
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

    private void click_add_list() {

        Intent intent = new Intent(this, AddListActivity.class);
        intent.putExtra("id_user", this.id_user);
        startActivityForResult(intent, ACTIVITY_ADDLIST_REQUEST);
    }

    private void get_lists() {

        new GetLists(this.id_user).execute();
    }

    private void init_views() {

        this.lvLists_lists = findViewById(R.id.lvLists_lists);
        this.fabLists = findViewById(R.id.fabLists);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == ACTIVITY_ADDLIST_REQUEST){
            if(resultCode == RESULT_OK){

                get_lists();
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

            Intent intent = new Intent(this, LoginView.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetLists extends AsyncTask<Void, Void, Void> {

        private String id_user;

        public GetLists(String id_user){

            this.id_user = id_user;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            presenter.getLists(this.id_user);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }
}
