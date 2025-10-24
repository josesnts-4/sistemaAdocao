package com.joseramos.sistemaadocao.entidades;

public class Cachorro extends Animal {

    public Cachorro(String nome, String raca, String idade, String sexo, String porte, String descricao) {
        super(nome, raca, idade, sexo, porte, descricao);
    }

    @Override
    public void emitirSom() {
        System.out.println("Ruf Ruf!");
    }
}
