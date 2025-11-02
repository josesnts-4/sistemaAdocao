package com.joseramos.sistemaadocao.entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Adocao {
    private Animal animal;
    private Adotante adotante;
    private LocalDate dataAdocao;
    private Integer id;

    public Adocao(Animal animal, Adotante adotante, LocalDate dataAdocao) {
        this.animal = animal;
        this.adotante = adotante;
        this.dataAdocao = dataAdocao;
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = dataAdocao.format(formatter);
        return "===== Registro de Adoção ===== \n" + "Data: " + dataFormatada + "\n" +
                "Adotante: " + adotante.getNome() + "(CPF: " + adotante.getCpf() +
                "Animal: " + animal.getNome() + "\n" + "(Tipo: " + animal.getClass().getSimpleName() + ")";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}
