package com.example.whatsapp.model;

import com.example.whatsapp.helper.Base64Custom;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable{

    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference grupoRef = databaseReference.child("grupos");

        String idGrupoFirebase = grupoRef.push().getKey();
        setId( idGrupoFirebase );

    }

    public void salvar(){

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference grupoRef = database.child("grupos");

        grupoRef.child( getId() ).setValue( this );

        //salvar conversa para membros do grupo
        for (Usuario membro: getMembros() ){

            String idRemetente = Base64Custom.codificarBase64(membro.getNumero().replace(" ", "") + "@phone.com");
            String idDestinatario = getId();

            Conversa conversa = new Conversa();
            conversa.setIdUsuario( idRemetente );
            conversa.setIdDestinatario( idDestinatario );
            conversa.setUltimaMensagem("Toque para interagir com seu grupo");
            conversa.setIsGroup("true");
            conversa.setGrupo( this );

            conversa.salvar();

        }

    }

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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}
