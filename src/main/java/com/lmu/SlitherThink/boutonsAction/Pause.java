package com.lmu.SlitherThink.boutonsAction;


import com.lmu.SlitherThink.GestionnaireVues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Pause extends ChangementFenetre{

    @FXML
    private void recommencer(ActionEvent event) {
        //ne pas sauvegarder
        if("libre".equals(Partie.dernierMode)){
            Partie controller = (Partie) GestionnaireVues.getController("partie");
            if (controller != null) {
                controller.initialiserPartie(Partie.getGrilleEnCours());
            }
            changerFenetre(event, "partie");
        }
        else{
            PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
            if (controller != null) {
                if ("aventure".equals(Partie.dernierMode)){
                    controller.initialiserPartie(PartieTimer.getNumPartie());
                }

                //tutoriel
                else{
                    controller.lancerTutoriel();
                }
                changerFenetre(event, "partieTimer");
            }

            else{
                System.out.println("Erreur : Impossible de récupérer le controller de PartieTimer");    
            }
        }
    }

    @FXML 
    private void abandonner(ActionEvent event) {
        //ne pas faire de sauvegarde
        changerFenetre(event, "menuAccueil");
    }


    @FXML
    private void techniques(ActionEvent event) {
        //TODO faire l'affichage des techniques
        Techniques.setVuePrecedente("menuAccueil");
        changerFenetre(event, "techniques");
    }

    @FXML
    private void menuPrincipal(ActionEvent event) {
        //TODO faire sauvegarde
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
                controller.reprendrePartie(); 
            }
            changerFenetre(event, "partieTimer");
        }    
       
    }
    
}