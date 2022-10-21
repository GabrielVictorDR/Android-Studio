package com.example.whatsapp.model;

import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private DatabaseReference mDataBase;

    private String id;
    private String nome;
    private String numero;
    private String email;
    private String senha;
    private String foto;

    public Usuario() {}

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Exclude
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Exclude
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

    /*
    *       METODOS DA CLASSE
    */

    public void salvar(){
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("usuarios").child(id).setValue(this);
    }

    // Atualizar Dados

    public void atualizar(){
        String idUsuario = UsuarioFirebase.getItentificadorUsuario();
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(idUsuario);

        Map<String, Object> valores = converterParaMap();

        usuarioRef.updateChildren( valores );
    }

    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("numero", getNumero());
        usuarioMap.put("foto", getFoto());

        return usuarioMap;
    }

    // Fim atualizar Dados

}
