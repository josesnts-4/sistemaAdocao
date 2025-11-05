package com.joseramos.sistemaadocao.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// (Importe suas classes de modelo e a ConnectionFactory)
import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.entidades.Cachorro;
import com.joseramos.sistemaadocao.entidades.Gato;
import com.joseramos.sistemaadocao.entidades.StatusAnimal; // (O Enum: DISPONIVEL, ADOTADO)
import com.joseramos.sistemaadocao.connection.ConnectionFactory;

/**
 * Classe responsável pela persistência (CRUD) da entidade Animal
 * e suas subclasses (Cachorro, Gato) no banco de dados SQLite.
 *
 */
public class AnimalRepository {

    /**
     * Salva um novo animal no banco de dados (Create).
     * Este método lida com o polimorfismo (Cachorro ou Gato).
     *
     */
    public Animal salvar(Animal animal) {
        String sql = "INSERT INTO animais (nome, idade, raca, tipo, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, animal.getNomeAnimal());
            pstmt.setInt(2, animal.getIdade());
            pstmt.setString(3, animal.getRaca());

            // --- Lógica de Polimorfismo (Salvar) ---
            // Verifica a classe concreta do objeto 'animal'
            if (animal instanceof Cachorro) {
                pstmt.setString(4, "CACHORRO");
            } else if (animal instanceof Gato) {
                pstmt.setString(4, "GATO");
            } else {
                // Caso de fallback, embora não devesse acontecer
                pstmt.setString(4, "OUTRO");
            }
            // ------------------------------------------

            // Salva o status (ex: "DISPONIVEL")
            pstmt.setString(5, animal.getStatus().toString());

            pstmt.executeUpdate();

            // Recupera o ID gerado pelo banco
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                animal.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar animal: " + e.getMessage());
        }
        return animal;
    }

    /**
     * Atualiza um animal existente no banco de dados (Update).
     *
     * Essencial para o fluxo de adoção (para alterar 'status' para ADOTADO).
     */
    public void atualizar(Animal animal) {
        String sql = "UPDATE animais SET nome = ?, idade = ?, raca = ?, tipo = ?, status = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getNomeAnimal());
            pstmt.setInt(2, animal.getIdade());
            pstmt.setString(3, animal.getRaca());

            // Lógica de Polimorfismo (Atualizar)
            if (animal instanceof Cachorro) {
                pstmt.setString(4, "CACHORRO");
            } else if (animal instanceof Gato) {
                pstmt.setString(4, "GATO");
            } else {
                pstmt.setString(4, "OUTRO");
            }

            pstmt.setString(5, animal.getStatus().toString());
            pstmt.setInt(6, animal.getId()); // ID é o filtro

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar animal: " + e.getMessage());
        }
    }

    /**
     * Remove um animal do banco de dados (Delete).
     *
     */
    public void remover(int id) {
        String sql = "DELETE FROM animais WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao remover animal: " + e.getMessage());
        }
    }

    /**
     * Lista todos os animais cadastrados (Read).
     * Este método lida com o polimorfismo (instancia Cachorro ou Gato).
     *
     */
    public List<Animal> listarTodos() {
        List<Animal> animais = new ArrayList<>();
        String sql = "SELECT * FROM animais";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // O método auxiliar cuida do polimorfismo
                Animal animal = extrairAnimalDoResultSet(rs);
                animais.add(animal);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar animais: " + e.getMessage());
        }
        return animais;
    }

    /**
     * Busca um animal específico pelo seu ID (Read).
     *
     */
    public Animal buscarPorId(int id) {
        String sql = "SELECT * FROM animais WHERE id = ?";
        Animal animal = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    animal = extrairAnimalDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar animal por ID: " + e.getMessage());
        }
        return animal; // Retorna null se não encontrar
    }

    /**
     * Método utilitário privado para "mapear" uma linha do ResultSet
     * para o objeto correto (Cachorro ou Gato).
     */
    private Animal extrairAnimalDoResultSet(ResultSet rs) throws SQLException {

        String tipo = rs.getString("tipo");
        Animal animal;

        // --- Lógica de Polimorfismo (Carregar) ---
        // Instancia a classe CORRETA com base na coluna "tipo"
        if ("CACHORRO".equalsIgnoreCase(tipo)) {
            animal = new Cachorro();
        } else if ("GATO".equalsIgnoreCase(tipo)) {
            animal = new Gato();
        } else {
            // Se o tipo for desconhecido, podemos lançar um erro ou usar um padrão.
            // Vamos lançar um erro para sinalizar dados inválidos.
            throw new SQLException("Tipo de animal desconhecido no banco: " + tipo);
        }
        // ------------------------------------------

        // Preenche os dados comuns da classe Abstrata Animal
        animal.setId(rs.getInt("id"));
        animal.setNomeAnimal(rs.getString("nome"));
        animal.setIdade(rs.getInt("idade"));
        animal.setRaca(rs.getString("raca"));

        // Converte a String do banco de volta para o Enum
        animal.setStatus(StatusAnimal.valueOf(rs.getString("status")));

        return animal;
    }
}