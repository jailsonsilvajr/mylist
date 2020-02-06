package com.jailson.mylist.mvps;

import com.jailson.mylist.object.Item;

import java.util.List;

public interface CartMVP {

    interface CartView{

        void returnGetItems(List<Item> items);
        void returnDeleteItem(boolean delete);
        void returnRemoveIntoCart(boolean remove);
    }

    interface CartPresenter{

        void getItems(String id_list);
        void deleteItem(String id_item);
        void removeIntoCart(String id_item);
        void returnGetItems(List<Item> items);
        void returnDeleteItem(boolean delete);
        void returnRemoveIntoCart(boolean remove);
    }

    interface CartModel{

        void getItems(String id_list);
        void deleteItem(String id_item);
        void removeIntoCart(String id_item);
    }
}
