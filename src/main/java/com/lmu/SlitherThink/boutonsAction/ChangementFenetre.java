package com.lmu.SlitherThink.boutonsAction;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import com.lmu.SlitherThink.GestionnaireVues;

public abstract class ChangementFenetre {

    protected void changerVueFinPartie(Button sourceButton, int aides, int aidesMax, String temps, String tempsMax, boolean succes) {
        Parent root = GestionnaireVues.getView("finPartieAventure");
        FinPartieAventure controller = (FinPartieAventure) GestionnaireVues.getController("finPartieAventure");
        if (root != null && controller != null) {
            controller.mettreDonnees(aides, aidesMax, temps, tempsMax, succes);
    
            sourceButton.getScene().setRoot(root);
        } else {
            System.err.println("La vue finPartie n'est pas prête !");
        }
    }

    protected void changerVue(Button sourceButton, String viewName) {
        Parent root = GestionnaireVues.getView(viewName);
        if (root != null) {
    
            Scene scene = sourceButton.getScene();

            scene.setRoot(root);
        } else {
            System.err.println("La vue " + viewName + " n'a pas été préchauffée !");
        }
    }

    protected void changerFenetre(ActionEvent event, String viewName) {
        Button btn = (Button) event.getSource();
        changerVue(btn, viewName);
    }
    
}