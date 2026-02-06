package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class FinPartie extends ChangementFenetre {
    @FXML
    private void partieSuivante(ActionEvent event) {
        System.out.println("Partie suivante");
    }

    @FXML
    private void recommencer(ActionEvent event) {
        System.out.println("Recommencer");
    }

    @FXML
    private void choixPartieAventure(ActionEvent event) {
        handleSceneChange(event, "/fxml/choixPartieAventure.fxml");
    }

    @FXML
    private void menu(ActionEvent event) {
        handleSceneChange(event, "/fxml/menuAccueil.fxml");
    }


}
