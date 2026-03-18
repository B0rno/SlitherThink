package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class ChoixDifficulte extends ChangementFenetre {
    @FXML
    private void facile(ActionEvent event) {
        //choixPartieLibre est dans le fichier ChangementFenetre
        choixPartieLibre(event, "Facile");
    }

    @FXML
    private void moyen(ActionEvent event) {
        choixPartieLibre(event, "Moyen");
    }

    @FXML
    private void difficile(ActionEvent event) {
        choixPartieLibre(event, "Difficile");
    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }
}
