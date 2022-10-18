package com.example.tarefasapp.model;

import java.io.Serializable;

public class Tarefa implements Serializable {
    private Long id;
    private String nome;

    public Tarefa () {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
