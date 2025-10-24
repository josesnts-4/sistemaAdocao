package com.joseramos.sistemaadocao.casoPrecise;

import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;

public class CadastroAnimalController extends Application {

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Cadastro de Animais - Sistema de Adoção");

            // --- Painel principal ---
            VBox root = new VBox(15);
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: #0D1B2A; -fx-background-radius: 15px;"); // fundo geral

            // --- Título ---
            Label lblTitulo = new Label("Cadastro de Animais");
            lblTitulo.setAlignment(Pos.CENTER);
            lblTitulo.setMaxWidth(Double.MAX_VALUE);
            lblTitulo.setStyle("-fx-font-size: 24px; -fx-text-fill: #ffffff;");

            // --- Campos de entrada ---
            TextField txtNome = new TextField();
            txtNome.setPromptText("Nome");
            txtNome.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B0C4DE; -fx-border-radius: 5px;");

            TextField txtRaca = new TextField();
            txtRaca.setPromptText("Raça");
            txtRaca.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B0C4DE; -fx-border-radius: 5px;");

            TextField txtIdade = new TextField();
            txtIdade.setPromptText("Idade");
            txtIdade.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B0C4DE; -fx-border-radius: 5px;");

            TextField txtEstado = new TextField();
            txtEstado.setPromptText("Foi encontrado?");
            txtEstado.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B0C4DE; -fx-border-radius: 5px;");

            // --- RadioButtons para tipo de animal ---
            ToggleGroup grupoAnimais = new ToggleGroup();
            RadioButton rbCachorro = new RadioButton("Cachorro");
            rbCachorro.setStyle("-fx-background-color: #0D1B2A; -fx-text-fill: #ffffff;");
            rbCachorro.setToggleGroup(grupoAnimais);
            RadioButton rbGato = new RadioButton("Gato");
            rbGato.setStyle("-fx-background-color: #0D1B2A; -fx-text-fill: #ffffff;");
            rbGato.setToggleGroup(grupoAnimais);
            RadioButton rbOutro = new RadioButton("Outro");
            rbOutro.setStyle("-fx-background-color: #0D1B2A; -fx-text-fill: #ffffff;");
            rbOutro.setToggleGroup(grupoAnimais);

            TextField txtOutroAnimal = new TextField();
            txtOutroAnimal.setPromptText("Especifique");
            txtOutroAnimal.setDisable(true);
            txtOutroAnimal.setStyle("-fx-background-color: #0D1B2A; -fx-border-color: #B0C4DE; -fx-border-radius: 5px;");

            grupoAnimais.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle == rbOutro) {
                    txtOutroAnimal.setDisable(false);
                } else {
                    txtOutroAnimal.setDisable(true);
                }
            });

            // Ativar campo "Outro" apenas se selecionado
            rbOutro.setOnAction(e -> txtOutroAnimal.setDisable(!rbOutro.isSelected()));

            HBox hboxRadio = new HBox(10, rbCachorro, rbGato, rbOutro, txtOutroAnimal);

            // --- Botões ---
            Button btnCadastrar = new Button("Cadastrar");
            btnCadastrar.setStyle("-fx-background-color: #2E8B57; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");

            Button btnCancelar = new Button("Cancelar");
            btnCancelar.setStyle("-fx-background-color: #A9A9A9; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");

            HBox hboxBotoes = new HBox(10, btnCadastrar, btnCancelar);

            // --- Mensagem de status ---
            Label lblStatus = new Label("");
            lblStatus.setStyle("-fx-font-weight: bold;");

            // Exemplo de ação do botão
            btnCadastrar.setOnAction(e -> {
                if (txtNome.getText().isEmpty() || txtRaca.getText().isEmpty() || txtIdade.getText().isEmpty()) {
                    lblStatus.setText("Preencha todos os campos obrigatórios!");
                    lblStatus.setTextFill(Color.web("#FF4C4C")); // vermelho
                } else {
                    lblStatus.setText("Cadastro realizado com sucesso!");
                    lblStatus.setTextFill(Color.web("#32CD32")); // verde
                }
            });

            // --- Montagem da tela ---
            root.getChildren().addAll(lblTitulo, txtNome, txtRaca, txtIdade, txtEstado, hboxRadio, hboxBotoes, lblStatus);

            Scene scene = new Scene(root, 500, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }


}
