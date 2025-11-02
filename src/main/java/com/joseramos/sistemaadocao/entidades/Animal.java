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
    private Integer idade;;
    private StatusAnimal status;

    public Animal() {
    }

    public Animal(String nome, String raca, Integer idade) {
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatusAnimal getStatus() {
        return status;
    }

    public void setStatus(StatusAnimal status) {
        this.status = status;
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
