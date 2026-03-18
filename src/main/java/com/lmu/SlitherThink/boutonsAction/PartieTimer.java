package com.lmu.SlitherThink.boutonsAction;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Partie.EtatPartie;
import com.lmu.SlitherThink.Partie.Score;
import com.lmu.SlitherThink.Partie.Profil;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PartieTimer extends Partie {
    private static int numPartie;

    @FXML
    private Label timerLabel;

    private int secondesEcoulees = 0;

    //chrono visuel
    private Timeline chronometre;

    @Override
    public void onVictoire(Score score) {
        //TODO sauvegarder le score


        // Arrêter le chrono visuel 
        if (chronometre != null) chronometre.stop();
        
        // Arrêter le chrono réel et calculer les étoiles
        score.arreterChrono();
        score.calculerEtoiles();

        // Récupérer les données réelles du Score pour l'écran de fin
        int aidesUtilisees = score.getNbAidesUtilisees();
        int aidesMax = score.getNbAidexMax();
        
        String tempsFinal = formatTime((int) score.getDureeEnSecondes());
        String tempsMaxEtoile = formatTime((int) score.getDureePourEtoile());

        changerVueFinPartie(aidesUtilisees, aidesMax, tempsFinal, tempsMaxEtoile, true);
    }

    @Override
    public void onEtatChange(EtatPartie etat) {
        if (etat == EtatPartie.PAUSE) {
            if (chronometre != null) chronometre.pause();
            if (moteurJeu != null) moteurJeu.getScore().pauseChrono();
        } else if (etat == EtatPartie.EN_COURS) {
            if (chronometre != null) chronometre.play();
            if (moteurJeu != null) moteurJeu.getScore().demarrerChrono();
        }
    }

    @FXML 
    public void initialize(){
        chronometre = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            actualiseChrono();
        }));
        chronometre.setCycleCount(Timeline.INDEFINITE);
    }

    private void actualiseChrono() {
        if (timerLabel != null && moteurJeu != null) {
            int secondes = (int) moteurJeu.getScore().getDureeEnSecondes();
            timerLabel.setText(formatTime(secondes));
        }
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @FXML
    @Override
    public void menuPause(ActionEvent event) {
        if (moteurJeu != null) moteurJeu.mettreEnPause(); 
        super.menuPause(event);
    }

    @FXML
    private void aide(ActionEvent event) {
        if (moteurJeu != null) {
            moteurJeu.utiliserAide(); 
        }
    }

    @FXML
    public void initialiserPartie(int numero) {
        numPartie = numero;
        secondesEcoulees = 0;
        Partie.setDernierMode("aventure"); 

        Matrice mat = Matrice.loadGrille("partie" + numero);
        if (mat == null) {
            System.err.println("Erreur de chargement de la grille");
            return;
        }

        this.moteurJeu = new com.lmu.SlitherThink.Partie.Partie(new Profil("Joueur"), mat, 3, new Score());
        this.moteurJeu.ajouterObserver(this);
        
        chargerMatrice(mat);
        actualiseChrono();
        
        this.moteurJeu.demarrer(); 
        chronometre.play();
    }

    @FXML 
    public void lancerTutoriel() {
        numPartie = -1; 
        secondesEcoulees = 0;
        Partie.setDernierMode("tutoriel");
        
        Matrice mat = Matrice.loadGrille("tutoriel");
        if (mat == null) return;

        this.moteurJeu = new com.lmu.SlitherThink.Partie.Partie(new Profil("Apprenti"), mat, 99, new Score());
        this.moteurJeu.ajouterObserver(this);

        chargerMatrice(mat);
        actualiseChrono();
        this.moteurJeu.demarrer();
        chronometre.play();
    }

    
    public void reprendrePartie() {
        if (moteurJeu != null) {
            moteurJeu.demarrer(); 
        }
    }

    public static int getNumPartie() {
        return numPartie;
    }
}