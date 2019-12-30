package com.jailson.mylist.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireConnection {

    private static FirebaseAuth firebaseAuth;

    private FireConnection(){}

    public static FirebaseAuth getFirebaseAuth(){

        if(firebaseAuth == null){

            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static FirebaseUser getFirebaseUser(){

        return firebaseAuth.getCurrentUser();
    }

    public static void logOut(){

        firebaseAuth.signOut();
    }
}
