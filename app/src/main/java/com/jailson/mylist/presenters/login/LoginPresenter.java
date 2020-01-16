package com.jailson.mylist.presenters.login;

import com.jailson.mylist.models.login.LoginModel;
import com.jailson.mylist.mvps.LoginMVP;

public class LoginPresenter implements LoginMVP.LoginPresenter {

    private LoginMVP.LoginView view;
    private LoginMVP.LoginModel model;

    public LoginPresenter(LoginMVP.LoginView view){

        this.view = view;
        this.model = new LoginModel(this);
    }

    @Override
    public void doLogin(String email, String password) {

        this.model.doLogin(email, password);
    }

    @Override
    public void doLogin(String id_user) {

        if(id_user != null){

            this.view.doLogin(id_user);
        }else{

            this.view.showToast("Login Fail!");
        }
    }
}
