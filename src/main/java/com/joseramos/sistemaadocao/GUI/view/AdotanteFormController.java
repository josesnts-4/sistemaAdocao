package com.joseramos.sistemaadocao.GUI.view;

import com.joseramos.sistemaadocao.entidades.Adotante;
import com.joseramos.sistemaadocao.service.AdotanteService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdotanteFormController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtEndereco;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private AdotanteService adotanteService;
    private Stage dialogStage;
    private boolean salvo = false;

    // (NOVO) Guarda o adotante que está sendo editado
    private Adotante adotanteParaEditar;

    public void setAdotanteService(AdotanteService adotanteService) {
        this.adotanteService = adotanteService;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // (NOVO) Método para injetar o adotante selecionado no formulário
    public void setAdotanteParaEditar(Adotante adotante) {
        this.adotanteParaEditar = adotante;

        // Preenche os campos do formulário
        txtNome.setText(adotante.getNome());
        txtCpf.setText(adotante.getCpf());
        txtEndereco.setText(adotante.getEndereco());

        // (O total de adoções não é editável por aqui)
    }

    public boolean isSalvo() {
        return salvo;
    }

    @FXML
    private void handleSalvar() {
        if (validarEntrada()) {
            try {
                String nome = txtNome.getText();
                String cpf = txtCpf.getText();
                String endereco = txtEndereco.getText();

                // (LÓGICA ATUALIZADA)
                if (adotanteParaEditar == null) {
                    // 1. MODO CRIAR (NOVO ADOTANTE)
                    Adotante novoAdotante = new Adotante(nome, cpf, endereco);
                    adotanteService.cadastrarAdotante(novoAdotante);

                } else {
                    // 2. MODO EDITAR (ATUALIZAR ADOTANTE)
                    adotanteParaEditar.setNome(nome);
                    adotanteParaEditar.setCpf(cpf);
                    adotanteParaEditar.setEndereco(endereco);
                    adotanteService.atualizarAdotante(adotanteParaEditar);
                }

                salvo = true;
                dialogStage.close();

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
        if (txtCpf.getText() == null || txtCpf.getText().isEmpty()) {
            msgErro += "CPF inválido!\n";
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