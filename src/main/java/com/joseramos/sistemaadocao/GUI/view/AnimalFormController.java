package com.joseramos.sistemaadocao.GUI.view;

import com.joseramos.sistemaadocao.entidades.Animal;
import com.joseramos.sistemaadocao.entidades.Cachorro;
import com.joseramos.sistemaadocao.entidades.Gato;
import com.joseramos.sistemaadocao.entidades.StatusAnimal;
import com.joseramos.sistemaadocao.service.AnimalService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AnimalFormController {

    @FXML private TextField txtNome;
    @FXML private TextField txtIdade;
    @FXML private TextField txtRaca;
    @FXML private ChoiceBox<String> choiceTipo;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private AnimalService animalService;
    private Stage dialogStage;
    private boolean salvo = false;

    // (NOVO) Guarda o animal que está sendo editado
    private Animal animalParaEditar;

    @FXML
    private void initialize() {
        choiceTipo.setItems(FXCollections.observableArrayList("CACHORRO", "GATO"));
    }

    public void setAnimalService(AnimalService animalService) {
        this.animalService = animalService;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSalvo() {
        return salvo;
    }

    // (NOVO) Método para injetar o animal selecionado no formulário
    public void setAnimalParaEditar(Animal animal) {
        this.animalParaEditar = animal;

        // Preenche os campos do formulário com os dados do animal
        txtNome.setText(animal.getNome());
        txtIdade.setText(String.valueOf(animal.getIdade()));
        txtRaca.setText(animal.getRaca());
        choiceTipo.setValue(animal.getTipo().toUpperCase());

        // Desabilita a troca de tipo (não se pode mudar um Cachorro para Gato)
        choiceTipo.setDisable(true);
    }

    @FXML
    private void handleSalvar() {
        if (validarEntrada()) {
            try {
                String nome = txtNome.getText();
                int idade = Integer.parseInt(txtIdade.getText());
                String raca = txtRaca.getText();

                // (LÓGICA ATUALIZADA)
                if (animalParaEditar == null) {
                    // 1. MODO CRIAR (NOVO ANIMAL)
                    String tipo = choiceTipo.getValue();
                    Animal novoAnimal;
                    if ("CACHORRO".equals(tipo)) {
                        novoAnimal = new Cachorro(nome, raca, idade);
                    } else {
                        novoAnimal = new Gato(nome, raca, idade);
                    }
                    novoAnimal.setStatus(StatusAnimal.DISPONIVEL);
                    animalService.cadastrarAnimal(novoAnimal);

                } else {
                    // 2. MODO EDITAR (ATUALIZAR ANIMAL)
                    animalParaEditar.setNome(nome);
                    animalParaEditar.setIdade(idade);
                    animalParaEditar.setRaca(raca);
                    // (Status e Tipo não são alterados na edição)
                    animalService.atualizarAnimal(animalParaEditar);
                }

                salvo = true;
                dialogStage.close();

            } catch (NumberFormatException e) {
                mostrarAlerta("Erro de Formato", "Idade deve ser um número.", Alert.AlertType.ERROR);
            } catch (Exception e) {
                mostrarAlerta("Erro ao Salvar", "Ocorreu um erro: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    private boolean validarEntrada() {
        String msgErro = "";
        if (txtNome.getText() == null || txtNome.getText().isEmpty()) {
            msgErro += "Nome inválido!\n";
        }
        if (txtIdade.getText() == null || txtIdade.getText().isEmpty()) {
            msgErro += "Idade inválida!\n";
        }
        if (txtRaca.getText() == null || txtRaca.getText().isEmpty()) {
            msgErro += "Raça inválida!\n";
        }
        if (choiceTipo.getValue() == null) {
            msgErro += "Selecione um Tipo!\n";
        }

        if (msgErro.isEmpty()) {
            return true;
        } else {
            mostrarAlerta("Campos Inválidos", msgErro, Alert.AlertType.ERROR);
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}