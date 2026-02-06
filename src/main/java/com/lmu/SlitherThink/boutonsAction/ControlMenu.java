package com.lmu.SlitherThink.boutonsAction;


import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import java.net.URL;

// On hérite de changementFenetre pour accéder à switchScene
public class ControlMenu extends changementFenetre implements Initializable {


    //boutons de la page pseudo.fxml
    @FXML
    private Button btnConfirmer;
    @FXML
    private TextField txtPseudo;


    //boutons de la page menuTest.fxml
    @FXML
    private Button btnChangerCompte; 
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



    //actions des boutons de la page pseudo.fxml
    @FXML
    private void confirmer(ActionEvent event) {
        String pseudo = txtPseudo.getText();
        if (!pseudo.isEmpty()) {
            System.out.println("Pseudo : " + pseudo);
            switchScene(btnConfirmer, "/fxml/menuAccueil.fxml");
        }
    }

    //actions des boutons de la page menuTest.fxml
    @FXML
    private void changerCompte(ActionEvent event) {
        switchScene(btnJouer, "/fxml/pseudo.fxml");

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
        switchScene(btnRetourMenu, "/fxml/menuTest.fxml");
    }
    @FXML
    private void aventure(ActionEvent event) {
        switchScene(btnRetourMenu, "/fxml/choixPartie.fxml");
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
        Button boutonClique = (Button) event.getSource(); 
        switchScene(boutonClique, "/fxml/choixMode.fxml");
    }    

    //page pseudo
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (txtPseudo != null) {
            //limite le nombre de caractères à 12 
            txtPseudo.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.length() > 12) {
                    txtPseudo.setText(oldValue);
                }
            });   
        }
    }
}