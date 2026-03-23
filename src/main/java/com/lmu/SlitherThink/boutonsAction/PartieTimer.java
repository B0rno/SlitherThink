package com.lmu.SlitherThink.boutonsAction;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PartieTimer extends Partie{
    private static int numPartie;

    @FXML
    private Label timerLabel;
    @FXML
    private VBox zoneJeu; // Zone de jeu pour afficher les éléments du niveau


    private int secondesEcoulees = 0;
    private Timeline chronometre;
    private StackPane[][] matriceCases;
    private int tailleMatrice;





    private void genererPlateau(int taille) {
        this.tailleMatrice = taille;
        this.matriceCases = new StackPane[taille][taille];
        
        // On vide la zone de jeu avant de reconstruire [cite: 8]
        zoneJeu.getChildren().clear();

        GridPane plateau = new GridPane();
        plateau.setAlignment(Pos.CENTER);
        
        // Calcul dynamique de la taille d'une case (600px / nombre de cases)
        double tailleCase = 540.0 / taille; // 540 pour laisser une petite marge de padding [cite: 8]

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int col = 0; col < taille; col++) {
                StackPane caseGrille = new StackPane();
                caseGrille.setPrefSize(tailleCase, tailleCase);
                
                // Style visuel des cases
                caseGrille.setStyle("-fx-border-color: #2f2f2f; -fx-background-color: #f4f4f4;");
                
                // On stocke la référence dans notre matrice Java
                matriceCases[ligne][col] = caseGrille;
                
                // On l'ajoute au GridPane (enfant, colonne, ligne)
                plateau.add(caseGrille, col, ligne);
            }
        }
        
        zoneJeu.getChildren().add(plateau);
    }


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
        genererPlateau(13);
        placerChiffre(0,0, 3);
        placerChiffre(2,3, 1);

        placerChiffre(5,7, 0);
        placerChiffre(7,5, 2);



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


    public void placerChiffre(int ligne, int col, int valeur) {
        if (ligne >= 0 && ligne < tailleMatrice && col >= 0 && col < tailleMatrice) {
            StackPane caseCible = matriceCases[ligne][col];
            
            Label labelChiffre = new Label(String.valueOf(valeur));
            
            // Optionnel : Style pour que ce soit joli
            labelChiffre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2f2f2f;");
            
            caseCible.getChildren().clear();
            
            caseCible.getChildren().add(labelChiffre);
        }
        else{
            System.out.println("Coordonnées hors limites : ligne " + ligne + ", col " + col);
        }
    }
}




