package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class ChoixPartieAventure extends ChangementFenetre {
    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }

    @FXML
    private void partie1(ActionEvent event) {
        Button btn = (Button) event.getSource();
        int aides = 2;
        int aidesMax = 0;
        String temps = "05:46";
        String tempsMax = "08:00";
        boolean succes = true;

        changerVueFinPartie(aides, aidesMax, temps, tempsMax, succes);
    }
}
