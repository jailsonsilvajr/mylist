package com.jailson.mylist.presenters.register;

import androidx.appcompat.app.AppCompatActivity;

import com.jailson.mylist.models.register.RegisterModel;
import com.jailson.mylist.mvps.RegisterMVP;

public class RegisterPresenter extends AppCompatActivity implements RegisterMVP.RegisterPresenter {

    private RegisterMVP.RegisterView view;
    private RegisterMVP.RegisterModel model;

    public RegisterPresenter(RegisterMVP.RegisterView view){

        this.view = view;
        this.model = new RegisterModel(this);
    }

    @Override
    public void doRegister(String name, String email, String passwrod) {

        this.model.doRegister(name, email, passwrod);
    }

    @Override
    public void goViewLists(String id_user) {

        if(id_user != null) this.view.goViewLists(id_user);
        else this.view.showToast("Register Fail!");
    }
}
