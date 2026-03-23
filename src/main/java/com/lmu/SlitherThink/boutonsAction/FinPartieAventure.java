package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FinPartieAventure extends ChangementFenetre {

    @FXML private Label txtTemps;
    @FXML private Label txtTempsMax;    
    @FXML private Label txtAides;
    @FXML private Label txtComplete;

    @FXML private ImageView etoile1; // Correspond à l'ID dans le FXML
    @FXML private ImageView etoile2;
    @FXML private ImageView etoile3;

    // Chemins vers les images (à adapter selon ton projet)
    private final Image IMAGE_PLEINE = new Image(getClass().getResourceAsStream("/images/etoilePleine.png"));
    private final Image IMAGE_VIDE = new Image(getClass().getResourceAsStream("/images/etoileVide.png"));

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


    private boolean comparerTemps(String temps, String tempsMax) {
        String [] partsTemps = temps.split(":");
        String [] partsTempsMax = tempsMax.split(":");
        int totalSecondes = Integer.parseInt(partsTemps[0]) * 90 + Integer.parseInt(partsTemps[1]);
        int totalSecondesMax = Integer.parseInt(partsTempsMax[0]) * 60 + Integer.parseInt(partsTempsMax[1]);
        
        return totalSecondes <= totalSecondesMax;
    }

    /**
     * Met à jour l'affichage et calcule les étoiles selon les critères :
     * Etoile 1 : Partie complétée.
     * Etoile 2 : Temps respecté ou Aides respectées.
     * Etoile 3 : Tout est valide 
     */
    public void mettreDonnees(int aides, int aidesMax, String temps, String tempsMax, boolean complete) {
        // Mise à jour des textes
        txtTemps.setText(temps);
        txtAides.setText("Aides utilisées : " + aides + " / " + aidesMax);
        txtTempsMax.setText("Temps : " + temps + " / " + tempsMax);
        txtComplete.setText("Partie complétée : " + (complete ? "oui" : "non"));

        // Calcul des conditions
        boolean aidesRespectees = (aides <= aidesMax);
        boolean tempsRespecte = comparerTemps(temps, tempsMax); 
        
        boolean e1 = complete;
        boolean e2 = complete && (aidesRespectees || tempsRespecte);
        boolean e3 = complete && aidesRespectees && tempsRespecte;

        
        appliquerEtoile(etoile1, e1);
        appliquerEtoile(etoile2, e2);
        appliquerEtoile(etoile3, e3);
    }

    private void appliquerEtoile(ImageView iv, boolean pleine) {
        if (iv != null) {
            iv.setImage(pleine ? IMAGE_PLEINE : IMAGE_VIDE);
        }
    }
}