package com.lmu.SlitherThink.boutonsAction;


import com.lmu.SlitherThink.GestionnaireVues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.util.ArrayList;
import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.gestionDonnee.rechercheSave;

public class Pause extends ChangementFenetre{

    @FXML
    public void recommencer(ActionEvent event) {
        //ne pas sauvegarder
        if("libre".equals(Partie.dernierMode)){
            Partie controller = (Partie) GestionnaireVues.getController("partie");
            if (controller != null) {
                controller.initialiserPartie(Partie.getGrilleEnCours(), true); // true = recommencer
            }
            changerFenetre(event, "partie");
        }
        else{
            PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
            if (controller != null) {
                if ("aventure".equals(Partie.dernierMode)){
                    
                    controller.initialiserPartie(PartieTimer.getNumPartie(), true);
                }

                //tutoriel
                else{
                    controller.lancerTutoriel();
                }
                changerFenetre(event, "partieTimer");
            }

            else{
                System.out.println("Erreur : Impossible de récupérer le controller de PartieTimer");    
            }
        }
    }

    @FXML 
    private void abandonner(ActionEvent event) {
        LoadSave save = LoadSave.getInstance("");
        save.rechargerSaveGlobal();

        var sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(
            Pseudo.nomJoueur,
            "./save/saveGrille/" + Partie.nomGrille + ".json"
        );

        if (sauvegarde != null && sauvegarde.getId() != null) {
            // Réinitialiser le détail de la sauvegarde
            DetailleSavePartie nouveauDetail = DetailleSavePartie.create(new ArrayList<>(), 0, 0);
            nouveauDetail.setNameClass(sauvegarde.getId().toString());
            sauvegarde.setDetailleSave(nouveauDetail);

            SaveManager saveManager = new SaveManager(save);
            saveManager.updateSaveFichierId(sauvegarde.getId());
            saveManager.actualiserSaveGlobal();

            System.out.println("[DEBUG] Sauvegarde réinitialisée pour abandonner");
        }
        changerFenetre(event, "menuAccueil");
    }


    @FXML
    private void techniques(ActionEvent event) {
        Techniques.setVuePrecedente("pause");
        changerFenetre(event, "techniques");
    }

    @FXML
    private void menuPrincipal(ActionEvent event) {
        if ("libre".equals(Partie.dernierMode)) {
            Partie controller = (Partie) GestionnaireVues.getController("partie");
            if (controller != null) {
                controller.sauvegarderProgressionCourante();
            }
        } else {
            PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
            if (controller != null) {
                controller.sauvegarderProgressionCourante();
            }
        }

        changerFenetre(event, "menuAccueil");
    }

    @FXML
    private void reprendre(ActionEvent event) {
        
        if ("libre".equals(Partie.dernierMode)){
            changerFenetre(event, "partie");
        }
        else{
            PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
            if (controller != null) {
                controller.reprendrePartie(); 
            }
            changerFenetre(event, "partieTimer");
        }    
       
    }
    
}