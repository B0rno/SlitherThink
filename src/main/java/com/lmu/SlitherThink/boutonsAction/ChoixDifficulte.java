package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class ChoixDifficulte extends ChangementFenetre {
    @FXML
    private void facile(ActionEvent event) {
        choixPartieLibre(event, "facile");
    }

    @FXML
    private void moyen(ActionEvent event) {
        choixPartieLibre(event, "moyen");
    }

    @FXML
    private void difficile(ActionEvent event) {
        choixPartieLibre(event, "difficile");
    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }
}
