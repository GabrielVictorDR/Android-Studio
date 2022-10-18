package com.example.organizze.model;

import com.example.organizze.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario {

    private DatabaseReference mDatabase;

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Double receitaTotal = 0.0;
    private Double despesaTotal = 0.0;

    public Usuario() {
        /*
        *   CONSTRUTOR VAZIO
        */
    }

    @Exclude
    public String getIdUsuario() { return idUsuario; }

    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    @Exclude
    public String getSenha() { return senha; }

    public void setSenha(String senha) { this.senha = senha; }

    public Double getReceitaTotal() { return receitaTotal; }

    public void setReceitaTotal(Double receitaTotal) { this.receitaTotal = receitaTotal; }

    public Double getDespesaTotal() { return despesaTotal; }

    public void setDespesaTotal(Double despesaTotal) { this.despesaTotal = despesaTotal; }

    /*
    *       MÉTODOS DA CLASSE
    */

    public void salvar(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("usuarios").child(idUsuario).setValue(this);
    }

}
