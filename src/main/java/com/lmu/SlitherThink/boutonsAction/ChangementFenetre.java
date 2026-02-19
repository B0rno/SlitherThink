package com.lmu.SlitherThink.boutonsAction;

import javafx.event.ActionEvent;
import com.lmu.SlitherThink.App;
import com.lmu.SlitherThink.GestionnaireVues;

public abstract class ChangementFenetre {

    // Méthode simple pour changer de page
    protected void changerFenetre(ActionEvent event, String viewName) {
        App.changerVue(viewName);
    }

    // Méthode spéciale pour la fin de partie (avec passage de données)
    protected void changerVueFinPartie(int aides, int aidesMax, String temps, String tempsMax, boolean succes) {
        FinPartieAventure controller = (FinPartieAventure) GestionnaireVues.getController("finPartieAventure");

        if (controller != null) {
            // On injecte les données
            controller.mettreDonnees(aides, aidesMax, temps, tempsMax, succes);

            // On change la vue via App (en gardant le zoom)
            App.changerVue("finPartieAventure");
        } else {
            System.err.println("Erreur : Le contrôleur finPartieAventure est null !");
        }
    }
}
