package com.lmu.SlitherThink.boutonsAction;


import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class Partie extends ChangementFenetre{

    public static String dernierMode = "libre";

    public static void setDernierMode(String vue) {
        dernierMode = vue;
    }

    @FXML
    public void menuPause(ActionEvent event) {
        changerFenetre(event, "pause");
    }

    @FXML
    private void aide(ActionEvent event) {
        System.out.println("Aide demandée pour la partie libre");}

    public void relancerJeu(){
    }
}




