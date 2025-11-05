package com.joseramos.sistemaadocao.entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Adocao {
    private Animal animal;
    private Adotante adotante;
    private LocalDate dataAdocao;
    private Integer id;
    private Adocao animalAdotado;

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
                "Animal: " + animal.getNomeAnimal() + "\n" + "(Tipo: " + animal.getClass().getSimpleName() + ")";
    }
    // Coloque estes métodos dentro da sua classe Adocao.java

    /**
     * Helper para a Tabela de Adoções
     * Chamado pelo PropertyValueFactory("dataAdocaoFormatada")
     */
    public String getDataAdocaoFormatada() {
        if (this.dataAdocao == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataAdocao.format(formatter);
    }

    /**
     * Helper para a Tabela de Adoções
     * Chamado pelo PropertyValueFactory("nomeAnimal")
     */
    // ... dentro da classe Adocao.java ...

    // Helper para a Tabela de Adoções
    public String getNomeAnimal() {
        if (this.animalAdotado == null) {
            return getNome(); // Texto alternativo
        }
        return this.animalAdotado.getNome();
    }

    // Helper para a Tabela de Adoções
    public String getNome() {
        if (this.adotante == null) {
            return "[Adotante Removido]"; // Texto alternativo
        }
        return this.adotante.getNome();
    }

    /**
     * Helper para a Tabela de Adoções
     * Chamado pelo PropertyValueFactory("nomeAdotante")
     */
    public String getNomeAdotante() {
        return this.adotante.getNome();
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
