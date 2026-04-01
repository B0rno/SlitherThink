package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class ChoixMode extends ChangementFenetre {

    @FXML
    private void aventure(ActionEvent event) {
        changerFenetre(event, "choixPartieAventure");
    }

    @FXML
    private void libre(ActionEvent event) {
        changerFenetre(event, "choixDifficulte");

    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "menuAccueil");

    }
}
