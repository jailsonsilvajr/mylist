package com.jailson.mylist.mvps;

import com.jailson.mylist.object.Item;

public interface AddItemMVP {

    interface AddItemView{

        void returnAddItem(boolean add);
    }

    interface AddItemPresenter{

        void addItem(Item item);
        void returnAddItem(boolean add);
    }

    interface  AddItemModel{

        void addItem(Item item);
    }
}
