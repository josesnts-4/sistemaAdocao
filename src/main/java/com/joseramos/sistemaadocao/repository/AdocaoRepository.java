package com.joseramos.sistemaadocao.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Importações das Entidades e da Conexão
import com.joseramos.sistemaadocao.entidades.Adocao;
import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.entidades.Adotante;
import com.joseramos.sistemaadocao.connection.ConnectionFactory;

/**
 * Classe responsável pela persistência (CRUD) da entidade de ligação Adocao.
 * [cite_start]Também implementa os filtros de listagem[cite: 17].
 *
 */
public class AdocaoRepository {

    // Formato padrão para salvar datas no SQLite como TEXTO (ISO-8601)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // Dependência de outros repositórios para reconstruir os objetos
    private AnimalRepository animalRepository;
    private AdotanteRepository adotanteRepository;

    public AdocaoRepository(AnimalRepository animalRepository, AdotanteRepository adotanteRepository) {
        this.animalRepository = animalRepository;
        this.adotanteRepository = adotanteRepository;
    }

    /**
     * Salva um novo registro de adoção no banco (Create).
     * Armazena apenas os IDs (chaves estrangeiras) do animal e do adotante.
     *
     */
    public Adocao salvar(Adocao adocao) {
        String sql = "INSERT INTO adocoes (data_adocao, id_adotante, id_animal) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Salva a data como uma String no formato "YYYY-MM-DD"
            pstmt.setString(1, adocao.getDataAdocao().format(DATE_FORMATTER));

            // Salva as chaves estrangeiras
            pstmt.setInt(2, adocao.getAdotante().getId());
            pstmt.setInt(3, adocao.getAnimal().getId());

            pstmt.executeUpdate();

            // Recupera o ID gerado para o registro da adoção
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                adocao.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar adoção: " + e.getMessage());
        }
        return adocao;
    }

    /**
     * [cite_start]Lista adoções filtrando por Adotante[cite: 17].
     *
     */
    public List<Adocao> listarPorAdotante(int idAdotante) {
        List<Adocao> adocoes = new ArrayList<>();
        String sql = "SELECT * FROM adocoes WHERE id_adotante = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAdotante);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    adocoes.add(extrairAdocaoDoResultSet(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar adoções por adotante: " + e.getMessage());
        }
        return adocoes;
    }

    /**
     * [cite_start]Lista adoções filtrando por período[cite: 17].
     *
     */
    public List<Adocao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Adocao> adocoes = new ArrayList<>();
        // Como salvamos a data no formato ISO "YYYY-MM-DD",
        // podemos usar o BETWEEN do SQL para filtrar o texto.
        String sql = "SELECT * FROM adocoes WHERE data_adocao BETWEEN ? AND ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inicio.format(DATE_FORMATTER));
            pstmt.setString(2, fim.format(DATE_FORMATTER));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    adocoes.add(extrairAdocaoDoResultSet(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar adoções por período: " + e.getMessage());
        }
        return adocoes;
    }

    /**
     * Método utilitário privado para "mapear" uma linha do ResultSet
     * para um objeto Adocao completo, buscando os objetos relacionados.
     */
    private Adocao extrairAdocaoDoResultSet(ResultSet rs) throws SQLException {

        // 1. Pega os dados simples da tabela 'adocoes'
        int idAdocao = rs.getInt("id");
        String dataAdocaoStr = rs.getString("data_adocao");
        LocalDate dataAdocao = LocalDate.parse(dataAdocaoStr, DATE_FORMATTER);

        // 2. Pega as chaves estrangeiras
        int idAdotante = rs.getInt("id_adotante");
        int idAnimal = rs.getInt("id_animal");

        // 3. "Reconstrói" os objetos usando os outros repositórios
        // Se derem null, uma exceção será lançada (o que é bom, sinaliza erro)
        Adotante adotante = adotanteRepository.buscarPorId(idAdotante);
        Animal animal = animalRepository.buscarPorId(idAnimal);

        if (adotante == null) throw new SQLException("Dados inconsistentes: Adotante ID " + idAdotante + " não encontrado.");
        if (animal == null) throw new SQLException("Dados inconsistentes: Animal ID " + idAnimal + " não encontrado.");

        // 4. Monta o objeto Adocao
        Adocao adocao = new Adocao(animal, adotante, dataAdocao);
        adocao.setId(idAdocao);

        return adocao;
    }

    // (Dentro da classe AdocaoRepository)

    /**
     * Lista TODAS as adoções cadastradas, sem filtro.
     *
     */
    public List<Adocao> listarTodas() {
        List<Adocao> adocoes = new ArrayList<>();
        String sql = "SELECT * FROM adocoes"; // SQL simples para pegar tudo

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Reutiliza o método que já criamos para "montar" o objeto Adocao
                adocoes.add(extrairAdocaoDoResultSet(rs));
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar todas as adoções: " + e.getMessage());
        }
        return adocoes;
    }

    // (Dentro da classe AdocaoService)

    /**
     * Lista todas as adoções registradas, sem filtro.
     * Apenas repassa a chamada para o repositório.
     */
    public List<Adocao> listarTodasAdocoes() {
        AdocaoRepository adocaoRepository = null;
        return adocaoRepository.listarTodas();
    }

    public boolean existeAdocaoPorAdotante(int idAdotante) {
        return listarPorAdotante(idAdotante).size() > 0;
    }
}