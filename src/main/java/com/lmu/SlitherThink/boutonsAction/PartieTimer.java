package com.lmu.SlitherThink.boutonsAction;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Partie.EtatPartie;
import com.lmu.SlitherThink.Partie.Score;
import com.lmu.SlitherThink.Partie.Profil; // Import ajouté

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class PartieTimer extends Partie {
    private static int numPartie;

    @FXML
    private Label timerLabel;

    private int secondesEcoulees = 0;
    private Timeline chronometre;

    @Override
    public void onVictoire(Score score) {
        if (chronometre != null) chronometre.stop();
        String tempsFormate = formatTime(secondesEcoulees);
        String test = "05:00";
        changerVueFinPartie(0, 5, tempsFormate, test, true);
    }

    @Override
    public void onEtatChange(EtatPartie etat) {
        if (etat == EtatPartie.PAUSE) {
            if (chronometre != null) chronometre.pause();
        } else if (etat == EtatPartie.EN_COURS) {
            if (chronometre != null) chronometre.play();
        }
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void actualiseChrono() {
        String tempsFormate = formatTime(secondesEcoulees);
        if (timerLabel != null) timerLabel.setText(tempsFormate);
    }

    public static int getNumPartie() {
        return numPartie;
    }

    @FXML 
    public void initialize(){
        chronometre = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondesEcoulees++;
            actualiseChrono();
        }));
        chronometre.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    @Override
    public void menuPause(ActionEvent event) {
        if (chronometre != null) chronometre.pause(); 
        // On prévient aussi le moteur logique que la partie est en pause
        if (moteurJeu != null) moteurJeu.mettreEnPause();
        super.menuPause(event);
    }

    @FXML
    private void aide(ActionEvent event) {
        System.out.println("Aide demandée pour la partie n°" + numPartie);
        if (moteurJeu != null) moteurJeu.utiliserAide();
    }

    @FXML
    public void initialiserPartie(int numero) {
        this.numPartie = numero;
        secondesEcoulees = 0;
        Partie.setDernierMode("aventure"); 
        System.out.println("Chargement du niveau n°" + numero);

        String grille = "partie" + numero;
        Matrice mat = Matrice.loadGrille(grille);

        if (mat == null) {
            System.err.println("Erreur : la matrice n'a pas pu être chargée.");
            return; 
        }

        // --- INITIALISATION DU MOTEUR LOGIQUE ---
        // On crée l'objet métier avec un profil, la matrice chargée et un nouveau score
        this.moteurJeu = new com.lmu.SlitherThink.Partie.Partie(new Profil("Joueur"), mat, 3, new Score());
        this.moteurJeu.ajouterObserver(this); // Pour recevoir onVictoire
        this.moteurJeu.demarrer();            // Pour que jouerCoup() soit actif
        // ----------------------------------------

        chargerMatrice(mat);
        actualiseChrono();
        chronometre.play();
    }

    @FXML 
    public void lancerTutoriel() {
        this.numPartie = -1; 
        this.secondesEcoulees = 0;
        Partie.setDernierMode("tutoriel");
        System.out.println("Chargement du tutoriel");
        
        Matrice mat = Matrice.loadGrille("tutoriel");
        if (mat == null) {
            System.err.println("Erreur : la matrice n'a pas pu être chargée.");
            return; 
        }

        // --- INITIALISATION DU MOTEUR LOGIQUE POUR TUTO ---
        this.moteurJeu = new com.lmu.SlitherThink.Partie.Partie(new Profil("Apprenti"), mat, 99, new Score());
        this.moteurJeu.ajouterObserver(this);
        this.moteurJeu.demarrer();
        // --------------------------------------------------

        chargerMatrice(mat);
        actualiseChrono();
        chronometre.play();
    }

    @Override
    public void relancerJeu() {
        System.out.println("Reprise du jeu pour la partie n°" + numPartie);
        // Reprendre la partie existante
        if (moteurJeu != null) {
            moteurJeu.demarrer(); // Reprise depuis PAUSE -> EN_COURS
        }
        if (chronometre != null) {
            chronometre.play(); // Relancer le timer
        }
    }
}