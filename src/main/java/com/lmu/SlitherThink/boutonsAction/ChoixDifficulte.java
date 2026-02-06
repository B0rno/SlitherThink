package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class ChoixDifficulte extends ChangementFenetre {
    @FXML
    private void facile(ActionEvent event) {
        System.out.println("Difficulté facile");
    }

    @FXML
    private void moyen(ActionEvent event) {
        System.out.println("Difficulté moyen");
    }

    @FXML
    private void difficile(ActionEvent event) {
        System.out.println("Difficulté difficile");
    }

    @FXML
    private void retour(ActionEvent event) {
        handleSceneChange(event, "/fxml/choixMode.fxml");
    }
}
