package com.lmu.SlitherThink.boutonsAction;


import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Partie.EtatPartie;
import com.lmu.SlitherThink.Partie.PartieObserver;
import com.lmu.SlitherThink.Partie.Profil;
import com.lmu.SlitherThink.Partie.Score;

import javafx.event.ActionEvent;

public class Partie extends ChangementFenetre implements PartieObserver{
    // On utilise le chemin complet pour ne pas confondre avec la classe parente
    protected com.lmu.SlitherThink.Partie.Partie moteurJeu;

    public static String dernierMode = "libre";

    @FXML
    private VBox zoneJeu; // Zone de jeu pour afficher les éléments du niveau

    protected StackPane[][] matriceCases;



   
    @FXML
    public void menuPause(ActionEvent event) {
        changerFenetre(event, "pause");
    }

    @FXML
    private void aide(ActionEvent event) {
        System.out.println("Aide demandée pour la partie libre");}

    public void relancerJeu(){
    }


    @Override
    public void onVictoire(Score score) {
        // Cette méthode sera appelée AUTOMATIQUEMENT par la classe Partie (métier)
        System.out.println("Gagné ! ");
        
        // Arrêter le chrono visuel
        
        // Afficher une alerte ou changer de fenêtre
        // ex: changerFenetre(null, "finPartieAventure");
    }

    @Override
    public void onEtatChange(EtatPartie etat) {
        // Réagir visuellement aux changements d'état
        
    }

    @Override
    public void onAideUtilisee(int restantes) {
        System.out.println("Aides restantes : " + restantes);
        // Mettre à jour un label d'aide dans ton FXML si tu en as un
    }




    public void initialiserPartie() {
        Partie.setDernierMode("libre"); 
        
        //TODO gestion de recherche de la partie selon la difficulté

        String grille = "partie";
        Matrice mat = Matrice.loadGrille(grille);

        if (mat == null) {
            System.err.println("Erreur : la matrice n'a pas pu être chargée pour la grille '" + grille + "'.");
            return; 
        }

        this.moteurJeu = new com.lmu.SlitherThink.Partie.Partie(new Profil("Joueur"), mat, 3, new Score());
        this.moteurJeu.ajouterObserver(this);
        this.moteurJeu.demarrer();
        chargerMatrice(mat);
    }


    public static void setDernierMode(String vue) {
        dernierMode = vue;
    }

    private void genererPlateau(int taille) {
        this.matriceCases = new StackPane[taille][taille];
        zoneJeu.getChildren().clear();

        GridPane plateau = new GridPane();
        plateau.setAlignment(Pos.CENTER);
        
        // Nombre total de composants (Points + Traits + Cases)
        int nbCellulesFX = taille * 2 + 1;
        double tailleAdaptee = taille * 1.5;
        
        // On définit la taille maximale que doit prendre la grille dans l'écran
        // Tu peux ajuster 600.0 selon la taille de ton carré noir
        double tailleGrilleSouhaitee = 600.0; 
        double tailleUnite = tailleGrilleSouhaitee / tailleAdaptee;

        // Échelles pour que les traits soient plus fins que les cases
        

        for (int l = 0; l < nbCellulesFX; l++) {
            for (int c = 0; c < nbCellulesFX; c++) {
                
                if (l % 2 != 0 && c % 2 != 0) {
                    //case
                    StackPane caseChiffre = new StackPane();
                    caseChiffre.setPrefSize(tailleUnite, tailleUnite); 
                    caseChiffre.setMinSize(tailleUnite, tailleUnite);
                    plateau.add(caseChiffre, c, l);
                    matriceCases[(l-1)/2][(c-1)/2] = caseChiffre;

                } else if ((l % 2 == 0 && c % 2 != 0) || (l % 2 != 0 && c % 2 == 0)) {
                    //trait
                    boolean estHorizontal = (l % 2 == 0);
                    Trait traitLogique = new Trait();
                    
                    Rectangle traitVisuel = creerTraitGraphique(estHorizontal, traitLogique, tailleUnite, l, c);
                    plateau.add(traitVisuel, c, l);

                } else {
                    // point d'intersection
                    Circle point = new Circle(tailleUnite * 0.15, Color.DARKGREY);
                    plateau.add(point, c, l);
                }
            }
        }
        zoneJeu.getChildren().add(plateau);
    }



    private Rectangle creerTraitGraphique(boolean horizontal, Trait logique, double tailleUnite, int l, int c) {
        double longTrait = tailleUnite; 
        double epaisTrait = tailleUnite * 0.2; 
    
        Rectangle rect = new Rectangle(
            horizontal ? longTrait : epaisTrait, 
            horizontal ? epaisTrait : longTrait
        );
    
        rect.setFill(Color.TRANSPARENT);
        rect.setCursor(javafx.scene.Cursor.HAND);
    
        Runnable rafraichirVisuel = () -> {
            switch (logique.getEtat()) {
                case PLEIN:
                    rect.setFill(Color.BLACK);
                    break;
                case CROIX:
                    try {
                        javafx.scene.image.Image imgCroix = new javafx.scene.image.Image(
                            getClass().getResourceAsStream("/images/croix.png")
                        );
                        double tX = epaisTrait * 5; 
                        double offX = (rect.getWidth() - tX) / 2.0;
                        double offY = (rect.getHeight() - tX) / 2.0;
                        rect.setFill(new javafx.scene.paint.ImagePattern(imgCroix, offX, offY, tX, tX, false));
                    } catch (Exception ex) { rect.setFill(Color.RED); }
                    break;
                case VIDE:
                    rect.setFill(Color.TRANSPARENT);
                    break;
            }
        };
    
        rect.setOnMousePressed(event -> {
            logique.etatSuivant();
            rafraichirVisuel.run();
            communiquerAuMoteur(l, c, horizontal);
        });
    
        // Mode "Glissement" : On ne force que le tracé de traits vides pour éviter les erreurs de glissement
        rect.setOnDragDetected(event -> rect.startFullDrag());
    
        rect.setOnMouseDragEntered(event -> {
            // CONDITION : On ne modifie le trait en glissant QUE s'il est VIDE
            // Cela évite de transformer tes traits existants en croix par erreur
            if (logique.getEtat() == com.lmu.SlitherThink.Grille.ValeurTrait.VIDE) {
                logique.etatSuivant(); // Passe de VIDE à PLEIN
                rafraichirVisuel.run();
                communiquerAuMoteur(l, c, horizontal);
            }
        });
    
        if (!horizontal) rect.setTranslateX(3); 
        return rect;
    }
    
    //évite de recalculer les coordonnées à chaque coup
    private void communiquerAuMoteur(int l, int c, boolean horizontal) {
        if (moteurJeu != null) {
            int ligneM, colM, dir;
            if (horizontal) {
                ligneM = l / 2;
                colM = (c - 1) / 2;
                dir = 0;
                if (ligneM >= moteurJeu.getMatrice().getHauteur()) { ligneM--; dir = 3; }
            } else {
                ligneM = (l - 1) / 2;
                colM = c / 2;
                dir = 1;
                if (colM >= moteurJeu.getMatrice().getLargeur()) { colM--; dir = 2; }
            }
            moteurJeu.jouerCoup(ligneM, colM, dir);
        }
    }



    


    public Matrice chargerMatrice(Matrice mat){
        int taille = mat.getHauteur();
        genererPlateau(taille);
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int col = 0; col < taille; col++) {
                int valeur = mat.getCase(ligne, col).getNumero();
                if (valeur != -1) {
                    // On utilise la méthode de placement vue au début
                    if (ligne >= 0 && ligne < taille && col >= 0 && col < taille) {
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
        }
        return mat;
    }
}




