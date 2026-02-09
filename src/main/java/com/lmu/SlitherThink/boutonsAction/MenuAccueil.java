package com.lmu.SlitherThink.boutonsAction;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuAccueil extends ChangementFenetre{
    @FXML
    private void changerCompte(ActionEvent event) {
        handleSceneChange(event, "/fxml/pseudo.fxml");
    }

    @FXML 
    private void jouer(ActionEvent event) {
        handleSceneChange(event, "/fxml/choixMode.fxml");
    }

    @FXML
    private void tutoriel(ActionEvent event) {
        System.out.println("Tutoriel");
    }

    @FXML
    private void leaderboards(ActionEvent event) {
        System.out.println("Leaderboards");
        handleSceneChange(event, "/fxml/leaderboards.fxml");
    }

    @FXML
    private void options(ActionEvent event) {
        System.out.println("Options");  
    }

    @FXML
    private void quitter(ActionEvent event) {
        Platform.exit(); 
        System.exit(0); 
    }
    @FXML
    private void pause(ActionEvent event) {
        System.out.println("Pause");
        handleSceneChange(event, "/fxml/pause.fxml");
    }
}
