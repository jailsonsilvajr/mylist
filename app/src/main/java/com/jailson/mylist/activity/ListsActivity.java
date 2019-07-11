package com.jailson.mylist.activity;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        //getSupportActionBar().setTitle("");

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickBtnAddList();
            }
        });

        this.user = (User) getIntent().getSerializableExtra("user");

        try {

            this.lists = this.service.getLists(user.getId());
            //this.lists = this.service.getLists(2);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.lvLists_lists = findViewById(R.id.lvLists_lists);
        this.adapterLists = new AdapterLists(this.lists, this);
        this.lvLists_lists.setAdapter(adapterLists);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menuList_add){

            clickBtnAddList();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void clickBtnAddList(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.list_add_popup);

        ImageView imageView_close = dialog.findViewById(R.id.imgList_close);
        final EditText editText_name = dialog.findViewById(R.id.etList_name);
        Button btn_add = dialog.findViewById(R.id.btnList_add);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if(service.addList(editText_name.getText().toString(), user.getId())){

                        lists = service.getLists(user.getId());
                        adapterLists.setList(lists);
                        lvLists_lists.setAdapter(adapterLists);
                        dialog.dismiss();
                    }else{

                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }
}
