package com.joseramos.sistemaadocao.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// (Importe a sua classe Adotante e a ConnectionFactory)
import com.joseramos.sistemaadocao.entidades.Adotante;
import com.joseramos.sistemaadocao.connection.ConnectionFactory;

    /**
     * Classe responsável pela persistência (CRUD) da entidade Adotante
     * no banco de dados SQLite.
     [cite_start]* [cite: 15, 28, 31]
     */
    public class AdotanteRepository {

        /**
         * Salva um novo adotante no banco de dados (Create).
         [cite_start]* [cite: 15]
         */
        public Adotante salvar(Adotante adotante) {
            // SQL para inserir um novo adotante.
            // O 'id' é AUTOINCREMENT, por isso não o incluímos.
            // O 'totalAdocoes' tem DEFAULT 0 na tabela.
            String sql = "INSERT INTO adotantes (nome, cpf, endereco) VALUES (?, ?, ?)";

            // Usamos try-with-resources para garantir que a conexão e o statement
            // sejam fechados automaticamente, mesmo se ocorrer um erro.
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // Define os parâmetros do SQL
                pstmt.setString(1, adotante.getNome());
                pstmt.setString(2, adotante.getCpf());
                pstmt.setString(3, adotante.getEndereco());

                // Executa o comando
                pstmt.executeUpdate();

                // Recupera o ID gerado pelo banco
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    adotante.setId(generatedKeys.getInt(1));
                }

            } catch (SQLException e) {
                System.err.println("Erro ao salvar adotante: " + e.getMessage());
                // Tratar ou relançar a exceção
            }
            return adotante;
        }

        /**
         * Atualiza um adotante existente no banco de dados (Update).
         [cite_start]* [cite: 15]
         * Essencial para o fluxo de adoção (para incrementar 'totalAdocoes').
         */
        public void atualizar(Adotante adotante) {
            String sql = "UPDATE adotantes SET nome = ?, cpf = ?, endereco = ?, totalAdocoes = ? WHERE id = ?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, adotante.getNome());
                pstmt.setString(2, adotante.getCpf());
                pstmt.setString(3, adotante.getEndereco());
                pstmt.setInt(4, adotante.getTotalAdocoes());
                pstmt.setInt(5, adotante.getId()); // ID é o filtro

                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Erro ao atualizar adotante: " + e.getMessage());
            }
        }

        /**
         * Remove um adotante do banco de dados (Delete).
         [cite_start]* [cite: 15]
         */
        public void remover(int id) {
            String sql = "DELETE FROM adotantes WHERE id = ?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Erro ao remover adotante: " + e.getMessage());
            }
        }

        /**
         * Lista todos os adotantes cadastrados (Read).
         [cite_start]* [cite: 15]
         */
        public List<Adotante> listarTodos() {
            List<Adotante> adotantes = new ArrayList<>();
            String sql = "SELECT * FROM adotantes";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                // Itera sobre os resultados
                while (rs.next()) {
                    Adotante adotante = extrairAdotanteDoResultSet(rs);
                    adotantes.add(adotante);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao listar adotantes: " + e.getMessage());
            }
            return adotantes;
        }

        /**
         * Busca um adotante específico pelo seu ID (Read).
         [cite_start]* [cite: 46]
         */
        public Adotante buscarPorId(int id) {
            String sql = "SELECT * FROM adotantes WHERE id = ?";
            Adotante adotante = null;

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        adotante = extrairAdotanteDoResultSet(rs);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao buscar adotante por ID: " + e.getMessage());
            }
            return adotante; // Retorna null se não encontrar
        }

        /**
         * Método utilitário privado para "mapear" uma linha do ResultSet
         * para um objeto Adotante. Evita repetição de código.
         */
        private Adotante extrairAdotanteDoResultSet(ResultSet rs) throws SQLException {
            Adotante adotante = new Adotante();
            adotante.setId(rs.getInt("id"));
            adotante.setNome(rs.getString("nome"));
            adotante.setCpf(rs.getString("cpf"));
            adotante.setEndereco(rs.getString("endereco"));
            adotante.setTotalAdocoes(rs.getInt("totalAdocoes"));
            return adotante;
        }
    }
