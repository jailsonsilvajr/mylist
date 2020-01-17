package com.jailson.mylist.presenters.lists;

import com.jailson.mylist.models.lists.ListsModel;
import com.jailson.mylist.mvps.ListMVP;
import com.jailson.mylist.object.List;

public class ListsPresenter implements ListMVP.ListPresenter {

    private ListMVP.ListView view;
    private ListMVP.ListModel model;

    public ListsPresenter(ListMVP.ListView view){

        this.view = view;
        this.model = new ListsModel(this);
    }

    @Override
    public void showToast(String msg) {

        this.view.showToast(msg);
    }

    @Override
    public void getLists(String id_user) {

        this.model.getLists(id_user);
    }

    @Override
    public void showLists(java.util.List<List> lists) {

        this.view.showLists(lists);
    }

    @Override
    public void deleteList(int position) {

        this.model.deleteList(position);
    }
}
