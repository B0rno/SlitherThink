package com.lmu.SlitherThink.boutonsAction;

import com.lmu.SlitherThink.App;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Partie.EtatPartie;
import com.lmu.SlitherThink.Partie.PartieObserver;
import com.lmu.SlitherThink.Partie.Profil;
import com.lmu.SlitherThink.Partie.Score;
import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.gestionDonnee.rechercheSave;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Partie extends ChangementFenetre implements PartieObserver {
    protected com.lmu.SlitherThink.Partie.PartieHelper moteurJeu;

    public static String dernierMode = "libre";
    private static String grilleEnCours = null;
    public static String nomGrille = "tutoriel";

    @FXML
    private VBox zoneJeu; 

    protected StackPane[][] matriceCases;

    // --- MÉTHODES STATIQUES (Rétablies pour corriger les erreurs de compilation) ---
    
    public static String getGrilleEnCours() {
        return grilleEnCours;
    }

    public static void setGrilleEnCours(String grille) {
        grilleEnCours = grille;
    }

    public static void setDernierMode(String mode) {
        dernierMode = mode;
    }

    public static String getDernierMode() {
        return dernierMode;
    }

    // --- LOGIQUE DE JEU ---

    @Override
    public void onVictoire(Score score) {
        App.changerVue("finPartieLibre");
    }

    @Override
    public void onEtatChange(EtatPartie etat) {}


    @FXML
    protected Label labelAide;  
    @Override
    public void onAideUtilisee() {
        if (moteurJeu != null) {
            Aide aide = moteurJeu.getAideEnCours();
            if (aide != null) {
                // Afficher nom + description
                String texte = aide.getNom() + "\n\n" + aide.getTechniqueLiee();
                labelAide.setText(texte);
            } else {
                labelAide.setText("Aucune aide disponible");
            }
        }
    }

    @FXML
    public void menuPause(ActionEvent event) {
        changerFenetre(event, "pause");
    }

    @FXML
    private void aide(ActionEvent event) {
        if(moteurJeu != null) {
            moteurJeu.utiliserAide();
        }
    }

    public void initialiserPartie(String grille) {
        initialiserPartie(grille, false);
    }

    public void initialiserPartie(String grille, boolean recommencer) {
        setDernierMode("libre");
        setGrilleEnCours(grille);

        Matrice mat = Matrice.loadGrille(grille);
        if (mat == null) {
            System.err.println("Erreur : la matrice n'a pas pu être chargée pour la grille " + grille);
            return;
        }

        // Lecture de la sauvegarde ici, si elle n'est pas lu, créer la sauvegarde
        Partie.nomGrille = grille;
        if (!recommencer) {
            boolean loaded = mat.loadSave(Pseudo.nomJoueur, "./save/saveGrille/" + grille + ".json", false);
            if(!loaded){
                // Creer la référence de sauvegarde ici
                SaveHelper saveHelper = SaveHelper.getInstance();
                saveHelper.ajouterPartieLibre(LoadSave.getInstance(""), Pseudo.nomJoueur, grille);
                SaveManager saveManager = new SaveManager(LoadSave.getInstance(""));
                saveManager.actualiserSaveGlobal();
            }
        } else {
            // Si on recommence, réinitialiser la sauvegarde existante
            LoadSave save = LoadSave.getInstance("");
            save.rechargerSaveGlobal();

            var sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(
                Pseudo.nomJoueur,
                "./save/saveGrille/" + grille + ".json"
            );

            if (sauvegarde != null && sauvegarde.getId() != null) {
                // Réinitialiser le détail de la sauvegarde
                DetailleSavePartie nouveauDetail = DetailleSavePartie.create(new ArrayList<>(), 0, 0);
                nouveauDetail.setNameClass(sauvegarde.getId().toString());
                sauvegarde.setDetailleSave(nouveauDetail);

                SaveManager saveManager = new SaveManager(save);
                saveManager.updateSaveFichierId(sauvegarde.getId());
                saveManager.actualiserSaveGlobal();

                System.out.println("[DEBUG] Sauvegarde réinitialisée pour recommencer (mode libre)");
            }
        }

        this.moteurJeu = new com.lmu.SlitherThink.Partie.PartieHelper(new Profil(Pseudo.nomJoueur), mat, 3, new Score());
        this.moteurJeu.ajouterObserver(this);
        chargerMatrice(mat);
        this.moteurJeu.demarrer();
    }

    public void sauvegarderProgressionCourante() {
        if (moteurJeu == null || moteurJeu.getScore() == null) {
            return;
        }

        if ("tutoriel".equalsIgnoreCase(getDernierMode())) {
            return;
        }

        String grille = getGrilleEnCours();
        if (grille == null || grille.isBlank()) {
            grille = nomGrille;
        }
        if (grille == null || grille.isBlank()) {
            return;
        }

        String pseudo = moteurJeu.getProfil() != null ? moteurJeu.getProfil().getPseudo() : Pseudo.nomJoueur;
        if (pseudo == null || pseudo.isBlank()) {
            return;
        }

        int chrono = (int) moteurJeu.getScore().getDureeEnSecondes();
        int nbAides = moteurJeu.getScore().getNbAidesUtilisees();
        String pathGrille = "./save/saveGrille/" + grille + ".json";

        LoadSave save = LoadSave.getInstance("");
        SaveManager saveManager = new SaveManager(save);

        var sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(pseudo, pathGrille);

        if (sauvegarde == null) {
            if ("aventure".equalsIgnoreCase(getDernierMode())) {
                SaveHelper.ajouterPartieAventure(save, pseudo, grille);
            } else {
                SaveHelper.ajouterPartieLibre(save, pseudo, grille);
            }
            sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(pseudo, pathGrille);
        }

        if (sauvegarde == null || sauvegarde.getDetailleSave() == null) {
            return;
        }

        DetailleSavePartie ancienDetail = sauvegarde.getDetailleSave();

        // Mise à jour uniquement du chrono et des aides
        // L'état de la grille est déjà sauvegardé par Matrice.saveGrille() à chaque clic
        ancienDetail.setChronometre(chrono);
        ancienDetail.setNbAides(nbAides);

        Integer id = sauvegarde.getId();
        if (id != null) {
            saveManager.updateSaveFichierId(id);
            saveManager.actualiserSaveGlobal();
        }
    }

    private void genererPlateau(Matrice mat, int taille) {
        this.matriceCases = new StackPane[taille][taille];
        zoneJeu.getChildren().clear();

        GridPane plateau = new GridPane();
        plateau.setAlignment(Pos.CENTER);
        
        int nbCellulesFX = taille * 2 + 1;
        double tailleGrilleSouhaitee = 600.0; 
        double tailleUnite = tailleGrilleSouhaitee / (taille * 1.5);

        int currentHorizontalDir = 0; // 0 = NORD
        int currentVerticalDir = 1;   // 1 = OUEST

        for (int l = 0; l < nbCellulesFX; l++) {
            // Changement horizontal UNIQUEMENT quand l change et est pair
            if (l % 2 == 0 && l != 0) {
                currentHorizontalDir = (currentHorizontalDir == 0) ? 3 : 0;
            }

            // Reset vertical à chaque nouvelle ligne
            currentVerticalDir = 1;
            for (int c = 0; c < nbCellulesFX; c++) {
                if (l % 2 != 0 && c % 2 != 0) {
                    // Case à chiffre
                    StackPane caseChiffre = new StackPane();
                    caseChiffre.setPrefSize(tailleUnite, tailleUnite); 
                    caseChiffre.setMinSize(tailleUnite, tailleUnite);
                    plateau.add(caseChiffre, c, l);
                    matriceCases[(l-1)/2][(c-1)/2] = caseChiffre;

                } else if ((l % 2 == 0 && c % 2 != 0) || (l % 2 != 0 && c % 2 == 0)) {
                    // Trait (Horizontal ou Vertical)
                    boolean estHorizontal = (l % 2 == 0);

                    int ligneCase;
                    int colonneCase;
                    int direction;

                    if (estHorizontal) {
                        direction = currentHorizontalDir;

                        colonneCase = (c - 1) / 2;

                        if (direction == 0) {
                            // NORD → case en dessous
                            ligneCase = l / 2;
                        } else {
                            // SUD → case au dessus
                            ligneCase = (l / 2) - 1;
                        }

                        if (ligneCase < 0) ligneCase = 0;
                        if (ligneCase >= taille) ligneCase = taille - 1;

                    } else {
                        direction = currentVerticalDir;

                        // alternance verticale à CHAQUE trait
                        currentVerticalDir = (currentVerticalDir == 1) ? 2 : 1;

                        ligneCase = (l - 1) / 2;

                        if (direction == 1) {
                            // OUEST → case à droite
                            colonneCase = c / 2;
                        } else {
                            // EST → case à gauche
                            colonneCase = (c / 2) - 1;
                        }

                        if (colonneCase < 0) colonneCase = 0;
                        if (colonneCase >= taille) colonneCase = taille - 1;
                    }
                    Trait traitLogique = new Trait();
                    traitLogique.setTrait(mat.getCase(ligneCase, colonneCase).getTrait(direction).getEtat());
                    
                    // On récupère le StackPane contenant le rectangle + la croix
                    Node traitGraphique = creerTraitGraphique(estHorizontal, traitLogique, tailleUnite, l, c);
                    plateau.add(traitGraphique, c, l);

                } else {
                    // Point d'intersection
                    Circle point = new Circle(tailleUnite * 0.15, Color.DARKGREY);
                    plateau.add(point, c, l);
                }
            }
        }
        zoneJeu.getChildren().add(plateau);
    }

    private Node creerTraitGraphique(boolean horizontal, Trait logique, double tailleUnite, int l, int c) {
        double longTrait = tailleUnite; 
        double epaisTrait = tailleUnite * 0.2; 
    
        // 1. Le Rectangle (base cliquable)
        Rectangle rect = new Rectangle(
            horizontal ? longTrait : epaisTrait, 
            horizontal ? epaisTrait : longTrait
        );
        rect.setFill(Color.TRANSPARENT);

        // 2. La Croix dessinée (2 lignes rouges)
        // On définit la taille de la croix pour qu'elle soit bien visible
        double tailleC = epaisTrait * 0.4; // Ajuste ici pour agrandir/réduire la croix
        
        Line l1 = new Line(-tailleC, -tailleC, tailleC, tailleC);
        Line l2 = new Line(tailleC, -tailleC, -tailleC, tailleC);
        
        l1.setStroke(Color.RED);
        l1.setStrokeWidth(2.5); // Épaisseur des traits de la croix
        l2.setStroke(Color.RED);
        l2.setStrokeWidth(2.5);

        // On groupe les lignes. Le StackPane les centrera automatiquement.
        Group dessinCroix = new Group(l1, l2);
        dessinCroix.setVisible(false);
        dessinCroix.setMouseTransparent(true); // Pour que le clic passe au rectangle

        // 3. Le conteneur StackPane pour empiler rect + croix
        StackPane conteneur = new StackPane();
        conteneur.getChildren().addAll(rect, dessinCroix);
        conteneur.setCursor(javafx.scene.Cursor.HAND);
    
        // Logique de rafraîchissement
        Runnable rafraichirVisuel = () -> {
            switch (logique.getEtat()) {
                case PLEIN:
                    rect.setFill(Color.BLACK);
                    dessinCroix.setVisible(false);
                    break;
                case CROIX:
                    rect.setFill(Color.TRANSPARENT);
                    dessinCroix.setVisible(true);
                    break;
                case VIDE:
                    rect.setFill(Color.TRANSPARENT);
                    dessinCroix.setVisible(false);
                    break;
            }
        };
    
        // Événements
        conteneur.setOnMousePressed(event -> {
            logique.etatSuivant();
            rafraichirVisuel.run();
            communiquerAuMoteur(l, c, horizontal);
        });

        conteneur.setOnDragDetected(event -> conteneur.startFullDrag());

        conteneur.setOnMouseDragEntered(event -> {
            if (logique.getEtat() == com.lmu.SlitherThink.Grille.ValeurTrait.VIDE) {
                logique.etatSuivant();
                rafraichirVisuel.run();
                communiquerAuMoteur(l, c, horizontal);
            }
        });
        rafraichirVisuel.run();
        return conteneur;
    }
    
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
        genererPlateau(mat, taille);
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int col = 0; col < taille; col++) {
                int valeur = mat.getCase(ligne, col).getNumero();
                if (valeur != -1) {
                    if (ligne >= 0 && ligne < taille && col >= 0 && col < taille) {
                        StackPane caseCible = matriceCases[ligne][col];
                        Label labelChiffre = new Label(String.valueOf(valeur));
                        labelChiffre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2f2f2f;");
                        caseCible.getChildren().setAll(labelChiffre);
                    }   
                }
            }
        }
        return mat;
    }
}