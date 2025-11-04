package com.joseramos.sistemaadocao.entidades;

public class Cachorro extends Animal {

    public Cachorro() {
    }

    public Cachorro(String nome, String raca, Integer idade) {
        super(nome, raca, idade);
    }

    @Override
    public void emitirSom() {
        System.out.println("Ruf Ruf!");
    }

    @Override
    public void vacinar() {
        System.out.println("O cachorro " + getNome() + " recebeu a vacina V10 (polivalente).");
    }
}