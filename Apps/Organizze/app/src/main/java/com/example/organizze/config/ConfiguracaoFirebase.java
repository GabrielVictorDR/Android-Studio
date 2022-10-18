package com.example.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

//    public static DatabaseReference getFirebase(){
//        firebase = FirebaseDatabase.getInstance().getReference();
//        return firebase;
//    }

    public static FirebaseAuth getFirebaseAutenticacao(){

        if( autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }
}
