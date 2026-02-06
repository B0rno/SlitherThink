package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.event.ActionEvent;

// On hérite de changementFenetre pour accéder à switchScene
public class ControlMenu extends changementFenetre {


    //boutons de la page menuTest.fxml
    @FXML
    private Button btnChangerCompte; //impossible de cliquer dessus, à fix
    @FXML
    private Button btnJouer;
    @FXML
    private Button btntutoriel;
    @FXML
    private Button btnLeaderboards;
    @FXML
    private Button btnoptions;
    @FXML
    private Button btnQuitter;


    //boutons de la page choixMode.fxml
    @FXML
    private Button btnRetourMenu;
    @FXML
    private Button btnAventure;
    @FXML
    private Button btnLibre;


    //boutons de la page choixDifficulte.fxml
    @FXML
    private Button btnFacile;
    @FXML
    private Button btnMoyen;
    @FXML
    private Button btnDifficile;
    @FXML
    private Button btnRetourChoix;


    
    //actions des boutons


    //actions des boutons de la page menuTest.fxml
    @FXML
    private void changerCompte(ActionEvent event) {
        System.out.println("Changer de compte");
    }
    @FXML
    private void jouer(ActionEvent event) {
        switchScene(btnJouer, "/fxml/choixMode.fxml");
    }
    @FXML
    private void tutoriel(ActionEvent event) {
        System.out.println("Tutoriel");
    }
    @FXML
    private void leaderboards(ActionEvent event) {
        switchScene(btnLeaderboards, "/fxml/leaderboards.fxml");

    }
    @FXML
    private void options(ActionEvent event) {
        System.out.println("options");
    }
    @FXML
    private void quitter(ActionEvent event) {
        Platform.exit(); 
        System.exit(0); 
    }


    //actions des boutons de la page choixMode.fxml
    @FXML
    private void retourMenu(ActionEvent event) {
        // Utilisation de la méthode héritée pour revenir au menu principal
        switchScene(btnRetourMenu, "/fxml/menuTest.fxml");
    }
    @FXML
    private void aventure(ActionEvent event) {
        System.out.println("Mode Aventure");    
    }
    @FXML
    private void libre(ActionEvent event) {
        switchScene(btnLibre, "/fxml/choixDifficulte.fxml");

    }


    //actions des boutons de la page choixDifficulte.fxml
    @FXML
    private void facile(ActionEvent event) {
        System.out.println("Difficulté Facile");    
    }
    @FXML
    private void moyen(ActionEvent event) {
        System.out.println("Difficulté Moyen"); 
    }
    @FXML
    private void difficile(ActionEvent event) { 
        System.out.println("Difficulté Difficile"); 
    }
    @FXML
    private void retourChoix(ActionEvent event) {
        switchScene(btnRetourChoix, "/fxml/choixMode.fxml");
    }
    
}