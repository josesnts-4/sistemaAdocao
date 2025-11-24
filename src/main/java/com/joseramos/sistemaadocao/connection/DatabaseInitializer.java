package com.joseramos.sistemaadocao.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Responsável por inicializar o banco de dados.
 * 1. Cria as tabelas se elas não existirem.
 * 2. Insere dados iniciais (semente) se o banco estiver vazio.
 */
public class DatabaseInitializer {

    public static void initialize() {
        // SQL para criar a tabela de Adotantes
        String sqlAdotantes = "CREATE TABLE IF NOT EXISTS adotantes ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " cpf TEXT UNIQUE NOT NULL,"
                + " endereco TEXT,"
                + " totalAdocoes INTEGER DEFAULT 0"
                + ");";

        // SQL para criar a tabela de Animais
        String sqlAnimais = "CREATE TABLE IF NOT EXISTS animais ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " idade INTEGER,"
                + " raca TEXT,"
                + " tipo TEXT NOT NULL," // "CACHORRO" ou "GATO"
                + " status TEXT NOT NULL," // "DISPONIVEL" ou "ADOTADO"
                + " vacinado BOOLEAN DEFAULT 0,"
                + " vermifugado BOOLEAN DEFAULT 0"
                + ");";

        // SQL para criar a tabela de Adoções
        String sqlAdocoes = "CREATE TABLE IF NOT EXISTS adocoes ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " data_adocao TEXT NOT NULL," // Data como Texto (ISO)
                + " id_adotante INTEGER,"
                + " id_animal INTEGER,"
                + " FOREIGN KEY (id_adotante) REFERENCES adotantes(id),"
                + " FOREIGN KEY (id_animal) REFERENCES animais(id)"
                + ");";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            // Executa a criação das tabelas
            stmt.execute(sqlAdotantes);
            stmt.execute(sqlAnimais);
            stmt.execute(sqlAdocoes);

            System.out.println("Tabelas verificadas/criadas com sucesso.");

            // Chama o método para inserir dados iniciais
            seedData(conn);

        } catch (Exception e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    /**
     * Insere dados iniciais (semente) no banco,
     * apenas se as tabelas estiverem vazias.
     *
     */
    private static void seedData(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            // Verifica se a tabela de animais está vazia
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM animais");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Banco vazio. Inserindo dados iniciais (semente)...");

                // Inserindo Animais
                stmt.execute("INSERT INTO animais (nome, idade, raca, tipo, status) VALUES " +
                        "('Rex', 3, 'Vira-lata', 'CACHORRO', 'DISPONIVEL')," +
                        "('Mia', 1, 'Siamês', 'GATO', 'DISPONIVEL')," +
                        "('Thor', 5, 'Golden Retriever', 'CACHORRO', 'DISPONIVEL')," +
                        "('Luna', 2, 'Persa', 'GATO', 'ADOTADO');"); // Um já adotado para testes

                // Inserindo Adotantes
                stmt.execute("INSERT INTO adotantes (nome, cpf, endereco, totalAdocoes) VALUES " +
                        "('João Silva', '111.111.111-11', 'Rua A, 123', 0)," +
                        "('Maria Souza', '222.222.222-22', 'Rua B, 456', 1);"); // Maria já tem 1

                System.out.println("Dados iniciais inseridos.");
            } else {
                System.out.println("Banco de dados já populado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir dados iniciais (semente): " + e.getMessage());
        }
    }
}