package com.jailson.mylist.mvps;

import com.jailson.mylist.object.Item;

import java.util.List;

public interface ItemMVP {

    interface ItemView{

        void returnGetItems(List<Item> items);
        void returnDelete(boolean delete);
        void returnInsertIntoCart(boolean insert);
    }

    interface ItemPresenter{

        void getItems(String id_list);
        void deleteItem(String id_item);
        void insertInCart(String id_item, int insert);

        void returnGetItems(List<Item> items);
        void returnDelete(boolean delete);
        void returnInsertIntoCart(boolean insert);
    }

    interface ItemModel{

        void getItems(String id_list);
        void deleteItem(String id_item);
        void insertInCart(String id_item, int insert);
    }
}
