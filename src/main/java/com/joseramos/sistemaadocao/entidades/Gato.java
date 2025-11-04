package com.joseramos.sistemaadocao.entidades;

public class Gato extends Animal {

    public Gato() {
    }

    public Gato(String nome, String raca, Integer idade) {
        super(nome, raca, idade);
    }

    @Override
    public void emitirSom() {
        System.out.println("Miau!");
    }
    @Override
    public void vacinar() {
        System.out.println("O gato " + getNome() + " recebeu a vacina V4 (quíntupla felina).");
    }
}
