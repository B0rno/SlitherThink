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

/**
 * Contrôleur principal gérant le plateau de jeu, l'affichage de la grille 
 * et la communication avec le moteur logique.
 * @author Ilann
 */
public class Partie extends ChangementFenetre implements PartieObserver {
    protected com.lmu.SlitherThink.Partie.PartieGestion moteurJeu;

    public static String dernierMode = "libre";
    private static String grilleEnCours = null;
    public static String nomGrille = "tutoriel";

    @FXML
    private VBox zoneJeu; 

    protected StackPane[][] matriceCases;

    /** @return Le nom du fichier de la grille actuelle. */
    public static String getGrilleEnCours() {
        return grilleEnCours;
    }

    /** @param grille Définit la grille à charger. */
    public static void setGrilleEnCours(String grille) {
        grilleEnCours = grille;
    }

    /** @param mode Définit si le mode est libre ou aventure. */
    public static void setDernierMode(String mode) {
        dernierMode = mode;
    }

    /** @return Le dernier mode de jeu utilisé. */
    public static String getDernierMode() {
        return dernierMode;
    }

    /** Déclenche l'écran de victoire. 
     * @param score Le score final de la partie, utilisé pour afficher les résultats.
    */
    @Override
    public void onVictoire(Score score) {
        App.changerVue("finPartieLibre");
    }

    @Override
    public void onEtatChange(EtatPartie etat) {}

    @FXML
    protected Label labelAide;  

    /** Efface le texte du label d'aide. */
    protected void reinitialiserAffichageAide() {
        if (labelAide != null) {
            labelAide.setText("");
        }
    }

    /** Affiche les détails de l'aide fournie par le moteur. */
    @Override
    public void onAideUtilisee() {
        if (moteurJeu != null) {
            Aide aide = moteurJeu.getAideEnCours();
            if (aide != null) {
                String texte = aide.getNom() + "\n\n" + aide.getTechniqueLiee();
                labelAide.setText(texte);
            } else {
                labelAide.setText("Aucune aide disponible");
            }
        }
    }

    /** Ouvre la vue de pause. 
     * @param event L'événement de clic sur le bouton de pause.
    */
    @FXML
    public void menuPause(ActionEvent event) {
        changerFenetre(event, "pause");
    }

    /** Demande une aide au moteur de jeu. 
     * @param event L'événement de clic sur le bouton d'aide.
    */
    @FXML
    private void aide(ActionEvent event) {
        if(moteurJeu != null) {
            moteurJeu.utiliserAide();
        }
    }

    /** Initialise une partie sans l'option recommencer. 
     * @param grille Le nom du fichier de la grille à charger.
    */
    public void initialiserPartie(String grille) {
        initialiserPartie(grille, false);
    }

    /** * Initialise la structure de la partie, charge la matrice et gère la sauvegarde.
     * @param grille Le nom du fichier de la grille à charger.
     * @param recommencer Booléen qui indique si la partie doit être réinitialisée
     */
    public void initialiserPartie(String grille, boolean recommencer) {
        setDernierMode("libre");
        setGrilleEnCours(grille);
        reinitialiserAffichageAide();

        Matrice mat = Matrice.loadGrille(grille);
        if (mat == null) {
            System.err.println("Erreur : la matrice n'a pas pu être chargée pour la grille " + grille);
            return;
        }

        Partie.nomGrille = grille;
        if (!recommencer) {
            boolean loaded = mat.loadSave(Pseudo.nomJoueur, "./save/saveGrille/" + grille + ".json", false);
            if(!loaded){
                SaveHelper saveHelper = SaveHelper.getInstance();
                saveHelper.ajouterPartieLibre(LoadSave.getInstance(""), Pseudo.nomJoueur, grille);
                SaveManager saveManager = new SaveManager(LoadSave.getInstance(""));
                saveManager.actualiserSaveGlobal();
            }
        } else {
            LoadSave save = LoadSave.getInstance("");
            save.rechargerSaveGlobal();

            var sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(
                Pseudo.nomJoueur,
                "./save/saveGrille/" + grille + ".json"
            );

            if (sauvegarde != null && sauvegarde.getId() != null) {
                DetailleSavePartie nouveauDetail = DetailleSavePartie.create(new ArrayList<>(), 0, 0);
                nouveauDetail.setNameClass(sauvegarde.getId().toString());
                sauvegarde.setDetailleSave(nouveauDetail);

                SaveManager saveManager = new SaveManager(save);
                saveManager.updateSaveFichierId(sauvegarde.getId());
                saveManager.actualiserSaveGlobal();
            }
        }

        this.moteurJeu = new com.lmu.SlitherThink.Partie.PartieGestion(new Profil(Pseudo.nomJoueur), mat, 3, new Score());
        this.moteurJeu.ajouterObserver(this);
        chargerMatrice(mat);
        this.moteurJeu.demarrer();
    }

    /** Enregistre le chrono et le nombre d'aides dans le fichier de sauvegarde global. */
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
        ancienDetail.setChronometre(chrono);
        ancienDetail.setNbAides(nbAides);

        Integer id = sauvegarde.getId();
        if (id != null) {
            saveManager.updateSaveFichierId(id);
            saveManager.actualiserSaveGlobal();
        }
    }

    /** Génère les composants graphiques du plateau (points, traits et cellules). 
     * @param mat La matrice logique du moteur de jeu à représenter graphiquement.
     * @param taille La taille de la matrice (nombre de cases par côté).
    */
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
            if (l % 2 == 0 && l != 0) {
                currentHorizontalDir = (currentHorizontalDir == 0) ? 3 : 0;
            }

            currentVerticalDir = 1;
            for (int c = 0; c < nbCellulesFX; c++) {
                if (l % 2 != 0 && c % 2 != 0) {
                    StackPane caseChiffre = new StackPane();
                    caseChiffre.setPrefSize(tailleUnite, tailleUnite); 
                    caseChiffre.setMinSize(tailleUnite, tailleUnite);
                    plateau.add(caseChiffre, c, l);
                    matriceCases[(l-1)/2][(c-1)/2] = caseChiffre;

                } else if ((l % 2 == 0 && c % 2 != 0) || (l % 2 != 0 && c % 2 == 0)) {
                    boolean estHorizontal = (l % 2 == 0);

                    int ligneCase;
                    int colonneCase;
                    int direction;

                    if (estHorizontal) {
                        direction = currentHorizontalDir;
                        colonneCase = (c - 1) / 2;

                        if (direction == 0) {
                            ligneCase = l / 2;
                        } else {
                            ligneCase = (l / 2) - 1;
                        }

                        if (ligneCase < 0) ligneCase = 0;
                        if (ligneCase >= taille) ligneCase = taille - 1;

                    } else {
                        direction = currentVerticalDir;
                        currentVerticalDir = (currentVerticalDir == 1) ? 2 : 1;
                        ligneCase = (l - 1) / 2;

                        if (direction == 1) {
                            colonneCase = c / 2;
                        } else {
                            colonneCase = (c / 2) - 1;
                        }

                        if (colonneCase < 0) colonneCase = 0;
                        if (colonneCase >= taille) colonneCase = taille - 1;
                    }
                    Trait traitLogique = new Trait();
                    traitLogique.setTrait(mat.getCase(ligneCase, colonneCase).getTrait(direction).getEtat());
                    
                    Node traitGraphique = creerTraitGraphique(estHorizontal, traitLogique, tailleUnite, l, c);
                    plateau.add(traitGraphique, c, l);

                } else {
                    Circle point = new Circle(tailleUnite * 0.15, Color.DARKGREY);
                    plateau.add(point, c, l);
                }
            }
        }
        zoneJeu.getChildren().add(plateau);
    }

    /** Crée un trait interactif (rectangle invisible) avec visuel pour les croix. 
     * @param horizontal Indique si le trait est horizontal ou vertical.
     * @param logique L'objet Trait du moteur de jeu associé à ce trait graphique.
     * @param tailleUnite La taille d'une unité de la grille pour dimensionner le trait.
     * @param l La ligne dans la grille FX où le trait est placé.
     * @param c La colonne dans la grille FX où le trait est placé.
     * @return Un Node contenant le trait interactif et son visuel de croix.
    */
    private Node creerTraitGraphique(boolean horizontal, Trait logique, double tailleUnite, int l, int c) {
        double longTrait = tailleUnite; 
        double epaisTrait = tailleUnite * 0.2; 
    
        Rectangle rect = new Rectangle(
            horizontal ? longTrait : epaisTrait, 
            horizontal ? epaisTrait : longTrait
        );
        rect.setFill(Color.TRANSPARENT);

        double tailleC = epaisTrait * 0.4; 
        
        Line l1 = new Line(-tailleC, -tailleC, tailleC, tailleC);
        Line l2 = new Line(tailleC, -tailleC, -tailleC, tailleC);
        
        l1.setStroke(Color.RED);
        l1.setStrokeWidth(2.5); 
        l2.setStroke(Color.RED);
        l2.setStrokeWidth(2.5);

        Group dessinCroix = new Group(l1, l2);
        dessinCroix.setVisible(false);
        dessinCroix.setMouseTransparent(true); 

        StackPane conteneur = new StackPane();
        conteneur.getChildren().addAll(rect, dessinCroix);
        conteneur.setCursor(javafx.scene.Cursor.HAND);
    
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
    
    /** Traduit les coordonnées FX en coordonnées de matrice pour le moteur. 
     * @param l La ligne dans la grille FX.
     * @param c La colonne dans la grille FX.
     * @param horizontal Indique si le trait est horizontal ou vertical.
    */
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

    /** Charge la matrice et place les indices numériques sur le plateau. 
     * @param mat La matrice à charger et afficher. 
     * @return La matrice chargée avec les cases mises à jour.
    */
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