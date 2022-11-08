package com.example.instagram.validate;

import android.content.Context;
import android.widget.Toast;

public class CadastroActivityValidate {

    public boolean validar(Context c, String nome, String email, String senha){

        if(!nome.isEmpty() || nome == null){
            if (!email.isEmpty() || email == null){
                if(!senha.isEmpty() || senha == null){
                    return true;
                }else {
                    Toast.makeText(c, "O Campo 'Senha' é Obrigatório.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(c, "O Campo 'E-Mail' é Obrigatório.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(c, "O Campo 'Nome' é Obrigatório.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}
