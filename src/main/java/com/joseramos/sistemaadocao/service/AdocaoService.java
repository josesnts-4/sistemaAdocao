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
import com.joseramos.sistemaadocao.excecoes.AnimalIndisponivelException;

public class AdocaoService {

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
    // CORREÇÃO 1: Adicionado "throws Exception"
    public void realizarAdocao(int idAdotante, int idAnimal)
            throws LimiteAdocoesException, AnimalIndisponivelException, Exception {

        // 1. Busca os objetos no banco
        Adotante adotante = adotanteRepository.buscarPorId(idAdotante);
        Animal animal = animalRepository.buscarPorId(idAnimal);

        // CORREÇÃO 2: Verificação de Nulos
        if (adotante == null) {
            throw new Exception("Adotante com ID " + idAdotante + " não foi encontrado.");
        }
        if (animal == null) {
            throw new Exception("Animal com ID " + idAnimal + " não foi encontrado.");
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

        // 4. Se OK: Efetiva a Adoção
        animal.setStatus(StatusAnimal.ADOTADO);
        adotante.setTotalAdocoes(adotante.getTotalAdocoes() + 1);
        Adocao novaAdocao = new Adocao(animal, adotante, LocalDate.now());

        // 5. Persiste todas as alterações
        try {
            animalRepository.atualizar(animal);
            adotanteRepository.atualizar(adotante);
            adocaoRepository.salvar(novaAdocao);

        } catch (Exception e) {
            throw new Exception("Erro ao persistir os dados da adoção: " + e.getMessage());
        }
    }

    /**
     * Lista adoções com base em filtros [cite: 17]
     */
    public List<Adocao> listarAdocoesPorAdotante(int idAdotante) {
        return adocaoRepository.listarPorAdotante(idAdotante);
    }

    public List<Adocao> listarAdocoesPorPeriodo(LocalDate inicio, LocalDate fim) {
        return adocaoRepository.listarPorPeriodo(inicio, fim);
    }
}