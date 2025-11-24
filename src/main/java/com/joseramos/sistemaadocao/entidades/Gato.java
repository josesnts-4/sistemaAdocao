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
        super.vacinar();
        System.out.println("O gato " + getNomeAnimal() + " recebeu a vacina V4 (quíntupla felina).");
    }
    @Override
    public void vermifugar() {
        super.vermifugar();
        System.out.println("O gato " + getNomeAnimal() + " recebeu vermifugos.");
    }
}
