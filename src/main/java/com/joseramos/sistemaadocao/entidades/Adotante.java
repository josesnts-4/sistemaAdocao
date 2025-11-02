package com.joseramos.sistemaadocao.entidades;

public class Adotante {

    private Integer id;
    private String nome;
    private String endereco;
    private String cpf;
    private Integer totalAdocoes;

    public Adotante() {
    }

    public Adotante(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
        this.totalAdocoes = 0;
    }

    public Adotante(String nome, String endereco, String cpf) {
        this.nome = nome;
        this.endereco = endereco;
        this.cpf = cpf;
        this.totalAdocoes = 0;
    }

    public int getTotalAdocoes() {
        return totalAdocoes;
    }

    public void setTotalAdocoes(int totalAdocoes) {
        this.totalAdocoes = totalAdocoes;
    }

    public String getNome() {
        return nome;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "===== Adotante ===== \n" +
                "nome: " + nome + "\n endereco: " + endereco +
                "\n cpf: " + cpf;
    }
}
