package com.joseramos.sistemaadocao.entidades;

import java.util.*;

public class Adotante {
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private String cpf;
    private List<Adocao> adocoes;

    public Adotante(List<Adocao> adocoes) {
        this.adocoes = adocoes;
    }

    public Adotante(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public Adotante(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
    public void adicionarAdocao(Adocao adocao) {
        adocoes.add(adocao);
    }

    public int contarAnimaisAdotados() {
        return adocoes.size();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public List<Adocao> getAdocoes() {
        return adocoes;
    }

    public void setAdocoes(List<Adocao> adocoes) {
        this.adocoes = adocoes;
    }

    @Override
    public String toString() {
        return "===== Adotante ===== \n" +
                "nome: " + nome +
                "\n email: " + email +
                "\n telefone: " + telefone +
                "\n endereco: " + endereco +
                "\n cpf: " + cpf +
                "\n adocoes: " + adocoes;
    }
}
