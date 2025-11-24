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
        super.vacinar();
        System.out.println("O cachorro " + getNomeAnimal() + " recebeu a vacina V10 (polivalente).");
    }

    @Override
    public void vermifugar() {
        super.vermifugar();
        System.out.println("O cachorro " + getNomeAnimal() + " recebeu vermifugos.");
    }

}