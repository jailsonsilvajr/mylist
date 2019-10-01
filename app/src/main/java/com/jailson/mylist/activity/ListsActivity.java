package com.jailson.mylist.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.concurrent.ExecutionException;

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

        this.user = (User) getIntent().getSerializableExtra("user");

        init_views();
        get_lists();
        show_lists();
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

        try {

            this.lists = this.service.getLists(user.getId());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
}
