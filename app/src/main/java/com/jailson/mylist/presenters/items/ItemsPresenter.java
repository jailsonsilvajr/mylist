package com.jailson.mylist.presenters.items;

import com.jailson.mylist.models.items.ItemsModel;
import com.jailson.mylist.mvps.ItemMVP;
import com.jailson.mylist.object.Item;

import java.util.List;

public class ItemsPresenter implements ItemMVP.ItemPresenter {

    private ItemMVP.ItemView view;
    private ItemMVP.ItemModel model;

    public ItemsPresenter(ItemMVP.ItemView view){

        this.view = view;
        this.model = new ItemsModel(this);
    }

    @Override
    public void getItems(String id_list) {

        this.model.getItems(id_list);
    }

    @Override
    public void deleteItem(String id_item) {

        this.model.deleteItem(id_item);
    }

    @Override
    public void insertInCart(String id_item, int insert) {

        this.model.insertInCart(id_item, insert);
    }

    @Override
    public void returnGetItems(List<Item> items) {

        this.view.returnGetItems(items);
    }

    @Override
    public void returnDelete(boolean delete) {

        this.view.returnDelete(delete);
    }

    @Override
    public void returnInsertIntoCart(boolean insert) {

        this.view.returnInsertIntoCart(insert);
    }
}
