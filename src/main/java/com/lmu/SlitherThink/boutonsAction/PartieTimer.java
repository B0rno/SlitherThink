package com.lmu.SlitherThink.boutonsAction;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Partie.EtatPartie;
import com.lmu.SlitherThink.Partie.Profil;
import com.lmu.SlitherThink.Partie.Score;
import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.csvScore.structure.StructureCSV;
import com.lmu.SlitherThink.save.gestionDonnee.rechercheSave;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;

import java.util.ArrayList;

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


    //chrono visuel
    private Timeline chronometre;

    @Override
    public void onVictoire(Score score) {

        saveScore(score);
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
        System.out.println("fin de la partie");

        changerVueFinPartie(aidesUtilisees, aidesMax, tempsFinal, tempsMaxEtoile, true);
    }

    private void saveScore(Score score) {
        String grille = Partie.getGrilleEnCours();
        if (grille == null || grille.isBlank()) {
            grille = numPartie > 0 ? "partie" + numPartie : "tutoriel";
        }

        if ("tutoriel".equalsIgnoreCase(grille) || "tutoriel".equalsIgnoreCase(Partie.getDernierMode())) {
            return;
        }

        String pseudo = this.moteurJeu.getProfil().getPseudo();
        int nbAide = score.getNbAidesUtilisees();
        int chrono = (int) score.getDureeEnSecondes();

        LoadSave save = LoadSave.getInstance("");
        SaveManager saveManager = new SaveManager(save);

        saveManager.ajouterScoreEtSauvegarderCsv(
            new StructureCSV(pseudo, grille, nbAide, chrono),
            ""
        );

        String pathGrille = "./save/saveGrille/" + grille + ".json";
        Integer idFichier = saveManager.trouverIdSauvegardeParPseudoEtPath(pseudo, pathGrille);
        if (idFichier != null) {
            saveManager.delFichierId(idFichier);
        }

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
    public void initialiserPartie(int numero, boolean recommencer) {
        numPartie = numero;
        Partie.setDernierMode("aventure"); 
        reinitialiserAffichageAide();

        Matrice mat = Matrice.loadGrille("partie" + numero);
        if (mat == null) {
            System.err.println("Erreur de chargement de la grille");
            return;
        }

        // Lecture de la sauvegarde ici, si elle n'est pas lu, créer la sauvegarde
        Partie.nomGrille = "partie" + numero;
        Score score = new Score();
        if(!recommencer){
            boolean loaded = mat.loadSave(Pseudo.nomJoueur, "./save/saveGrille/partie" + numero + ".json", true);
            if(!loaded){
                // Creer la référence de sauvegarde ici
                SaveHelper saveHelper = SaveHelper.getInstance();
                saveHelper.ajouterPartieAventure(LoadSave.getInstance(""), Pseudo.nomJoueur, "partie" + numero);
                SaveManager saveManager = new SaveManager(LoadSave.getInstance(""));
                saveManager.actualiserSaveGlobal();
            } else {
                var sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(
                    Pseudo.nomJoueur,
                    "./save/saveGrille/partie" + numero + ".json"
                );

                if (sauvegarde != null && sauvegarde.getDetailleSave() != null) {
                    Integer chronoInt = sauvegarde.getDetailleSave().getChronometre();
                    if (chronoInt != null) {
                        score.setDureeAccumulee(java.time.Duration.ofSeconds(chronoInt.longValue()));
                    }

                    Integer nbAides = sauvegarde.getDetailleSave().getNbAides();
                    if (nbAides != null) {
                        score.setNbAidesUtilisees(nbAides);
                    }
                }
            }
        } else {
            // Si on recommence, réinitialiser la sauvegarde existante
            LoadSave save = LoadSave.getInstance("");
            save.rechargerSaveGlobal();

            var sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(
                Pseudo.nomJoueur,
                "./save/saveGrille/partie" + numero + ".json"
            );

            if (sauvegarde != null && sauvegarde.getId() != null) {
                // Réinitialiser le détail de la sauvegarde (grille vide, chrono à 0, 0 aides)
                DetailleSavePartie nouveauDetail = DetailleSavePartie.create(new ArrayList<>(), 0, 0);
                nouveauDetail.setNameClass(sauvegarde.getId().toString());
                sauvegarde.setDetailleSave(nouveauDetail);

                SaveManager saveManager = new SaveManager(save);
                saveManager.updateSaveFichierId(sauvegarde.getId());
                saveManager.actualiserSaveGlobal();
            }
        }


        this.moteurJeu = new com.lmu.SlitherThink.Partie.PartieHelper(new Profil(Pseudo.nomJoueur), mat, 3, score);
        this.moteurJeu.ajouterObserver(this);
        
        chargerMatrice(mat);
        actualiseChrono();

        System.out.println(mat);
        
        this.moteurJeu.demarrer(); 
        chronometre.play();
    }

    @FXML 
    public void lancerTutoriel() {
        numPartie = 0; 
        Partie.setDernierMode("tutoriel");
        reinitialiserAffichageAide();
        
        Matrice mat = Matrice.loadGrille("tutoriel");
        if (mat == null) return;

        this.moteurJeu = new com.lmu.SlitherThink.Partie.PartieHelper(new Profil("Apprenti"), mat, 99, new Score());
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