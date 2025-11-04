package com.joseramos.sistemaadocao;

// Importações do Backend (Database, Repositories, Services)
import com.joseramos.sistemaadocao.connection.DatabaseInitializer;
import com.joseramos.sistemaadocao.repository.AdocaoRepository;
import com.joseramos.sistemaadocao.repository.AnimalRepository;
import com.joseramos.sistemaadocao.repository.AdotanteRepository;
import com.joseramos.sistemaadocao.service.AdocaoService;
import com.joseramos.sistemaadocao.service.AnimalService;
import com.joseramos.sistemaadocao.service.AdotanteService;

// Importação do Controller da View
import com.joseramos.sistemaadocao.GUI.view.MainViewController;

// Importações do JavaFX
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe principal da aplicação JavaFX (Bônus GUI)[cite: 34].
 * Responsável por inicializar o backend e carregar a view principal.
 */
public class Main extends Application {

    // --- Backend ---
    // Referências para os serviços que serão passados ao Controller
    private AnimalService animalService;
    private AdotanteService adotanteService;
    private AdocaoService adocaoService;

    /**
     * Método de inicialização do backend.
     * Roda ANTES da interface gráfica.
     * Perfeito para carregar o banco e instanciar os serviços.
     */
    @Override
    public void init() throws Exception {
        System.out.println("Inicializando o sistema...");

        // 1. Inicializa o Banco de Dados (Cria tabelas e insere sementes)
        DatabaseInitializer.initialize();

        // 2. Instancia os Repositórios
        // A camada de persistência [cite: 22, 26, 31]
        AnimalRepository animalRepo = new AnimalRepository();
        AdotanteRepository adotanteRepo = new AdotanteRepository();

        // O AdocaoRepository precisa dos outros dois para funcionar
        AdocaoRepository adocaoRepo = new AdocaoRepository(animalRepo, adotanteRepo);

        // 3. Instancia os Serviços (Injetando os Repositórios)
        this.animalService = new AnimalService(animalRepo);
        this.adotanteService = new AdotanteService(adotanteRepo);

        // O AdocaoService é o mais complexo, precisa de todos os repositórios
        this.adocaoService = new AdocaoService(adocaoRepo, animalRepo, adotanteRepo);

        System.out.println("Serviços prontos.");
    }

    /**
     * Método principal da UI. Roda na thread do JavaFX.
     * Carrega a primeira tela (Stage) baseada no FXML.
     */
    // (Dentro de Main.java)

    // (Substitua este método no seu Main.java)

    // (Substitua este método no seu Main.java)

    @Override
    public void start(Stage primaryStage) {
        try {
            // --- CORREÇÃO AQUI ---
            // Adicionando o pacote 'GUI' que estava faltando
            String fxmlPath = "/com/joseramos/sistemaadocao/view/MainView.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                // Mensagem de erro corrigida para o caminho certo
                throw new IOException("Não foi possível encontrar 'MainView.fxml' em " + fxmlPath +
                        "\nVerifique se o arquivo está em 'src/main/resources/com/joseramos/sistemaadocao/GUI/view/'");
            }

            Parent root = loader.load();

            // O getController() funcionará pois o FXML aponta para o controller correto
            com.joseramos.sistemaadocao.GUI.view.MainViewController controller = loader.getController();

            controller.setServices(animalService, adotanteService, adocaoService);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Sistema de Adoção de Animais");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erro crítico ao carregar a view principal:");
            e.printStackTrace();
        }
    }
}