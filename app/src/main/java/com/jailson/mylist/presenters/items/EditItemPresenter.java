package com.jailson.mylist.presenters.items;

import com.jailson.mylist.models.items.EditItemModel;
import com.jailson.mylist.mvps.EditItemMVP;
import com.jailson.mylist.object.Item;

public class EditItemPresenter implements EditItemMVP.EditItemPresenter {

    private EditItemMVP.EditItemView view;
    private EditItemMVP.EditItemModel model;

    public EditItemPresenter(EditItemMVP.EditItemView view){

        this.view = view;
        this.model = new EditItemModel(this);
    }


    @Override
    public void editItem(Item item) {

        this.model.editItem(item);
    }

    @Override
    public void returnEditItem(boolean edit) {

        this.view.returnEditItem(edit);
    }
}
