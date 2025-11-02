package com.joseramos.sistemaadocao.connection;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        // SQL para criar as tabelas
        String sqlAnimais = "CREATE TABLE IF NOT EXISTS animais ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " idade INTEGER,"
                + " raca TEXT,"
                + " tipo TEXT," // Ex: "CACHORRO" ou "GATO"
                + " status TEXT NOT NULL" // Ex: "DISPONIVEL", "ADOTADO"
                + ");";

        String sqlAdotantes = "CREATE TABLE IF NOT EXISTS adotantes ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " cpf TEXT UNIQUE,"
                + " endereco TEXT,"
                + " totalAdocoes INTEGER DEFAULT 0"
                + ");";

        // (Você também precisará da tabela 'adocoes')

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            // Executa os comandos SQL
            stmt.execute(sqlAnimais);
            stmt.execute(sqlAdotantes);

            System.out.println("Tabelas verificadas/criadas com sucesso.");

        } catch (Exception e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
}
