package com.joseramos.sistemaadocao.entidades;

public abstract class Animal {
    private String nome;
    private String raca;
    private String idade;
    private String sexo;
    private String porte;
    private String descricao;
    private String disponivel;

    public Animal(String nome, String raca, String idade, String sexo, String porte, String descricao, String disponivel) {
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.sexo = sexo;
        this.porte = porte;
        this.descricao = descricao;
        this.disponivel = disponivel;
    }

    public abstract void emitirSom();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(String disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return "Animal:\n nome=" + nome + ", raca=" + raca + ", idade=" + idade + ", sexo=" + sexo + ", porte=" + porte + ", descricao=" + descricao + ", disponivel=" + disponivel;
    }
}
