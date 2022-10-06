package com.example.instacard.model;

public class Postagem {
    private String usuario_main;
    private String desc_postagem;
    private int postagem;
    private int usuario_perfil;

    public Postagem() {}

    public Postagem(String usuario_main, String desc_postagem, int postagem, int usuario_perfil) {
        this.usuario_main = usuario_main;
        this.desc_postagem = desc_postagem;
        this.postagem = postagem;
        this.usuario_perfil = usuario_perfil;
    }

    public String getUsuario_main() {
        return usuario_main;
    }

    public void setUsuario_main(String usuario_main) {
        this.usuario_main = usuario_main;
    }

    public String getDesc_postagem() {
        return desc_postagem;
    }

    public void setDesc_postagem(String desc_postagem) {
        this.desc_postagem = desc_postagem;
    }

    public int getPostagem() {
        return postagem;
    }

    public void setPostagem(int postagem) {
        this.postagem = postagem;
    }

    public int getUsuario_perfil() {
        return usuario_perfil;
    }

    public void setUsuario_perfil(int usuario_perfil) {
        this.usuario_perfil = usuario_perfil;
    }
}
