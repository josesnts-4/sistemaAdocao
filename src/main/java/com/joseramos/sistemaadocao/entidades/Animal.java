package com.joseramos.sistemaadocao.entidades;

import com.joseramos.sistemaadocao.interfaces.CuidadosEspeciais;

public abstract class Animal implements CuidadosEspeciais {

    private Integer id;
    private String nomeAnimal;
    private String raca;
    private Integer idade;;
    private StatusAnimal status;
    private String tipo;

    public Animal() {
    }

    public Animal(String nome, String raca, Integer idade) {
        this.nomeAnimal = nome;
        this.raca = raca;
        this.idade = idade;
    }

    public String getTipo() {
        if (this instanceof Cachorro) {
            return "Cachorro";
        } else if (this instanceof Gato) {
            return "Gato";
        }
        return "Indefinido";
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

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
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
