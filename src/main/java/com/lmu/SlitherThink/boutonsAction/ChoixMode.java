package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

/**
 * Cette classe permet à l'utilisateur de naviguer vers le mode Aventure, 
 * le mode Libre (sélection de difficulté) ou de revenir au menu principal.
 * * @author Ilann
 */
public class ChoixMode extends ChangementFenetre {

    /**
     * Gère l'action du bouton "Aventure".
     * Redirige l'utilisateur vers l'écran de sélection des niveaux du mode aventure.
     * * @param event L'événement déclenché par le clic sur le bouton aventure.
     */
    @FXML
    private void aventure(ActionEvent event) {
        changerFenetre(event, "choixPartieAventure");
    }

    /**
     * Gère l'action du bouton "Libre".
     * Redirige l'utilisateur vers l'écran de sélection de la difficulté.
     * * @param event L'événement déclenché par le clic sur le bouton mode libre.
     */
    @FXML
    private void libre(ActionEvent event) {
        changerFenetre(event, "choixDifficulte");
    }

    /**
     * Gère l'action du bouton "Retour".
     * Redirige l'utilisateur vers le menu d'accueil de l'application.
     * * @param event L'événement déclenché par le clic sur le bouton retour.
     */
    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }
}