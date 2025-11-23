package com.joseramos.sistemaadocao;

// (Todas as suas importações continuam iguais)
import com.joseramos.sistemaadocao.connection.ConnectionFactory;
import com.joseramos.sistemaadocao.connection.DatabaseInitializer;
import com.joseramos.sistemaadocao.repository.AdocaoRepository;
import com.joseramos.sistemaadocao.repository.AnimalRepository;
import com.joseramos.sistemaadocao.repository.AdotanteRepository;
import com.joseramos.sistemaadocao.service.AdocaoService;
import com.joseramos.sistemaadocao.service.AnimalService;
import com.joseramos.sistemaadocao.service.AdotanteService;
import com.joseramos.sistemaadocao.GUI.view.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main extends Application {

    private AnimalService animalService;
    private AdotanteService adotanteService;
    private AdocaoService adocaoService;

    /**
     * O método main agora só inicia o JavaFX.
     * Toda a lógica de inicialização vai para o init().
//     */
//    public static void main(String[] args) {
//        launch(args);
//    }


    @Override
    public void init() throws Exception {
        System.out.println("Inicializando o sistema...");

        // 1. Inicializa o Banco de Dados (CRIA AS TABELAS)
        DatabaseInitializer.initialize();

        // 2. LIMPA OS DADOS ÓRFÃOS (AGORA QUE AS TABELAS EXISTEM)
        // (Também mudei para não ser 'static')
        // limparDadosOrfaos();

        // 3. Instancia os Repositórios
        AnimalRepository animalRepo = new AnimalRepository();
        AdotanteRepository adotanteRepo = new AdotanteRepository();
        AdocaoRepository adocaoRepo = new AdocaoRepository(animalRepo, adotanteRepo);

        // 4. Instancia os Serviços
        this.animalService = new AnimalService(animalRepo);
        this.adotanteService = new AdotanteService(adotanteRepo, adocaoRepo);
        this.adocaoService = new AdocaoService(adocaoRepo, animalRepo, adotanteRepo);

        System.out.println("Serviços prontos.");
    }

//    /**
//     * MÉTODO DE LIMPEZA
//     * (Não é mais 'static', pois é chamado pelo 'init()')
//     */
//    private void limparDadosOrfaos() {
//        // !!! ATENÇÃO: SUBSTITUA ESTA LINHA PELA URL CORRETA!!!
//        String url = "jdbc:sqlite:DatabaseInitializer.db";
//
//        String sql = "DELETE FROM adocoes WHERE id_adotante NOT IN (SELECT id FROM adotantes)";
//
//        try (Connection conn = com.joseramos.sistemaadocao.connection.ConnectionFactory.getConnection();
//             Statement stmt = conn.createStatement()) {
//
//            int linhasAfetadas = stmt.executeUpdate(sql);
//            if (linhasAfetadas > 0) {
//                System.out.println("[LIMPEZA] " + linhasAfetadas + " adoção(ões) órfã(s) foram removidas.");
//            } else {
//                System.out.println("[LIMPEZA] Banco de dados já está consistente.");
//            }
//
//        } catch (Exception e) {
//            // Se o erro "no such table" persistir aqui,
//            // significa que seu DatabaseInitializer não está criando a tabela 'Adocao'
//            System.err.println("Erro ao tentar limpar o banco: " + e.getMessage());
//        }
//    }

    /**
     * Método principal da UI (start)
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            String fxmlPath = "/com/joseramos/sistemaadocao/view/MainView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                throw new IOException("Não foi possível encontrar 'MainView.fxml' em " + fxmlPath +
                        "\nVerifique se o arquivo está em 'src/main/resources/com/joseramos/sistemaadocao/GUI/view/'");
            }

            Parent root = loader.load();
            MainViewController controller = loader.getController();
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