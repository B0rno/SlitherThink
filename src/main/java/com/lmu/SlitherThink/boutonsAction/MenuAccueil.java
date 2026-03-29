package com.lmu.SlitherThink.boutonsAction;


import com.lmu.SlitherThink.App;
import com.lmu.SlitherThink.GestionnaireVues;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuAccueil extends ChangementFenetre{
    @FXML
    private void changerCompte(ActionEvent event) {
        changerFenetre(event, "pseudo");
    }

    @FXML 
    private void jouer(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }

    @FXML
    private void tutoriel(ActionEvent event) {

        PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
    
        if (controller != null) {
            controller.lancerTutoriel();
            App.changerVue("partieTimer");
            
        } else {
            System.err.println("Erreur : Le contrôleur de la partie est introuvable !");
        }
    }

    @FXML
    private void leaderboards(ActionEvent event) {
        Leaderboards controller = (Leaderboards) GestionnaireVues.getController("leaderboards");
        if (controller != null) {
            controller.rafraichirDonnees();
            App.changerVue("leaderboards");
        } else {
            System.err.println("Erreur : Le contrôleur leaderboards est introuvable !");
        }
    }

    @FXML
    private void techniques(ActionEvent event) {
        Techniques.setVuePrecedente("menuAccueil");
        changerFenetre(event, "techniques");
    }

    @FXML
    private void quitter(ActionEvent event) {
        Platform.exit(); 
        System.exit(0); 
    }
    @FXML
    private void pause(ActionEvent event) {
        System.out.println("Pause");
        changerFenetre(event, "pause");
    }
}
