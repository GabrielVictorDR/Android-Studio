package com.example.mynotes;

import android.content.Context;
import android.content.SharedPreferences;

public class AnotacaoPrefs {

    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private final String NOME_ARQUIVO = "anotacao.prefs";
    private final String CHAVE_NOME = "nome";

    public AnotacaoPrefs(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(NOME_ARQUIVO, 0);
        editor = prefs.edit();
    }

    public void salvarAnotacao(String note){
        editor.putString("nome", note);
        editor.commit();
    }

    public String recuperarAnotacao(){
        return prefs.getString(CHAVE_NOME, "");
    }

}
