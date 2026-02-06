package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class ChoixMode extends ChangementFenetre {

    @FXML
    private void aventure(ActionEvent event) {
        handleSceneChange(event, "/fxml/choixPartieAventure.fxml");
    }

    @FXML
    private void libre(ActionEvent event) {
        handleSceneChange(event, "/fxml/choixDifficulte.fxml");
    }

    @FXML
    private void retour(ActionEvent event) {
        handleSceneChange(event, "/fxml/menuAccueil.fxml");
    }
}
