package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class Leaderboards extends ChangementFenetre {

    @FXML
    private MenuButton menuLeaderboards; 

    @FXML
    private void modifChamp(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        
        menuLeaderboards.setText(item.getText());
        
    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }
}