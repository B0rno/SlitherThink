package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class Pseudo extends ChangementFenetre{
    @FXML private TextField txtPseudo;
    @FXML private Button btnConfirmer;


    @FXML
    private void confirmer(ActionEvent event) {
        String pseudo = txtPseudo.getText().trim();

        if (!pseudo.isEmpty()) {
            System.out.println("Pseudo : " + pseudo);
            changerFenetre(event, "menuAccueil");
        }
    }

    @FXML
    public void initialize() {
        btnConfirmer.disableProperty().bind(txtPseudo.textProperty().isEmpty());

        txtPseudo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 20) {
                txtPseudo.setText(oldValue);
            }
        });
    }
}