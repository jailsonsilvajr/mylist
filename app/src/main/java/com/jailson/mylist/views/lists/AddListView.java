package com.jailson.mylist.views.lists;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.jailson.mylist.R;
import com.jailson.mylist.mvps.AddListMVP;
import com.jailson.mylist.object.List;
import com.jailson.mylist.presenters.lists.AddListPresenter;

public class AddListView extends AppCompatActivity implements AddListMVP.AddListView {

    private TextInputLayout textInputLayout_name;
    private Button button_add_list;

    private String id_user;
    private AddListMVP.AddListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Show button
        getSupportActionBar().setHomeButtonEnabled(true); //Activate button

        this.id_user = getIntent().getStringExtra("id_user");

        this.presenter = new AddListPresenter(this);

        init_views();
        click_button_add();
    }

    private void click_button_add() {

        this.button_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List list = new List(
                        "",
                        textInputLayout_name.getEditText().getText().toString(),
                        id_user
                );

                new AddList(list, presenter).execute();
            }
        });
    }

    private void init_views() {

        this.textInputLayout_name = findViewById(R.id.editext_add_list_name);
        this.button_add_list = findViewById(R.id.button_add_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class AddList extends AsyncTask<Void, Void, Void> {

        private List list;
        private AddListMVP.AddListPresenter presenter;

        public AddList(List list, AddListMVP.AddListPresenter presenter){

            this.list = list;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.presenter.addList(this.list);
            return null;
        }
    }


    @Override
    public void returnAddList(boolean add) {

        if(add){

            setResult(RESULT_OK);
            finish();
        }else{

            Toast.makeText(this, "Add List Fail", Toast.LENGTH_LONG).show();
        }
    }
}
