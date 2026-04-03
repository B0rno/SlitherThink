package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

/**
 * Contrôleur de la vue de fin de partie pour le mode Libre.
 * Cette classe gère les options post-partie, permettant au joueur de relancer 
 * une nouvelle partie ou de retourner à l'accueil.
 * * @author Ilann
 */
public class FinPartieLibre extends ChangementFenetre {

    /**
     * Gère l'action du bouton "Nouvelle Partie".
     * Redirige l'utilisateur vers l'écran de sélection de la difficulté du mode libre.
     * * @param event L'événement déclenché par le clic sur le bouton.
     */
    @FXML
    public void newGame(ActionEvent event) {
        changerFenetre(event, "choixDifficulte");
    }

    /**
     * Gère l'action du bouton "Menu".
     * Redirige l'utilisateur vers le menu d'accueil principal de l'application.
     * * @param event L'événement déclenché par le clic sur le bouton.
     */
    @FXML
    public void menu(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }   
}