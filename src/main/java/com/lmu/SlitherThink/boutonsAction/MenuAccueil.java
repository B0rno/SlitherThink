package com.lmu.SlitherThink.boutonsAction;

import com.lmu.SlitherThink.App;
import com.lmu.SlitherThink.GestionnaireVues;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Contrôleur du menu principal de l'application SlitherThink.
 * Cette classe gère les interactions de l'écran d'accueil, permettant de naviguer 
 * vers les modes de jeu, le tutoriel, les classements, ou de quitter l'application.
 * * @author Ilann
 */
public class MenuAccueil extends ChangementFenetre {

    /**
     * Redirige l'utilisateur vers l'écran de changement de compte (saisie du pseudo).
     * @param event L'événement de clic sur le bouton.
     */
    @FXML
    private void changerCompte(ActionEvent event) {
        changerFenetre(event, "pseudo");
    }

    /**
     * Redirige l'utilisateur vers l'écran de sélection du mode de jeu (Aventure ou Libre).
     * @param event L'événement de clic sur le bouton Jouer.
     */
    @FXML 
    private void jouer(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }

    /**
     * Lance le mode tutoriel.
     * Récupère le contrôleur de la partie avec minuteur, initialise le mode tutoriel 
     * et change la vue vers le plateau de jeu.
     * @param event L'événement de clic sur le bouton Tutoriel.
     */
    @FXML
    private void tutoriel(ActionEvent event) {
        PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
        if (controller != null) {
            controller.lancerTutoriel();
            App.changerVue("partieTimer");
        } else {
            System.err.println("Erreur : Le contrôleur de la partie est introuvable !");
        }
    }

    /**
     * Accède à l'écran des classements mondiaux ou locaux.
     * Rafraîchit les données avant d'afficher la vue pour garantir l'actualité des scores.
     * @param event L'événement de clic sur le bouton Leaderboards.
     */
    @FXML
    private void leaderboards(ActionEvent event) {
        Leaderboards controller = (Leaderboards) GestionnaireVues.getController("leaderboards");
        if (controller != null) {
            controller.rafraichirDonnees();
            App.changerVue("leaderboards");
        } else {
            System.err.println("Erreur : Le contrôleur leaderboards est introuvable !");
        }
    }

    /**
     * Redirige l'utilisateur vers l'écran d'apprentissage des techniques de jeu.
     * Définit le menu d'accueil comme vue précédente pour permettre un retour cohérent.
     * @param event L'événement de clic sur le bouton Techniques.
     */
    @FXML
    private void techniques(ActionEvent event) {
        Techniques.setVuePrecedente("menuAccueil");
        changerFenetre(event, "techniques");
    }

    /**
     * Ferme proprement l'application JavaFX et arrête le processus système.
     * @param event L'événement de clic sur le bouton Quitter.
     */
    @FXML
    private void quitter(ActionEvent event) {
        Platform.exit(); 
        System.exit(0); 
    }

    /**
     * Redirige l'utilisateur vers l'écran de pause (utilisé principalement pour les tests 
     * ou une navigation directe).
     * @param event L'événement de clic sur le bouton Pause.
     */
    @FXML
    private void pause(ActionEvent event) {
        changerFenetre(event, "pause");
    }
}