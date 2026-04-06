package com.lmu.SlitherThink.boutonsAction;


import com.lmu.SlitherThink.GestionnaireVues;
import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.gestionDonnee.rechercheSave;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Contrôleur gérant l'écran de pause du jeu.
 * Permet de reprendre, recommencer, abandonner ou consulter les techniques.
 * @author Ilann
 */
public class Pause extends ChangementFenetre{

    /**
     * Réinitialise la partie actuelle selon le mode de jeu (Libre, Aventure ou Tutoriel).
     * @param event L'événement déclenché par le bouton recommencer.
     */
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
            }
        }
    }

    /**
     * Réinitialise les données de sauvegarde de la grille actuelle et retourne à l'accueil.
     * @param event L'événement déclenché par le bouton abandonner.
     */
    @FXML 
    private void abandonner(ActionEvent event) {
        LoadSave save = LoadSave.getInstance("");
        save.rechargerSaveGlobal();

        var sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(
            Pseudo.nomJoueur,
            "./save/saveGrille/" + Partie.nomGrille + ".json"
        );

        if (sauvegarde != null && sauvegarde.getId() != null) {

            SaveManager saveManager = new SaveManager(save);
            saveManager.delFichierId(sauvegarde.getId());
            saveManager.actualiserSaveGlobal();

        }
        save.rechargerSaveGlobal(); // Recharger pour s'assurer que les changements sont pris en compte
        
        changerFenetre(event, "menuAccueil");
    }


    /**
     * Redirige vers l'écran des techniques en mémorisant la vue actuelle.
     * @param event L'événement déclenché par le bouton techniques.
     */
    @FXML
    private void techniques(ActionEvent event) {
        Techniques.setVuePrecedente("pause");
        changerFenetre(event, "techniques");
    }

    /**
     * Sauvegarde la progression actuelle avant de retourner au menu principal.
     * @param event L'événement déclenché par le bouton menu principal.
     */
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

    /**
     * Reprend la partie en cours selon le mode de jeu actif.
     * @param event L'événement déclenché par le bouton reprendre.
     */
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