package com.jailson.mylist.presenters.items;

import com.jailson.mylist.models.items.AddItemModel;
import com.jailson.mylist.mvps.AddItemMVP;
import com.jailson.mylist.object.Item;

public class AddItemPresenter implements AddItemMVP.AddItemPresenter {

    private AddItemMVP.AddItemView view;
    private AddItemMVP.AddItemModel model;

    public AddItemPresenter(AddItemMVP.AddItemView view){

        this.view = view;
        this.model = new AddItemModel(this);
    }

    @Override
    public void addItem(Item item) {

        this.model.addItem(item);
    }

    @Override
    public void returnAddItem(boolean add) {

        this.view.returnAddItem(add);
    }
}
