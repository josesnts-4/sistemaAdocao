package com.joseramos.sistemaadocao.entidades;

import java.time.LocalDate;

public class Adocao {
    private Animal animal;
    private Adotante adotante;
    private LocalDate dataAdocao;

    public Adocao(Animal animal, Adotante adotante, LocalDate dataAdocao) {
        this.animal = animal;
        this.adotante = adotante;
        this.dataAdocao = dataAdocao;


        //Adiciona esta adoção ao adotante
        this.adotante.adicionarAdocao(this);
    }

    public Animal getAnimal() {
        return animal;
    }

    public Adotante getAdotante() {
        return adotante;
    }

    public LocalDate getDataAdocao() {
        return dataAdocao;
    }

    @Override
    public String toString() {
        return "===== Adocao ===== \n" +
                "animal: " + animal +
                "\n adotante: " + adotante +
                "\n dataAdocao: " + dataAdocao;
    }
}
