package com.joseramos.sistemaadocao;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class CadastroAnimalController {
    @FXML
    private RadioButton btnOutro;
    @FXML
    private TextField txtOutro;
    @FXML
    private void mostrarCampoOutro(){
        txtOutro.setVisible(true);
        txtOutro.requestFocus();
    }
}
