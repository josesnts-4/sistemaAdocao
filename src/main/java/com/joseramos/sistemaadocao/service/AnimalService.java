package com.joseramos.sistemaadocao.service;

import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.repository.AnimalRepository;

import java.util.List;

/**
 * Camada de serviço para as operações de negócio relacionadas a Animais.
 * Atua como intermediário entre a View (Controller) e o Repository.
 */
public class AnimalService {

    // O serviço depende do repositório
    private AnimalRepository animalRepository;

    /**
     * Construtor que recebe o repositório (Injeção de Dependência).
     */
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    /**
     * Cadastra um novo animal.
     * (Regras de negócio, como validações, podem ser adicionadas aqui)
     */
    public void cadastrarAnimal(Animal animal) {
        // Exemplo de regra de negócio que você poderia adicionar:
        if (animal.getNomeAnimal() == null || animal.getNomeAnimal().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do animal não pode ser vazio.");
        }

        animalRepository.salvar(animal);
    }

    /**
     * Lista todos os animais cadastrados.
     */
    public List<Animal> listarAnimais() {
        return animalRepository.listarTodos();
    }

    /**
     * Atualiza os dados de um animal existente.
     */
    public void atualizarAnimal(Animal animal) {
        // (Validações de atualização podem vir aqui)
        animalRepository.atualizar(animal);
    }

    /**
     * Remove um animal pelo seu ID.
     */
    public void removerAnimal(int id) {
        animalRepository.remover(id);
    }

    /**
     * Busca um animal pelo seu ID.
     */
    public Animal buscarPorId(int id) {
        return animalRepository.buscarPorId(id);
    }
}