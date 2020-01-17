package com.jailson.mylist.mvps;

import java.util.List;

public interface ListMVP {

    interface ListView{

        void showToast(String msg);
        void showLists(List<com.jailson.mylist.object.List> lists);
    }

    interface ListPresenter{

        void showToast(String msg);
        void getLists(String id_user);
        void showLists(List<com.jailson.mylist.object.List> lists);
        void deleteList(int position);
    }

    interface ListModel{

        void getLists(String id_user);
        void deleteList(int position);
    }
}
