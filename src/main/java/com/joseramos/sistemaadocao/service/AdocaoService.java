import java.time.LocalDate;
import java.util.List;

// Assumindo que você tem esses pacotes
import com.joseramos.sistemaadocao.entidades.Adocao;
import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.entidades.Adotante;
import com.joseramos.sistemaadocao.entidades.StatusAnimal; // (Um Enum: DISPONIVEL, ADOTADO)
import com.joseramos.sistemaadocao.excecoes.LimiteAdocoesException;
import com.joseramos.sistemaadocao.repository.AdocaoRepository;
import com.joseramos.sistemaadocao.repository.AnimalRepository;
import com.joseramos.sistemaadocao.repository.AdotanteRepository;

public class AdocaoService {

    // O serviço DEPENDE dos repositórios.
    // Usamos "injeção de dependência" (passando no construtor)
    // para manter o isolamento[cite: 31].
    private AdocaoRepository adocaoRepository;
    private AnimalRepository animalRepository;
    private AdotanteRepository adotanteRepository;

    public AdocaoService(AdocaoRepository adocaoRepository,
                         AnimalRepository animalRepository,
                         AdotanteRepository adotanteRepository) {
        this.adocaoRepository = adocaoRepository;
        this.animalRepository = animalRepository;
        this.adotanteRepository = adotanteRepository;
    }

    /**
     * Método principal que aplica todas as regras de negócio para uma adoção.
     * [cite: 44-50]
     */
    public void realizarAdocao(int idAdotante, int idAnimal)
            throws LimiteAdocoesException, AnimalIndisponivelException, Exception {

        // 1. Busca os objetos no banco [cite: 46]
        Adotante adotante = adotanteRepository.buscarPorId(idAdotante);
        Animal animal = animalRepository.buscarPorId(idAnimal);

        // Validações de segurança (se não existem)
        if (adotante == null) {
            throw new Exception("Adotante com ID " + idAdotante + " não encontrado.");
        }
        if (animal == null) {
            throw new Exception("Animal com ID " + idAnimal + " não encontrado.");
        }

        // 2. Valida Regra: Quantidade de adoções < 3? [cite: 19, 48]
        if (adotante.getTotalAdocoes() >= 3) {
            throw new LimiteAdocoesException("Adotante " + adotante.getNome() +
                    " já atingiu o limite de 3 adoções simultâneas.");
        }

        // 3. Valida Regra: Animal está DISPONIVEL? [cite: 20, 49]
        if (animal.getStatus() != StatusAnimal.DISPONIVEL) {
            throw new AnimalIndisponivelException("O animal " + animal.getNome() +
                    " não está disponível para adoção (Status: " + animal.getStatus() + ").");
        }

        // 4. Se OK: Efetiva a Adoção [cite: 50]

        // Atualiza os status dos objetos
        animal.setStatus(StatusAnimal.ADOTADO);
        adotante.setTotalAdocoes(adotante.getTotalAdocoes() + 1);

        // Cria o registro de adoção
        Adocao novaAdocao = new Adocao(animal, adotante, LocalDate.now());

        // 5. Persiste todas as alterações [cite: 50]
        // (Em um sistema real, isso seria uma "transação" de banco de dados)
        try {
            animalRepositorio.atualizar(animal); // Marca animal como ADOTADO no DB
            adotanteRepositorio.atualizar(adotante); // Incrementa contador no DB
            adocaoRepositorio.salvar(novaAdocao); // Salva o novo registro de adoção

        } catch (Exception e) {
            // Se algo der errado (ex: falha no DB), lança uma exceção geral
            throw new Exception("Erro ao persistir os dados da adoção: " + e.getMessage());
        }
    }

    /**
     * Lista adoções com base em filtros [cite: 17]
     */
    public List<Adocao> listarAdocoesPorAdotante(int idAdotante) {
        return adocaoRepositorio.listarPorAdotante(idAdotante);
    }

    public List<Adocao> listarAdocoesPorPeriodo(LocalDate inicio, LocalDate fim) {
        return adocaoRepositorio.listarPorPeriodo(inicio, fim);
    }
}