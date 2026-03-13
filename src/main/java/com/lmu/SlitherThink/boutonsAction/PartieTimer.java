package com.lmu.SlitherThink.boutonsAction;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PartieTimer extends Partie{
    private static int numPartie;

    @FXML
    private Label timerLabel;
    private int secondesEcoulees = 0;
    private Timeline chronometre;



    private void majLabel() {
        int minutes = secondesEcoulees / 60;
        int secondes = secondesEcoulees % 60;
        
        // Formate le texte en "00:00"
        String tempsFormate = String.format("%02d:%02d", minutes, secondes);
        timerLabel.setText(tempsFormate);
    }

    public static int getNumPartie() {
        return numPartie;
    }


  


    @FXML 
    public void initialize(){
        chronometre = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondesEcoulees++;
            majLabel();
        }));
        
        chronometre.setCycleCount(Timeline.INDEFINITE); // Le timer tourne sans fin
    }


    @FXML
    @Override
    public void menuPause(ActionEvent event) {
        if (chronometre != null) chronometre.pause(); 
        super.menuPause(event);
    }
    

    @FXML
    private void aide(ActionEvent event) {
        System.out.println("Aide demandée pour la partie n°" + numPartie);}

    @FXML
    public void initialiserPartie(int numero) {
        this.numPartie = numero;
        Partie.setDernierMode("aventure"); 
        System.out.println("Chargement du niveau n°" + numero);
        
        // - Charger le bon fichier JSON
        secondesEcoulees = 0;
        majLabel();
        chargerConfigurationNiveau(numero);
        chronometre.play();

    }
    
    private void chargerConfigurationNiveau(int n) {
        // Logique de lecture de fichier ou de setup
    }

    @Override
    public void relancerJeu() {
        System.out.println("Relance du jeu pour la partie n°" + numPartie);
        majLabel();
        chronometre.play();

        // Logique pour reprendre le jeu
    }
}




