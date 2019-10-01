package com.jailson.mylist.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jailson.mylist.R;
import com.jailson.mylist.service.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdapterLists extends BaseAdapter {

    private List<com.jailson.mylist.object.List> lists;
    private final Activity activity;

    public AdapterLists(List<com.jailson.mylist.object.List> lists, Activity activity){

        this.lists = lists;
        this.activity = activity;
    }

    public void setList(List<com.jailson.mylist.object.List> lists){

        this.lists = lists;
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

        return this.lists.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = this.activity.getLayoutInflater().inflate(R.layout.listview_lists, parent, false);

        com.jailson.mylist.object.List list = this.lists.get(position);

        TextView textView = view.findViewById(R.id.tvLists_name);
        textView.setText(list.getName());

        ImageView img_close = view.findViewById(R.id.imgLists_delete);
        final AdapterLists adapter = this;
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Service service = new Service();
                try {

                    if(service.deleteList(lists.get(position).getId())){

                        lists.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
