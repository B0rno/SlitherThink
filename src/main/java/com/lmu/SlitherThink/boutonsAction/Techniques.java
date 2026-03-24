package com.lmu.SlitherThink.boutonsAction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

import javafx.scene.control.MenuItem;

public class Techniques extends ChangementFenetre{

    private static String vuePrecedente = "menuAccueil";


    public static void setVuePrecedente(String vue) {
        vuePrecedente = vue;
    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, vuePrecedente);
    }
}