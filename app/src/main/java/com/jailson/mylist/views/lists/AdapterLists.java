package com.jailson.mylist.views.lists;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jailson.mylist.R;
import com.jailson.mylist.mvps.ListMVP;

import java.util.List;

public class AdapterLists extends BaseAdapter {

    private List<com.jailson.mylist.object.List> lists;
    private final Activity activity;
    private ListMVP.ListPresenter presenter;

    public AdapterLists(List<com.jailson.mylist.object.List> lists, Activity activity, ListMVP.ListPresenter presenter){

        this.lists = lists;
        this.activity = activity;
        this.presenter = presenter;
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

                new DeleteList(position, presenter).execute();
            }
        });

        return view;
    }

    private class DeleteList extends AsyncTask<Void, Void, Void>{

        private int position;
        private ListMVP.ListPresenter presenter;

        public DeleteList(int position, ListMVP.ListPresenter presenter){

            this.position = position;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            this.presenter.deleteList(this.position);
            return null;
        }
    }
}
