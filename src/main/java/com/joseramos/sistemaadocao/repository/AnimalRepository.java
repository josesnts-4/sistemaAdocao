package com.joseramos.sistemaadocao.repository;

import com.joseramos.sistemaadocao.entidades.Animal;

import java.util.List;

public interface AnimalRepository {

    /**
     * salva um animal na persistência
     */
    void salvar(Animal animal);

    /**
     * atualiza um animal na persistência
     */
    void atualizar(Animal animal);

    /**
     * remove um animal da persistência pelo ID
     */
    void remover(int id);

    /**
     * busca um animal na persistência pelo ID
     * @return O animal encontrado ou null se não existir
     */
    Animal buscarPorId(int id);

    /**
     * lista todos os animais cadastrado
     * @return Uma lista de todos os animais
     */
    List<Animal> listarTodos();

}
