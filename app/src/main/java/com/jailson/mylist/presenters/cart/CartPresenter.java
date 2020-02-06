package com.jailson.mylist.presenters.cart;

import com.jailson.mylist.models.cart.CartModel;
import com.jailson.mylist.mvps.CartMVP;
import com.jailson.mylist.object.Item;

import java.util.List;

public class CartPresenter implements CartMVP.CartPresenter {

    private CartMVP.CartView view;
    private CartMVP.CartModel model;

    public CartPresenter(CartMVP.CartView view){

        this.view = view;
        this.model = new CartModel(this);
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
    public void removeIntoCart(String id_item) {

        this.model.removeIntoCart(id_item);
    }

    @Override
    public void returnGetItems(List<Item> items) {

        this.view.returnGetItems(items);
    }

    @Override
    public void returnDeleteItem(boolean delete) {

        this.view.returnDeleteItem(delete);
    }

    @Override
    public void returnRemoveIntoCart(boolean remove) {

        this.view.returnRemoveIntoCart(remove);
    }
}
