package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class Pseudo extends ChangementFenetre{
    //@FXML private TextField txtPseudo;
    @FXML private Button btnConfirmer;


    @FXML
    private void confirmer(ActionEvent event) {
        /*String pseudo = txtPseudo.getText();
        if (!pseudo.isEmpty()) {
            System.out.println("Pseudo : " + pseudo);
        }*/
       System.out.println("Confirmer pseudo");
       handleSceneChange(event, "/fxml/menuAccueil.fxml");

    }

    /*
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> txtPseudo.requestFocus());
        
        txtPseudo.textProperty().addListener((obs, old, current) -> {
            if (current.length() > 12) txtPseudo.setText(old);
        });
    }
        */
}