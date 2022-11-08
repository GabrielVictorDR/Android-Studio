package com.example.instagram.validate;

import android.content.Context;
import android.widget.Toast;

public class LoginActivityValidate {
    
    public boolean validar(Context c, String email, String senha){
        
        if(!email.isEmpty() || email == null){
            if(!senha.isEmpty() || senha == null){
                return true;
            }else {
                Toast.makeText(c, "O Campo 'Senha' é Obrigatório.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(c, "O Campo 'E-mail' é Obrigatório.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
