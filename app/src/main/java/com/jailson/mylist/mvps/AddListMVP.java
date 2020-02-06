package com.jailson.mylist.mvps;

import com.jailson.mylist.object.List;

public interface AddListMVP {

    interface AddListView{

        void returnAddList(boolean add);
    }

    interface AddListPresenter{

        void addList(List list);
        void returnAddList(boolean add);
    }

    interface AddListModel{

        void addList(List list);
    }
}
