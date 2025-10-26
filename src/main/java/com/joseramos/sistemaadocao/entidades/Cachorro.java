package com.joseramos.sistemaadocao.entidades;
import jakarta.persistence.*;

public class Cachorro extends Animal {

    private String treinado;
    private String peso;
    private String porte;

    public Cachorro(String nome, String raca, Integer idade, String sexo, String descricao, String tipo, String porte, String treinado, String pesoCachorro) {
        super(nome, raca, idade, sexo, descricao, "cachorro");
        this.porte = porte;
        this.treinado = treinado;
        this.peso = pesoCachorro;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTreinado() {
        return treinado;
    }

    public void setTreinado(String treinado) {
        this.treinado = treinado;

    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    @Override
    public void emitirSom() {
        System.out.println("Ruf Ruf!");
    }
}