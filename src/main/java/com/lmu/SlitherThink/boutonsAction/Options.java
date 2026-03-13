package com.lmu.SlitherThink.boutonsAction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

import javafx.scene.control.MenuItem;

public class Options extends ChangementFenetre{

    private static String vuePrecedente = "menuAccueil";


    public static void setVuePrecedente(String vue) {
        vuePrecedente = vue;
    }

    @FXML
    private MenuButton languageMenuButton;

    @FXML
    private void changerLangue(ActionEvent event) {
        MenuItem itemSelectionne = (MenuItem) event.getSource();
        
        String nouvelleLangue = itemSelectionne.getText();
        
        languageMenuButton.setText(nouvelleLangue);
    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, vuePrecedente);
    }
}