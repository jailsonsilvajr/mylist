package com.jailson.mylist.mvps;

public interface RegisterMVP {

    interface RegisterView{

        void goViewLists(String id_user);
        void showToast(String msg);
    }

    interface RegisterPresenter{

        void doRegister(String name, String email, String passwrod);
        void goViewLists(String id_user);
    }

    interface RegisterModel{

        void doRegister(String name, String email, String password);
    }
}
