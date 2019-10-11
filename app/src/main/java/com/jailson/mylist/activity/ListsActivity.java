package com.jailson.mylist.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jailson.mylist.R;
import com.jailson.mylist.object.User;
import com.jailson.mylist.service.Service;
import com.jailson.mylist.util.AdapterLists;

import java.util.List;

public class ListsActivity extends AppCompatActivity {

    private ListView lvLists_lists;
    private List<com.jailson.mylist.object.List> lists;
    private AdapterLists adapterLists;

    private final Service service = new Service();

    private User user;

    private static final int ACTIVITY_ADDLIST_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.user = (User) getIntent().getSerializableExtra("user");

        init_views();
        get_lists();
        click_list();
    }

    private void click_list() {

        this.lvLists_lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                com.jailson.mylist.object.List list = lists.get(position);
                Intent intent = new Intent(ListsActivity.this, ItemsActivity.class);
                intent.putExtra("list", list);
                startActivity(intent);
            }
        });
    }

    private void show_lists() {

        this.adapterLists = new AdapterLists(this.lists, this);
        this.lvLists_lists.setAdapter(adapterLists);
    }

    private void get_lists() {

        new GetLists(user.getId()).execute();
    }

    private void init_views() {

        this.lvLists_lists = findViewById(R.id.lvLists_lists);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu_list) {

        getMenuInflater().inflate(R.menu.menu_list, menu_list);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menuList_add){

            click_add_list();
        }else if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void click_add_list() {

        Intent intent = new Intent(this, AddListActivity.class);
        intent.putExtra("user", this.user);
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

    private class GetLists extends AsyncTask<Void, Void, List<com.jailson.mylist.object.List> >{

        private int id_user;

        public GetLists(int id_user){

            this.id_user = id_user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<com.jailson.mylist.object.List> doInBackground(Void... voids) {

            return service.getLists(this.id_user);
        }

        @Override
        protected void onPostExecute(List<com.jailson.mylist.object.List> lists_result) {

            lists = lists_result;
            show_lists();
            super.onPostExecute(lists_result);
        }
    }
}
