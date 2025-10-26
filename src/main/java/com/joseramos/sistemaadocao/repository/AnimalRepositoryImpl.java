package com.joseramos.sistemaadocao.repository;

import com.joseramos.sistemaadocao.entidades.Animal;
import java.util.ArrayList;
import java.util.List;

public class AnimalRepositoryImpl implements AnimalRepository {

    private final List<Animal> animais = new ArrayList<>();

    @Override
    public void salvar(Animal animal) {
        animais.add(animal);
    }

    @Override
    public void atualizar(Animal animal) {
        for (int i = 0; i < animais.size(); i++) {
            if (animais.get(i).getId() == animal.getId()) {
                animais.set(i, animal);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {
        animais.removeIf(a -> a.getId() == id);
    }

    @Override
    public Animal buscarPorId(int id) {
        return animais.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Animal> listarTodos() {
        return new ArrayList<>(animais);
    }
}

