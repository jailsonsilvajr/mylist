package com.jailson.mylist.presenters.lists;

import com.jailson.mylist.models.lists.AddListModel;
import com.jailson.mylist.mvps.AddListMVP;
import com.jailson.mylist.object.List;

public class AddListPresenter implements AddListMVP.AddListPresenter {

    private AddListMVP.AddListView view;
    private AddListMVP.AddListModel model;

    public AddListPresenter(AddListMVP.AddListView view){

        this.view = view;
        this.model = new AddListModel(this);
    }

    @Override
    public void addList(List list) {

        this.model.addList(list);
    }

    @Override
    public void returnAddList(boolean add) {

        this.view.returnAddList(add);
    }
}
