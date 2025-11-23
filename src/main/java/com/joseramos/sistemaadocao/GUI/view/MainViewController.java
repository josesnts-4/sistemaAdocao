package com.joseramos.sistemaadocao.GUI.view;

import com.joseramos.sistemaadocao.entidades.Adocao;
import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.entidades.Adotante;
import com.joseramos.sistemaadocao.entidades.StatusCuidado;
import com.joseramos.sistemaadocao.excecoes.AnimalIndisponivelException;
import com.joseramos.sistemaadocao.excecoes.LimiteAdocoesException;
import com.joseramos.sistemaadocao.service.AdocaoService;
import com.joseramos.sistemaadocao.service.AdotanteService;
import com.joseramos.sistemaadocao.service.AnimalService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*; // Importa Alert, Button, etc.
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional; // Necessário para a confirmação de remoção

public class MainViewController {

    // --- Serviços (Backend) ---
    private AnimalService animalService;
    private AdotanteService adotanteService;
    private AdocaoService adocaoService;

    // --- Componentes da Aba Animais ---
    @FXML private TableView<Animal> animaisTable;
    @FXML private TableColumn<Animal, Integer> colAnimalId;
    @FXML private TableColumn<Animal, String> colAnimalNome;
    @FXML private TableColumn<Animal, String> colAnimalTipo;
    @FXML private TableColumn<Animal, String> colAnimalRaca;
    @FXML private TableColumn<Animal, String> colAnimalStatus;
    @FXML private Button btnNovoAnimal;
    @FXML private Button btnEditarAnimal;
    @FXML private Button btnRemoverAnimal;

    // --- Componentes da Aba Adotantes ---
    @FXML private TableView<Adotante> adotantesTable;
    @FXML private TableColumn<Adotante, Integer> colAdotanteId;
    @FXML private TableColumn<Adotante, String> colAdotanteNome;
    @FXML private TableColumn<Adotante, String> colAdotanteCpf;
    @FXML private TableColumn<Adotante, Integer> colAdotanteTotal;
    @FXML private Button btnNovoAdotante;
    @FXML private Button btnEditarAdotante;
    @FXML private Button btnRemoverAdotante;

    // --- Componentes da Aba Adoções ---
    @FXML private TextField txtAdocaoIdAnimal;
    @FXML private TextField txtAdocaoIdAdotante;
    @FXML private Button btnRealizarAdocao;
    @FXML private TableView<Adocao> adocoesTable;
    @FXML private TableColumn<Adocao, Integer> colAdocaoId;
    @FXML private TableColumn<Adocao, String> colAdocaoData;
    @FXML private TableColumn<Adocao, String> colAdocaoAnimal;
    @FXML private TableColumn<Adocao, String> colAdocaoAdotante;
    @FXML private TextField txtFiltroAdotanteId;
    @FXML private Button btnFiltrarAdocoes;
    @FXML private Button btnLimparFiltroAdocoes;


    @FXML
    public void initialize() {
        // --- Tabela de Animais ---
        colAnimalId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAnimalNome.setCellValueFactory(new PropertyValueFactory<>("nomeAnimal"));
        colAnimalRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));
        colAnimalStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAnimalTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        // --- Tabela de Adotantes ---
        colAdotanteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAdotanteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAdotanteCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colAdotanteTotal.setCellValueFactory(new PropertyValueFactory<>("totalAdocoes"));

        // --- Tabela de Adoções ---
        colAdocaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAdocaoData.setCellValueFactory(new PropertyValueFactory<>("dataAdocaoFormatada"));

        // 👇 CORRIGIDO:
        colAdocaoAnimal.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAnimal() != null ? cellData.getValue().getAnimal().getNomeAnimal() : ""
                )
        );

        colAdocaoAdotante.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAdotante() != null ? cellData.getValue().getAdotante().getNome() : ""
                )
        );
    }


    public void setServices(AnimalService animalService, AdotanteService adotanteService, AdocaoService adocaoService) {
        this.animalService = animalService;
        this.adotanteService = adotanteService;
        this.adocaoService = adocaoService;

        // Carrega os dados iniciais nas tabelas
        carregarAnimais();
        carregarAdotantes();
        handleLimparFiltroAdocoes();
    }


    @FXML
    private void handleNovoAnimal() {
        // Chama o método helper passando 'null' (pois é um animal novo)
        boolean salvo = abrirFormularioAnimal(null);
        if (salvo) {
            carregarAnimais();
        }
    }

    @FXML
    private void handleEditarAnimal() {
        Animal animalSelecionado = animaisTable.getSelectionModel().getSelectedItem();
        if (animalSelecionado == null) {
            mostrarAlerta("Nenhum Animal Selecionado", "Por favor, selecione um animal na tabela para editar.", Alert.AlertType.WARNING);
            return;
        }

        // Chama o método helper passando o animal selecionado
        boolean salvo = abrirFormularioAnimal(animalSelecionado);
        if (salvo) {
            carregarAnimais();
        }
    }

    @FXML
    private void handleRemoverAnimal() {
        Animal animalSelecionado = animaisTable.getSelectionModel().getSelectedItem();
        if (animalSelecionado == null) {
            mostrarAlerta("Nenhum Animal Selecionado", "Por favor, selecione um animal na tabela para remover.", Alert.AlertType.WARNING);
            return;
        }

        // Mostra pop-up de confirmação
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Remover Animal: " + animalSelecionado.getNomeAnimal());
        alert.setContentText("Você tem certeza que deseja remover este animal?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                animalService.removerAnimal(animalSelecionado.getId());
                carregarAnimais(); // Atualiza a tabela
            } catch (Exception e) {
                mostrarAlerta("Erro ao Remover", "Não foi possível remover o animal. Verifique se ele já não foi adotado.", Alert.AlertType.ERROR);
            }
        }
    }

    // Handlers da Aba Adotantes

    @FXML
    private void handleNovoAdotante() {
        boolean salvo = abrirFormularioAdotante(null);
        if (salvo) {
            carregarAdotantes();
        }
    }

    @FXML
    private void handleEditarAdotante() {
        Adotante adotanteSelecionado = adotantesTable.getSelectionModel().getSelectedItem();
        if (adotanteSelecionado == null) {
            mostrarAlerta("Nenhum Adotante Selecionado", "Por favor, selecione um adotante na tabela para editar.", Alert.AlertType.WARNING);
            return;
        }

        boolean salvo = abrirFormularioAdotante(adotanteSelecionado);
        if (salvo) {
            carregarAdotantes();
        }
    }

    @FXML
    private void handleRemoverAdotante() {
        Adotante adotanteSelecionado = adotantesTable.getSelectionModel().getSelectedItem();
        if (adotanteSelecionado == null) {
            mostrarAlerta("Nenhum Adotante Selecionado", "Por favor, selecione um adotante na tabela para remover.", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Remover Adotante: " + adotanteSelecionado.getNome());
        alert.setContentText("Você tem certeza que deseja remover este adotante?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                adotanteService.removerAdotante(adotanteSelecionado.getId());
                carregarAdotantes(); // Atualiza a tabela

            } catch (Exception e) {
                e.printStackTrace(); // ISSO VAI IMPRIMIR O ERRO REAL NO CONSOLE (IntelliJ)

                // Mostra o motivo real na tela
                mostrarAlerta("Erro Técnico", "Falha ao excluir: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // Handlers da Aba Adoção

    @FXML
    private void handleRealizarAdocao() {
        try {
            int idAnimal = Integer.parseInt(txtAdocaoIdAnimal.getText());
            int idAdotante = Integer.parseInt(txtAdocaoIdAdotante.getText());

            adocaoService.realizarAdocao(idAdotante, idAnimal);
            mostrarAlerta("Sucesso", "Adoção realizada com sucesso!", Alert.AlertType.INFORMATION);

            txtAdocaoIdAnimal.clear();
            txtAdocaoIdAdotante.clear();

            carregarAnimais();
            carregarAdotantes();
            handleLimparFiltroAdocoes();

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro de Entrada", "IDs devem ser números válidos.", Alert.AlertType.ERROR);
        } catch (LimiteAdocoesException | AnimalIndisponivelException e) {
            mostrarAlerta("Erro de limite de adoções", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Não foi possível realizar a adoção: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleFiltrarAdocoes() {
        try {
            int idAdotante = Integer.parseInt(txtFiltroAdotanteId.getText());
            List<Adocao> adocoes = adocaoService.listarAdocoesPorAdotante(idAdotante);
            adocoesTable.setItems(FXCollections.observableArrayList(adocoes));
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro de Entrada", "ID do adotante deve ser um número.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLimparFiltroAdocoes() {
        try {
            List<Adocao> adocoes = adocaoService.listarTodasAdocoes();
            adocoesTable.setItems(FXCollections.observableArrayList(adocoes));
            txtFiltroAdotanteId.clear();
        } catch (Exception e) {
            mostrarAlerta("Erro ao Carregar", "Não foi possível carregar a lista de adoções: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    // --- MÉTODOS "HELPER" PARA ABRIR FORMULÁRIOS (NOVOS) ---

    /**
     * Método auxiliar para abrir o formulário de Animal (tanto para Novo quanto Editar)
     * @param animal O animal a ser editado (ou 'null' se for um novo animal)
     * @return true se o formulário foi salvo, false se foi cancelado
     */
    private boolean abrirFormularioAnimal(Animal animal) {
        try {
            String fxmlPath = "/com/joseramos/sistemaadocao/view/AnimalForm.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                throw new IOException("Não foi possível encontrar 'AnimalForm.fxml' em " + fxmlPath);
            }

            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();

            if (animal == null) {
                dialogStage.setTitle("Cadastrar Novo Animal");
            } else {
                dialogStage.setTitle("Editar Animal");
            }

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(btnNovoAnimal.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AnimalFormController controller = loader.getController();
            controller.setAnimalService(this.animalService);
            controller.setDialogStage(dialogStage);

            // Se estamos editando, passa o animal para o controller
            if (animal != null) {
                controller.setAnimalParaEditar(animal);
            }

            dialogStage.showAndWait();
            return controller.isSalvo();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao Abrir", "Não foi possível carregar 'AnimalForm.fxml'.\nVerifique se o arquivo está em 'resources/com/joseramos/sistemaadocao/GUI/view/'", Alert.AlertType.ERROR);
            return false;
        }
    }

     // Método auxiliar para abrir o formulário de Adotante (tanto para Novo quanto Editar)

    private boolean abrirFormularioAdotante(Adotante adotante) {
        try {
            String fxmlPath = "/com/joseramos/sistemaadocao/view/AdotanteForm.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                throw new IOException("Não foi possível encontrar 'AdotanteForm.fxml' em " + fxmlPath);
            }

            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();

            if (adotante == null) {
                dialogStage.setTitle("Cadastrar Novo Adotante");
            } else {
                dialogStage.setTitle("Editar Adotante");
            }

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(btnNovoAdotante.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AdotanteFormController controller = loader.getController();
            controller.setAdotanteService(this.adotanteService);
            controller.setDialogStage(dialogStage);

            // Se estamos editando, passa o adotante para o controller
            if (adotante != null) {
                controller.setAdotanteParaEditar(adotante);
            }

            dialogStage.showAndWait();
            return controller.isSalvo();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao Abrir", "Não foi possível carregar 'AdotanteForm.fxml'.\nVerifique se o arquivo está em 'resources/com/joseramos/sistemaadocao/GUI/view/'", Alert.AlertType.ERROR);
            return false;
        }
    }


    // Método de carregamento de dados
    private void carregarAnimais() {
        List<Animal> animais = animalService.listarAnimais();
        animaisTable.setItems(FXCollections.observableArrayList(animais));
    }

    private void carregarAdotantes() {
        List<Adotante> adotantes = adotanteService.listarAdotantes();
        adotantesTable.setItems(FXCollections.observableArrayList(adotantes));
    }

    // MÉTODO UTILITÁRIO PARA POPUPS

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}