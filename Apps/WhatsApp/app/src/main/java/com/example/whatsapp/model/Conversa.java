package com.example.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Conversa {

    private String idUsuario;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario usuario_da_conversa;
    private String isGroup;
    private Grupo grupo;

    public Conversa() {
        this.setIsGroup("false");
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public Usuario getUsuario_da_conversa() {
        return usuario_da_conversa;
    }

    public void setUsuario_da_conversa(Usuario usuario_da_conversa) { this.usuario_da_conversa = usuario_da_conversa; }

    public String getIsGroup() { return isGroup; }

    public void setIsGroup(String isGroup) { this.isGroup = isGroup; }

    public Grupo getGrupo() { return grupo; }

    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    /*****************/

    public void salvar(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference conversaRef = database.child("conversas");
        conversaRef.child(this.getIdUsuario())
                .child(this.getIdDestinatario())
                .setValue(this);
    }
}
