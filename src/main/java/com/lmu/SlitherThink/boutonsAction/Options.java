package com.lmu.SlitherThink.boutonsAction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

import javafx.scene.control.MenuItem;

public class Options extends ChangementFenetre{

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
        changerFenetre(event, "menuAccueil");
    }
}