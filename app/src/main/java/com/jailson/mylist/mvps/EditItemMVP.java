package com.jailson.mylist.mvps;

import com.jailson.mylist.object.Item;

public interface EditItemMVP {

    interface EditItemView{

        void returnEditItem(boolean edit);
    }

    interface EditItemPresenter{

        void editItem(Item item);
        void returnEditItem(boolean edit);
    }

    interface EditItemModel{

        void editItem(Item item);
    }
}
