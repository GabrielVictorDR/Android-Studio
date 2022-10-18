package com.example.organizze.model;

import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Movimentacao {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    /*
     *       MÉTODOS DA CLASSE
     */

    public void salvar(String dataEscolhida){
        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida);
                String idUsuario = Base64Custom.codificarBase64(mAuth.getCurrentUser().getEmail());

        mDatabase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);

    }
}
