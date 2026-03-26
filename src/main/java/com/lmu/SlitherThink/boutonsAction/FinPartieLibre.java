package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;


import javafx.event.ActionEvent;

public class FinPartieLibre extends ChangementFenetre {
    @FXML
    public void newGame(ActionEvent event) {
        changerFenetre(event, "choixDifficulte");
    }

    @FXML
    public void menu(ActionEvent event) {
        changerFenetre(event, "menuAccueil");

    }   
}
