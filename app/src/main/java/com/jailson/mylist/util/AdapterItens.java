package com.jailson.mylist.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jailson.mylist.R;
import com.jailson.mylist.object.Item;

import java.util.List;

public class AdapterItens extends BaseAdapter {

    private List<Item> itens;
    private final Activity activity;

    public AdapterItens(List<Item> itens, Activity activity){

        this.itens = itens;
        this.activity = activity;
    }

    @Override
    public int getCount() {

        return this.itens.size();
    }

    @Override
    public Object getItem(int position) {

        return this.itens.get(position);
    }

    @Override
    public long getItemId(int position) {

        return this.itens.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = this.activity.getLayoutInflater().inflate(R.layout.listview_iten, parent, false);

        Item iten = this.itens.get(position);

        ImageView img = view.findViewById(R.id.imageview_iten);
        TextView name = view.findViewById(R.id.textview_name_iten);
        TextView mark = view.findViewById(R.id.textview_mark_iten);
        TextView price = view.findViewById(R.id.textview_price_iten);
        TextView qtd = view.findViewById(R.id.textview_qtd_iten);

        name.setText(name.getText() + iten.getName());
        mark.setText(mark.getText() + iten.getMark());
        price.setText(price.getText() + "" + iten.getPrice());
        qtd.setText(qtd.getText() + "" + iten.getQtd());

        //Picasso.get().load(iten.getUrl_img()).into(img);

        return view;
    }
}