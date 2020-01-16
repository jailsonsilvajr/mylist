package com.jailson.mylist.mvps;

public interface LoginMVP {

    interface LoginView{

        void showToast(String msg);// P -> V
        void doLogin(String id_user);// P -> V
    }

    interface LoginPresenter{

        void doLogin(String email, String password);// V -> P
        void doLogin(String id_user);// M -> P
    }

    interface LoginModel{

        void doLogin(String email, String password);// P -> M
    }
}
