package com.joseramos.sistemaadocao.service;

import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.repository.AnimalRepository;

import java.util.List;

public class AnimalService {
    private AnimalRepository animalRepository;

    /**
     * Construtor que recebe a implementação do repositório
     */
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
        // Ao iniciar, o serviço pode garantir que os dados sejam carregados

    }

    /**
     * Cadastra um animal no Sistema
     * @param animal O objeto Animal (Cachorro ou gato) a ser cadastrado
     */
    public void cadastrarAnimal(Animal animal) {

        // Delega a ação de salvar para o repositorio
        animalRepository.salvar(animal);
        System.out.println("Animal cadastrado com sucesso!");
    }

    /**
     * Lista todos os animais cadastrados
     * @return Uma lista de todos os animais
     */
    public List<Animal> listarAnimais() {
        return animalRepository.listarTodos();
    }

    /**
     * Busca um animal especifico pelo seu ID
     * este metodo ´é um exemplo de busca (Sobrecarga exigida)
     * @param id O ID do animal a ser buscado
     * @return O animal encontrado ou null se não existir
     */
    public Animal buscarAnimalPorId(int id) {
        return animalRepository.buscarPorId(id);
    }

    /**
     * Atualiza um animal existente no Sistema
     * @param animal O objeto Animal (Cachorro ou gato) a ser atualizado
     */
    public void atualizarAnimal(Animal animal) {
        Animal animalExistente = animalRepository.buscarPorId(animal.getId());
        if (animalExistente == null) {
            System.out.println("Erro Animal não encontrado");
            return;
        }
        // Delega a atualização para o repositório
        animalRepository.atualizar(animal);
        System.out.println("Animal atualizado com sucesso!");
    }

    public void removerAnimal(int id) {
        Animal animalExistente = animalRepository.buscarPorId(id);
        if (animalExistente == null) {
            System.out.println("Erro Animal não encontrado");
            return;
        }
        // Delega a remoção para o repositório
        animalRepository.remover(id);
        System.out.println("Animal removido com sucesso!");
    }
}
