package com.joseramos.sistemaadocao.entidades;

import com.joseramos.sistemaadocao.interfaces.CuidadosEspeciais;
import jakarta.persistence.*;
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Animal implements CuidadosEspeciais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String raca;
    private Integer idade;
    private String sexo;
    private String descricao;
    private String tipo;
    private StatusAnimal status;

    public Animal() {
    }

    public Animal(String nome, String raca, Integer idade, String sexo, String descricao, String tipo) {
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.sexo = sexo;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public StatusAnimal getStatus() {
        return status;
    }

    public void setStatus(StatusAnimal status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

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

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public abstract void emitirSom();

    @Override
    public void vacinar() {
        System.out.println("Animal vacinado!");
    }

    @Override
    public void vermifugar() {
        System.out.println("Animal vermifugado!");
    }
}
