package com.joseramos.sistemaadocao.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.entidades.Cachorro;
import com.joseramos.sistemaadocao.entidades.Gato;
import com.joseramos.sistemaadocao.entidades.StatusAnimal;
import com.joseramos.sistemaadocao.connection.ConnectionFactory;

public class AnimalRepository {

    /**
     * Salva um novo animal no banco de dados (Create).
     */
    public Animal salvar(Animal animal) {
        // ATUALIZADO: Adicionadas colunas 'vacinado' e 'vermifugado'
        String sql = "INSERT INTO animais (nome, idade, raca, tipo, status, vacinado, vermifugado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, animal.getNomeAnimal());
            pstmt.setInt(2, animal.getIdade());
            pstmt.setString(3, animal.getRaca());

            // Lógica de Polimorfismo
            if (animal instanceof Cachorro) {
                pstmt.setString(4, "CACHORRO");
            } else if (animal instanceof Gato) {
                pstmt.setString(4, "GATO");
            } else {
                pstmt.setString(4, "OUTRO");
            }

            pstmt.setString(5, animal.getStatus().toString());

            // --- NOVOS CAMPOS ---
            // Salva o estado boolean no banco (SQLite aceita boolean como 0 ou 1)
            pstmt.setBoolean(6, animal.isVacinado());
            pstmt.setBoolean(7, animal.isVermifugado());

            pstmt.executeUpdate();

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
     * Atualiza um animal existente (Update).
     * Fundamental para o botão "Vacinar" funcionar.
     */
    public void atualizar(Animal animal) {
        // ATUALIZADO: Adicionamos 'vacinado = ?' e 'vermifugado = ?'
        String sql = "UPDATE animais SET nome = ?, idade = ?, raca = ?, tipo = ?, status = ?, vacinado = ?, vermifugado = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getNomeAnimal());
            pstmt.setInt(2, animal.getIdade());
            pstmt.setString(3, animal.getRaca());

            if (animal instanceof Cachorro) {
                pstmt.setString(4, "CACHORRO");
            } else if (animal instanceof Gato) {
                pstmt.setString(4, "GATO");
            } else {
                pstmt.setString(4, "OUTRO");
            }

            pstmt.setString(5, animal.getStatus().toString());

            // --- ATUALIZAÇÃO DOS STATUS DE CUIDADO ---
            pstmt.setBoolean(6, animal.isVacinado());
            pstmt.setBoolean(7, animal.isVermifugado());

            pstmt.setInt(8, animal.getId()); // O ID é o último parâmetro

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar animal: " + e.getMessage());
        }
    }

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

    public List<Animal> listarTodos() {
        List<Animal> animais = new ArrayList<>();
        String sql = "SELECT * FROM animais";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Animal animal = extrairAnimalDoResultSet(rs);
                animais.add(animal);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar animais: " + e.getMessage());
        }
        return animais;
    }

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
        return animal;
    }

    /**
     * Método auxiliar para mapear os dados do banco para o objeto.
     */
    private Animal extrairAnimalDoResultSet(ResultSet rs) throws SQLException {

        String tipo = rs.getString("tipo");
        Animal animal;

        if ("CACHORRO".equalsIgnoreCase(tipo)) {
            animal = new Cachorro();
        } else if ("GATO".equalsIgnoreCase(tipo)) {
            animal = new Gato();
        } else {
            // Fallback seguro para evitar crash se o banco tiver lixo
            // Em um sistema real, logariamos um erro.
            animal = new Cachorro();
        }

        animal.setId(rs.getInt("id"));
        animal.setNomeAnimal(rs.getString("nome"));
        animal.setIdade(rs.getInt("idade"));
        animal.setRaca(rs.getString("raca"));
        animal.setStatus(StatusAnimal.valueOf(rs.getString("status")));

        // --- RECUPERAÇÃO DOS STATUS (IMPORTANTE) ---
        // O banco devolve true/false. Nós passamos para o objeto.

        // Se a coluna não existir no banco antigo, o getBoolean retorna false (seguro)
        try {
            boolean vacinado = rs.getBoolean("vacinado");
            boolean vermifugado = rs.getBoolean("vermifugado");

            animal.setVacinado(vacinado);
            animal.setVermifugado(vermifugado);

        } catch (SQLException e) {
            // Ignora se as colunas não existirem ainda (retrocompatibilidade)
            System.out.println("Aviso: Colunas de vacina/vermifugo não encontradas ou erro ao ler.");
        }

        return animal;
    }
}