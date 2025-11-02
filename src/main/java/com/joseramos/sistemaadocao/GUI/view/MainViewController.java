package com.joseramos.sistemaadocao.GUI.view;

import com.joseramos.sistemaadocao.entidades.Adocao;
import com.joseramos.sistemaadocao.entidades.Adotante;
import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.excecoes.AnimalIndisponivelException;
import com.joseramos.sistemaadocao.excecoes.LimiteAdocoesException;
import com.joseramos.sistemaadocao.service.AdocaoService;
import com.joseramos.sistemaadocao.service.AdotanteService;
import com.joseramos.sistemaadocao.service.AnimalService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MainViewController {

    // --- Serviços (Backend) ---
    // Estes serão "injetados" pela classe Main.java
    private AnimalService animalService;
    private AdotanteService adotanteService;
    private AdocaoService adocaoService;

    // --- Componentes da Aba Animais ---
    @FXML private TableView<Animal> animaisTable;
    @FXML private TableColumn<Animal, Integer> colAnimalId;
    @FXML private TableColumn<Animal, String> colAnimalNome;
    @FXML private TableColumn<Animal, String> colAnimalTipo; // (Vamos precisar de um getter para 'tipo' em Animal)
    @FXML private TableColumn<Animal, String> colAnimalRaca;
    @FXML private TableColumn<Animal, String> colAnimalStatus;

    // --- Componentes da Aba Adotantes ---
    @FXML private TableView<Adotante> adotantesTable;
    @FXML private TableColumn<Adotante, Integer> colAdotanteId;
    @FXML private TableColumn<Adotante, String> colAdotanteNome;
    @FXML private TableColumn<Adotante, String> colAdotanteCpf;
    @FXML private TableColumn<Adotante, Integer> colAdotanteTotal;

    // --- Componentes da Aba Adoções ---
    @FXML private TextField txtAdocaoIdAnimal;
    @FXML private TextField txtAdocaoIdAdotante;
    @FXML private Button btnRealizarAdocao;
    @FXML private TableView<Adocao> adocoesTable;
    @FXML private TableColumn<Adocao, Integer> colAdocaoId;
    @FXML private TableColumn<Adocao, String> colAdocaoData;
    @FXML private TableColumn<Adocao, String> colAdocaoAnimal; // (Vamos precisar de getters formatados)
    @FXML private TableColumn<Adocao, String> colAdocaoAdotante;
    @FXML private TextField txtFiltroAdotanteId;
    @FXML private Button btnFiltrarAdocoes;
    @FXML private Button btnLimparFiltroAdocoes;


    /**
     * Método de inicialização do Controller.
     * Chamado automaticamente depois que o FXML é carregado.
     * Usado para configurar as colunas das tabelas.
     */
    @FXML
    public void initialize() {
        // Configura as colunas da tabela de Animais
        // O valor "id", "nome", etc., DEVE corresponder ao nome do método getter
        // na classe Animal (ex: getId(), getNome())
        colAnimalId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAnimalNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAnimalRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));
        colAnimalStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        // Para "tipo", precisamos de um getter na classe Animal
        colAnimalTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        // Configura as colunas da tabela de Adotantes
        colAdotanteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAdotanteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAdotanteCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colAdotanteTotal.setCellValueFactory(new PropertyValueFactory<>("totalAdocoes"));

        // Configura as colunas da tabela de Adoções
        colAdocaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAdocaoData.setCellValueFactory(new PropertyValueFactory<>("dataAdocaoFormatada")); // (Precisamos criar este getter em Adocao.java)
        colAdocaoAnimal.setCellValueFactory(new PropertyValueFactory<>("nomeAnimal")); // (Precisamos criar este getter)
        colAdocaoAdotante.setCellValueFactory(new PropertyValueFactory<>("nomeAdotante")); // (Precisamos criar este getter)
    }

    /**
     * Este é o método que a Main.java chama para injetar os serviços.
     * Após injetar, carregamos os dados iniciais nas tabelas.
     */
    public void setServices(AnimalService animalService, AdotanteService adotanteService, AdocaoService adocaoService) {
        this.animalService = animalService;
        this.adotanteService = adotanteService;
        this.adocaoService = adocaoService;

        // Carrega os dados iniciais nas tabelas
        carregarAnimais();
        carregarAdotantes();
        // Carrega todas as adoções por padrão (requer método no service)
        handleLimparFiltroAdocoes();
    }

    // --- MÉTODOS DE AÇÃO (HANDLERS) ---

    @FXML
    private void handleRealizarAdocao() {
        try {
            // 1. Coletar dados da UI
            int idAnimal = Integer.parseInt(txtAdocaoIdAnimal.getText());
            int idAdotante = Integer.parseInt(txtAdocaoIdAdotante.getText());

            // 2. Chamar o Serviço (Lógica de Negócio)
            adocaoService.realizarAdocao(idAdotante, idAnimal); [cite: 37]

            // 3. Sucesso
            mostrarAlerta("Sucesso", "Adoção realizada com sucesso!", Alert.AlertType.INFORMATION);

            // 4. Limpar campos e atualizar tabelas
            txtAdocaoIdAnimal.clear();
            txtAdocaoIdAdotante.clear();

            // Atualiza as tabelas de animais e adotantes (pois o status/total mudou)
            carregarAnimais();
            carregarAdotantes();
            // Atualiza a tabela de adoções
            handleLimparFiltroAdocoes();

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro de Entrada", "IDs devem ser números válidos.", Alert.AlertType.ERROR);
        } catch (LimiteAdocoesException | AnimalIndisponivelException e) {
            // Captura as exceções personalizadas (Regras de Negócio) [cite: 21]
            mostrarAlerta("Erro de Regra de Negócio", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            // Captura outros erros (ex: Adotante não encontrado)
            mostrarAlerta("Erro", "Não foi possível realizar a adoção: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleFiltrarAdocoes() {
        try {
            int idAdotante = Integer.parseInt(txtFiltroAdotanteId.getText());
            List<Adocao> adocoes = adocaoService.listarAdocoesPorAdotante(idAdotante); [cite: 17]
            adocoesTable.setItems(FXCollections.observableArrayList(adocoes));

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro de Entrada", "ID do adotante deve ser um número.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLimparFiltroAdocoes() {
        // (Isso assume que você criou um método 'listarTodas' no seu AdocaoService/Repository)
        // List<Adocao> adocoes = adocaoService.listarTodasAdocoes();
        // adocoesTable.setItems(FXCollections.observableArrayList(adocoes));

        // Por enquanto, vamos apenas limpar a tabela se o método não existir
        adocoesTable.getItems().clear();
        txtFiltroAdotanteId.clear();
    }


    // --- MÉTODOS DE CARREGAMENTO DE DADOS ---

    private void carregarAnimais() {
        List<Animal> animais = animalService.listarAnimais();
        animaisTable.setItems(FXCollections.observableArrayList(animais));
    }

    private void carregarAdotantes() {
        List<Adotante> adotantes = adotanteService.listarAdotantes();
        adotantesTable.setItems(FXCollections.observableArrayList(adotantes));
    }

    // --- MÉTODO UTILITÁRIO PARA POPUPS ---

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // NOTA: Os botões Novo/Editar/Remover (ex: btnNovoAnimal)
    // não foram implementados. Eles exigiriam a criação de novas
    // janelas (modais) para formulários, o que é um passo
    // adicional de complexidade. O foco aqui foi o fluxo de adoção.
}