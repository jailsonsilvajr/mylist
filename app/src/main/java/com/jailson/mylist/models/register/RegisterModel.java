package com.jailson.mylist.models.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jailson.mylist.firebase.FireConnection;
import com.jailson.mylist.mvps.RegisterMVP;

import java.util.HashMap;
import java.util.Map;

public class RegisterModel extends AppCompatActivity implements RegisterMVP.RegisterModel {

    private RegisterMVP.RegisterPresenter presenter;

    public RegisterModel(RegisterMVP.RegisterPresenter presenter){

        this.presenter = presenter;
    }

    @Override
    public void doRegister(final String name, String email, String password) {

        final FirebaseAuth firebaseAuth = FireConnection.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, String> dataObj = new HashMap<>();
                            dataObj.put("name", name);
                            String id_user = firebaseAuth.getCurrentUser().getUid();

                            db.collection("users").document(id_user).set(dataObj);

                            presenter.goViewLists(id_user);
                        }else{

                            presenter.goViewLists(null);
                        }
                    }
                });
    }
}
