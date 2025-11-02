package com.joseramos.sistemaadocao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

        // O nome do arquivo do banco de dados
        private static final String DB_URL = "jdbc:sqlite:adocao_sistema.db";

        public static Connection getConnection() {
            try {
                // Carrega o driver (embora em JDBC 4.0+ não seja estritamente necessário)
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(DB_URL);
            } catch (
                    SQLException | ClassNotFoundException e) {
                System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
                return null;
            }
        }
    }

