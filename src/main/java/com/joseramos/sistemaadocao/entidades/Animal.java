package com.joseramos.sistemaadocao.entidades;

import com.joseramos.sistemaadocao.interfaces.CuidadosEspeciais;

public abstract class Animal implements CuidadosEspeciais {

    private Integer id;
    private String nomeAnimal;
    private String raca;
    private Integer idade;;
    private StatusAnimal status;
    private String tipo;
    private boolean vacinado = false;
    private boolean vermifugado = false;

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
    // O JavaFX chama estes métodos para preencher a tabela com o Enum
    public String getStatusVacinaTexto() {
        if (this.vacinado) {
            return StatusCuidado.VACINADO.toString();
        }
        return StatusCuidado.PENDENTE.toString();
    }

    public String getStatusVermifugoTexto() {
        if (this.vermifugado) {
            return StatusCuidado.VERMIFUGADO.toString();
        }
        return StatusCuidado.PENDENTE.toString();
    }

    // Getters booleanos padrões (necessários para lógica interna)
    public boolean isVacinado() { return vacinado; }
    public void setVacinado(boolean vacinado) { this.vacinado = vacinado; }
    public boolean isVermifugado() { return vermifugado; }
    public void setVermifugado(boolean vermifugado) { this.vermifugado = vermifugado; }

    public abstract void emitirSom();

    @Override
    public void vacinar(){
        this.vacinado = true;
    }
    @Override
    public void vermifugar(){
        this.vermifugado = true;
    }
}
