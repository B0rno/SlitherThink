package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

/**
 * Cette classe gère les interactions utilisateur permettant de choisir le niveau de difficult en mode libre.
 * * @author Ilann
 */
public class ChoixDifficulte extends ChangementFenetre {

    /**
     * Gère l'action du bouton pour lancer une partie en mode "Facile".
     * * @param event L'événement déclenché par le clic sur le bouton facile.
     */
    @FXML
    private void facile(ActionEvent event) {
        choixPartieLibre(event, "Facile");
    }

    /**
     * Gère l'action du bouton pour lancer une partie en mode "Moyen".
     * * @param event L'événement déclenché par le clic sur le bouton moyen.
     */
    @FXML
    private void moyen(ActionEvent event) {
        choixPartieLibre(event, "Moyen");
    }

    /**
     * Gère l'action du bouton pour lancer une partie en mode "Difficile".
     * * @param event L'événement déclenché par le clic sur le bouton difficile.
     */
    @FXML
    private void difficile(ActionEvent event) {
        choixPartieLibre(event, "Difficile");
    }

    /**
     * Gère l'action du bouton de retour.
     * Redirige l'utilisateur vers le menu de choix du mode de jeu.
     * * @param event L'événement déclenché par le clic sur le bouton retour.
     */
    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }
}