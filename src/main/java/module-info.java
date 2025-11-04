module com.joseramos.sistemaadocao {

    // --- MÓDULOS QUE SEU PROJETO PRECISA (REQUER) ---
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql; // Necessário para o JDBC
    requires org.xerial.sqlitejdbc; // O driver do SQLite

    // (Adicione outros 'requires' se você usar mais bibliotecas)


    // --- PACOTES QUE O JAVAFX PRECISA ACESSAR (OPEN) ---

    // 1. (CORRIGE O SEU ERRO ATUAL)
    // Abre o pacote principal (onde está Main.java) para o JavaFX.
    opens com.joseramos.sistemaadocao to javafx.graphics, javafx.fxml;

    // 2. (CORRIGE O PRÓXIMO ERRO QUE VOCÊ TERIA)
    // Abre o pacote 'view' (onde estão os Controllers) para o FXML.
    opens com.joseramos.sistemaadocao.GUI.view to javafx.fxml;

    // 3. (CORRIGE O ERRO DA TABELA - TableView)
    // Abre o pacote 'entidades' para o JavaFX (para a tabela acessar os getters)
    opens com.joseramos.sistemaadocao.entidades to javafx.base;
}