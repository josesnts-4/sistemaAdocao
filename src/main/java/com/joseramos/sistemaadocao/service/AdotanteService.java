package com.joseramos.sistemaadocao.service;

import com.joseramos.sistemaadocao.entidades.Adotante;
import com.joseramos.sistemaadocao.repository.AdocaoRepository;
import com.joseramos.sistemaadocao.repository.AdotanteRepository;

import java.util.List;

/**
 * Camada de serviço para as operações de negócio relacionadas a Adotantes.
 * Atua como intermediário entre a View (Controller) e o Repository.
 */
public class AdotanteService {
    private AdocaoRepository adocaoRepo;
    private AdotanteRepository adotanteRepo;

    // O serviço depende do repositório
    /**
     * Construtor que recebe o repositório (Injeção de Dependência).
     */
    public AdotanteService(AdotanteRepository adotanteRepo, AdocaoRepository adocaoRepo) {
        this.adotanteRepo = adotanteRepo;
        this.adocaoRepo = adocaoRepo;
    }

    /**
     * Cadastra um novo adotante.
     * (Regras de negócio, como validação de CPF, podem ser adicionadas aqui)
     */
    public void cadastrarAdotante(Adotante adotante) {
        // Exemplo de regra de negócio que você poderia adicionar:
        if (adotante.getNome() == null || adotante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do adotante não pode ser vazio.");
        }
        if (adotante.getCpf() == null || adotante.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do adotante não pode ser vazio.");
        }
        // (Aqui poderia entrar uma lógica mais complexa de validação de CPF)

        adotanteRepo.salvar(adotante);
    }

    /**
     * Lista todos os adotantes cadastrados.
     */
    public List<Adotante> listarAdotantes() {
        return adotanteRepo.listarTodos();
    }

    /**
     * Atualiza os dados de um adotante existente.
     */
    public void atualizarAdotante(Adotante adotante) {
        // (Validações de atualização podem vir aqui)
        adotanteRepo.atualizar(adotante);
    }

    /**
     * Remove um adotante pelo seu ID.
     */
    public void removerAdotante(int idAdotante) throws Exception {

        // 1. VERIFICAÇÃO AUTOMÁTICA
        // Buscamos no banco de adoções se este ID de adotante aparece em algum registro
        boolean adotanteTemAdocoes = adocaoRepo.existeAdocaoPorAdotante(idAdotante);

        // 2. APLICA A REGRA
        if (adotanteTemAdocoes) {
            throw new Exception("Este adotante possui histórico de adoções e não pode ser removido.");
        }
        adotanteRepo.remover(idAdotante);
    }
    /**
     * Busca um adotante pelo seu ID.
     */
    public Adotante buscarPorId(int id) {
        return adotanteRepo.buscarPorId(id);
    }
}