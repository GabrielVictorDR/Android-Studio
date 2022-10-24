package com.example.whatsapp.model;

public class Conversa {

    private String idUsuario;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario usuario_da_conversa;

    public Conversa() {
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

    public void setUsuario_da_conversa(Usuario usuario_da_conversa) {
        this.usuario_da_conversa = usuario_da_conversa;
    }
}
