package com.lmu.SlitherThink.boutonsAction;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Pause extends ChangementFenetre{
    @FXML
    private void recommencer(ActionEvent event) {
        System.out.println("Recommencer");
    }

    @FXML 
    private void abandonner(ActionEvent event) {
        System.out.println("Abandopnner");
    }

    @FXML
    private void options(ActionEvent event) {
        System.out.println("Options");  
    }

    @FXML
    private void techniques(ActionEvent event) {
        System.out.println("Techniques");
    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }
}