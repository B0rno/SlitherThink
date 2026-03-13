package com.lmu.SlitherThink.boutonsAction;


import com.lmu.SlitherThink.GestionnaireVues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Pause extends ChangementFenetre{

    @FXML
    private void recommencer(ActionEvent event) {
        if("libre".equals(Partie.dernierMode)){
            changerFenetre(event, "partie");
        }
        else{
            PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
            if (controller != null) {
                controller.initialiserPartie(PartieTimer.getNumPartie());
            }
            changerFenetre(event, "partieTimer");
        }
    }

    @FXML 
    private void abandonner(ActionEvent event) {
        changerFenetre(event, "choixPartieAventure");
    }

    @FXML
    private void options(ActionEvent event) {
        Options.setVuePrecedente("pause");
        changerFenetre(event, "options");
    }

    @FXML
    private void techniques(ActionEvent event) {
        System.out.println("Techniques");
    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }

    @FXML
    private void reprendre(ActionEvent event) {
        
        if ("libre".equals(Partie.dernierMode)){
            changerFenetre(event, "partie");
        }
        else{
            PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
            if (controller != null) {
                // 2. Appeler une méthode que vous créez dans PartieController pour relancer le timer
                controller.relancerJeu(); 
            }
            changerFenetre(event, "partieTimer");
        }    
       
    }
    
}