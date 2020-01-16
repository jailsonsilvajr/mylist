package com.jailson.mylist.models.login;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jailson.mylist.firebase.FireConnection;
import com.jailson.mylist.mvps.LoginMVP;

public class LoginModel implements LoginMVP.LoginModel {

    private LoginMVP.LoginPresenter presenter;

    public LoginModel(LoginMVP.LoginPresenter presenter){

        this.presenter = presenter;
    }

    @Override
    public void doLogin(String email, String password) {

        final FirebaseAuth firebaseAuth = FireConnection.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            presenter.doLogin(firebaseAuth.getCurrentUser().getUid());
                        }else{

                            presenter.doLogin(null);
                        }
                    }
                });
    }
}