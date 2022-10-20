package com.example.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference mDataBase;

    public static FirebaseAuth getFirebaseAuth(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }

        return  autenticacao;
    }

    public static DatabaseReference getDatabaseRef(){

        mDataBase = FirebaseDatabase.getInstance().getReference();
        return  mDataBase;

    }

}
