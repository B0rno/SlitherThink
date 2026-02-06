package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class ChoixPartieAventure extends ChangementFenetre {
    @FXML
    private void retour(ActionEvent event) {
        handleSceneChange(event, "/fxml/choixMode.fxml");
    }

    @FXML
    private void partie1(ActionEvent event) {
        handleSceneChange(event, "/fxml/finPartie.fxml");
    }
}
