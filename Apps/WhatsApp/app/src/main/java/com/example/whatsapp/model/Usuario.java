package com.example.whatsapp.model;

import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario {

    private DatabaseReference mDataBase;

    private String id;
    private String nome;
    private String numero;
    private String email;
    private String senha;
    private String foto;

    public Usuario () {}

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() { return numero; }

    public void setNumero(String numero) { this.numero = numero; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    /*
    *       METODOS DA CLASSE
    */

    public void salvar(){
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("usuarios").child(id).setValue(this);
    }
}
