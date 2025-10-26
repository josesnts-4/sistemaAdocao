package com.joseramos.sistemaadocao.entidades;

import jakarta.persistence.*;

public class Gato extends Animal {
    private String peso;
    private Boolean arisco;

    public Gato(String nome, String raca, Integer idade, String sexo, String descricao, String pesoGato, Boolean arisco) {
        super(nome, raca, idade, sexo, descricao, "Gato");
        this.peso = pesoGato;
        this.arisco = arisco;
    }

    public Boolean getArisco() {
        return arisco;
    }

    public void setArisco(Boolean arisco) {
        this.arisco = arisco;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    @Override
    public void emitirSom() {
        System.out.println("Miau!");
    }
}
