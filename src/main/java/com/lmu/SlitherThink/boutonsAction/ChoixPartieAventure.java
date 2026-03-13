package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class ChoixPartieAventure extends ChangementFenetre {
    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }

    //load partie en fonction du bouton cliqué (load la vue "partie" avec le numero de la partie cliquée)
    @FXML
    public void partie(ActionEvent event) {
        Button b = (Button) event.getSource();
        String niveau = b.getId().replace("btn", "");   
        choixPartie(event, niveau);
        //méthode dans changementfenetre.java
    }
}
