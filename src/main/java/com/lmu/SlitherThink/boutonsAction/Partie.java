package com.lmu.SlitherThink.boutonsAction;


import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class Partie extends ChangementFenetre{
    private int numPartie;

    @FXML
    private void menuPause(ActionEvent event) {
        changerFenetre(event, "pause");
    }

    @FXML
    private void aide(ActionEvent event) {
        System.out.println("Aide demandée pour la partie n°" + numPartie);}

    @FXML
    public void initialiserPartie(int numero) {
        this.numPartie = numero;
        System.out.println("Chargement du niveau n°" + numero);
        
        // Ici, insérez votre logique pour charger le niveau :
        // - Charger le bon fichier JSON/Texte du labyrinthe
        // - Placer le serpent au départ
        // - Réinitialiser le chrono
        chargerConfigurationNiveau(numero);
    }
    
    private void chargerConfigurationNiveau(int n) {
        // Logique de lecture de fichier ou de setup
    }

    public void relancerJeu() {
        System.out.println("Relance du jeu pour la partie n°" + numPartie);
        // Logique pour relancer le timer et reprendre le jeu
    }
}




