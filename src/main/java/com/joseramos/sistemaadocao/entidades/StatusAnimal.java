package com.joseramos.sistemaadocao.entidades;

public enum StatusAnimal {
DISPONIVEL("Disponvel"),
    ADOTADO("Adotado");

    private final String descricao;

    StatusAnimal(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
    @Override
    public String toString() {
        return descricao;
    }
}
