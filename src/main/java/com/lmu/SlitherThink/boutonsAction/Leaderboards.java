package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class Leaderboards extends ChangementFenetre {

   

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }
}
