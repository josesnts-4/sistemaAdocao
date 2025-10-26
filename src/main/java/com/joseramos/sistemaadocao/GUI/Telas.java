package com.joseramos.sistemaadocao.GUI;

import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.entidades.Cachorro;
import com.joseramos.sistemaadocao.entidades.Gato;
import com.joseramos.sistemaadocao.entidades.StatusAnimal;
import com.joseramos.sistemaadocao.repository.AnimalRepository;
import com.joseramos.sistemaadocao.repository.AnimalRepositoryImpl;
import com.joseramos.sistemaadocao.service.AnimalService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

    public class Telas extends Application {
            // 1. Inicializa o Serviço e Repositório
            // A GUI DEVE usar o mesmo serviço/repositório
            private final AnimalRepository animalRepository = new AnimalRepositoryImpl();
            private final AnimalService animalService = new AnimalService(animalRepository);

            // Componentes da UI
            private final TableView<Animal> animalTable = new TableView<>();
            private ObservableList<Animal> animalData;

            // Campos do Formulário
            private TextField idField = new TextField();
            private TextField nomeField = new TextField();
            private TextField idadeField = new TextField();
            private TextField racaField = new TextField();
            private ComboBox<String> tipoBox = new ComboBox<>();
            private ComboBox<StatusAnimal> statusBox = new ComboBox<>();
            private TextField pesoGatoField = new TextField();
            private TextField pesoCachorroField = new TextField();
            private TextField treinadoField = new TextField();
            private CheckBox ariscoButton = new CheckBox("É arisco?");
            private TextField sexoField = new TextField();
            private TextField porteField = new TextField();
            private TextField descricaoField = new TextField();

            public static void main(String[] args) {
                launch(args);
            }

            @Override
            public void start(Stage primaryStage) {
                primaryStage.setTitle("Sistema de Adoção de Animais");

                BorderPane root = new BorderPane();
                root.setPadding(new Insets(10));

                // 1. Configurar Tabela (Centro)
                setupTable();
                root.setCenter(animalTable);

                // 2. Criar Formulário de CRUD (Direita)
                VBox formBox = createCrudForm();
                root.setRight(formBox);

                // 3. Carregar dados iniciais
                refreshTable();

                // 4. Configurar listener para popular o form ao selecionar
                setupTableSelectionListener();

                Scene scene = new Scene(root, 800, 500);
                primaryStage.setScene(scene);
                primaryStage.show();
            }

            /**
             * Configura as colunas da Tabela de Animais.
             */
            private void setupTable() {
                // Coluna ID
                TableColumn<Animal, Integer> idCol = new TableColumn<>("ID");
                idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

                // Coluna Nome
                TableColumn<Animal, String> nomeCol = new TableColumn<>("Nome");
                nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

                // Coluna Idade
                TableColumn<Animal, Integer> idadeCol = new TableColumn<>("Idade");
                idadeCol.setCellValueFactory(new PropertyValueFactory<>("idade"));

                // Coluna Raça
                TableColumn<Animal, String> racaCol = new TableColumn<>("Raça");
                racaCol.setCellValueFactory(new PropertyValueFactory<>("raca")); // Assumindo que 'raca' existe em Animal

                // Coluna Status
                TableColumn<Animal, String> statusCol = new TableColumn<>("Status");
                statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

                // Coluna Tipo (Cachorro/Gato)
                TableColumn<Animal, String> tipoCol = new TableColumn<>("Tipo");
                tipoCol.setCellValueFactory(cellData ->
                        new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClass().getSimpleName())
                );

                animalTable.getColumns().addAll(idCol, nomeCol, idadeCol, racaCol, statusCol, tipoCol);
            }

            /**
             * Cria o VBox com o formulário de entrada e os botões de ação.
             */
            private VBox createCrudForm() {
                VBox vbox = new VBox(10);
                vbox.setPadding(new Insets(10));

                // Layout do Formulário
                GridPane grid = new GridPane();
                grid.setVgap(10);
                grid.setHgap(10);

                grid.add(new Label("ID:"), 0, 0);
                grid.add(idField, 1, 0);
                grid.add(new Label("Nome:"), 0, 1);
                grid.add(nomeField, 1, 1);
                grid.add(new Label("Idade:"), 0, 2);
                grid.add(idadeField, 1, 2);
                grid.add(new Label("Raça:"), 0, 3);
                grid.add(racaField, 1, 3);
                grid.add(new Label("Tipo:"), 0, 4);
                grid.add(new Label("Peso Gato:"), 0, 6);
                grid.add(pesoGatoField, 1, 6);
                grid.add(new Label("Arisco:"), 0, 7);
                grid.add(ariscoButton, 1, 7);
                grid.add(new Label("Peso Cachorro:"), 0, 8);
                grid.add(pesoCachorroField, 1, 8);
                grid.add(new Label("Treinado:"), 0, 9);
                grid.add(treinadoField, 1, 9);
                grid.add(new Label("Sexo:"), 0, 10);
                grid.add(sexoField, 1, 10);
                grid.add(new Label("Porte:"), 0, 11);
                grid.add(porteField, 1, 11);
                grid.add(new Label("Descrição:"), 0, 12);
                grid.add(descricaoField, 1, 12);
                tipoBox.getItems().addAll("Cachorro", "Gato");
                grid.add(tipoBox, 1, 4);

                grid.add(new Label("Status:"), 0, 5);
                statusBox.getItems().setAll(StatusAnimal.values());
                statusBox.setValue(StatusAnimal.DISPONIVEL);
                grid.add(statusBox, 1, 5);

                idField.setPromptText("ID (para atualizar/remover)");

                // Layout dos Botões
                HBox buttonBox = new HBox(10);
                Button cadastrarButton = new Button("Cadastrar");
                Button atualizarButton = new Button("Atualizar");
                Button removerButton = new Button("Remover");
                Button limparButton = new Button("Limpar");

                buttonBox.getChildren().addAll(cadastrarButton, atualizarButton, removerButton, limparButton);

                // --- AÇÕES DOS BOTÕES ---

                // C (Create)
                cadastrarButton.setOnAction(e -> handleCadastrar());

                // U (Update)
                atualizarButton.setOnAction(e -> handleAtualizar());

                // D (Delete)
                removerButton.setOnAction(e -> handleRemover());

                // Ação de Limpar
                limparButton.setOnAction(e -> limparCampos());

                vbox.getChildren().addAll(new Label("Gerenciar Animal"), grid, buttonBox);
                return vbox;
            }

            /**
             * Atualiza a tabela com os dados mais recentes do repositório.
             * R (Read)
             */
            private void refreshTable() {
                animalData = FXCollections.observableArrayList(animalService.listarAnimais());
                animalTable.setItems(animalData);
            }

            /**
             * Popula o formulário quando um item da tabela é selecionado.
             */
            private void setupTableSelectionListener() {
                animalTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        popularFormulario(newSelection);
                    }
                });
            }

            // --- LÓGICA DE NEGÓCIO (HANDLERS) ---

            private void handleCadastrar() {
                try {
                    System.out.println("Qual tipo de animal você quer cadastrar?");
                    String tipo = tipoBox.getValue();
                    if(tipo == null) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um tipo de animal.");
                        return;
                    }
                    String nome = nomeField.getText();
                    String raca = racaField.getText();
                    StatusAnimal status = statusBox.getValue();
                    String sexo = sexoField.getText();
                    String descricao = descricaoField.getText();


                    Integer idade;
                    try {
                        idade = Integer.parseInt(idadeField.getText());
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Idade deve ser um número.");
                        return;
                    }
                    Animal novoAnimal;
                    if ("Cachorro".equals(tipo)) {
                        String treinado = treinadoField.getText();
                        String pesoCachorro = pesoCachorroField.getText();
                        String porte = porteField.getText();
                        novoAnimal = new Cachorro(nome, raca, idade, sexo, descricao, tipo, porte, treinado, pesoCachorro);
                    }
                    else if ("Gato".equals(tipo)) {
                        String pesoGato = pesoGatoField.getText();
                        Boolean arisco = ariscoButton.isSelected();
                        novoAnimal = new Gato(nome, raca, idade, sexo, descricao, pesoGato, arisco);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Tipo de animal inválido.");
                        return;
                    }

                    animalService.cadastrarAnimal(novoAnimal);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Animal cadastrado!");
                    refreshTable();
                    limparCampos();

                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "ID e Idade devem ser números.");
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar: " + ex.getMessage());
                }
            }

            private void handleAtualizar() {
                Animal animalSelecionado = animalTable.getSelectionModel().getSelectedItem();
                if (animalSelecionado == null) {
                    showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um animal para atualizar.");
                    return;
                }

                try {
                    // Atualiza o objeto selecionado com os dados do formulário
                    animalSelecionado.setNome(nomeField.getText());
                    animalSelecionado.setIdade(Integer.parseInt(idadeField.getText()));
                    animalSelecionado.setRaca(racaField.getText());
                    animalSelecionado.setStatus(statusBox.getValue());

                    // O tipo (Cachorro/Gato) não pode ser mudado

                    animalService.atualizarAnimal(animalSelecionado);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Animal atualizado!");
                    refreshTable();
                    limparCampos();

                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Idade deve ser um número.");
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar: " + ex.getMessage());
                }
            }

            private void handleRemover() {
                Animal selecionado = animalTable.getSelectionModel().getSelectedItem();
                    if (selecionado == null) {
                    showAlert(Alert.AlertType.WARNING, "Aviso", "Selecione um animal para remover.");
                    return;
                }


                try {
                    Integer id = selecionado.getId();
                    animalService.removerAnimal(id);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Animal removido!");
                    refreshTable();
                    limparCampos();
                } catch (Exception ex) {
                    // Ex: Capturar a regra de negócio que impede remover animal adotado
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover: " + ex.getMessage());
                }
            }

            // --- MÉTODOS UTILITÁRIOS ---

            private void popularFormulario(Animal animal) {
                idField.setText(String.valueOf(animal.getId()));
                nomeField.setText(animal.getNome());
                idadeField.setText(String.valueOf(animal.getIdade()));
                racaField.setText(animal.getRaca()); // Assumindo que getRaca existe
                statusBox.setValue(animal.getStatus()); // Assumindo que getStatus existe
                tipoBox.setValue(animal.getClass().getSimpleName());

                idField.setEditable(false); // ID não deve ser editado após selecionado
            }

            private void limparCampos() {
                idField.clear();
                nomeField.clear();
                idadeField.clear();
                racaField.clear();
                tipoBox.setValue(null);
                statusBox.setValue(StatusAnimal.DISPONIVEL);
                pesoGatoField.clear();
                pesoCachorroField.clear();
                treinadoField.clear();
                ariscoButton.setSelected(false);
                idField.setEditable(true);
                animalTable.getSelectionModel().clearSelection();
            }

            private void showAlert(Alert.AlertType type, String title, String message) {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }
        }
