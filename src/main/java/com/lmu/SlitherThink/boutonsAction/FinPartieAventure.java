package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class FinPartieAventure extends ChangementFenetre {
    @FXML
    private void partieSuivante(ActionEvent event) {
        System.out.println("Partie suivante");
    }

    @FXML
    private void recommencer(ActionEvent event) {
        System.out.println("Recommencer");
    }

    @FXML
    private void choixPartieAventure(ActionEvent event) {
        changerFenetre(event, "choixPartieAventure");
    }

    @FXML
    private void menu(ActionEvent event) {
        changerFenetre(event, "menuAccueil");

    }

    @FXML private Label txtTemps;
    @FXML private Label txtTempsMax;    
    @FXML private Label txtAides;
    @FXML private Label txtComplete;



    public void mettreDonnees(int aides, int aidesMax, String temps, String tempsMax, boolean complete) {
        /*
        aides = aides utilisées
        aidesMax = aides à ne pas dépasser pour avoir l'étoile
        temps = temps utilisé
        tempsMax = temps à ne pas dépasser pour avoir l'étoile
        complete = partie completee ou non
        */
        txtTemps.setText(temps);
        txtAides.setText("Aides utilisées : " + aides + " / " + aidesMax);
        txtTempsMax.setText("Temps : " + temps + " / " + tempsMax);

        if (complete) {
            txtComplete.setText("Partie complétée : oui");
        } else {
            txtComplete.setText("Partie complétée : non");
        }   
    }
}
