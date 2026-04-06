package com.lmu.SlitherThink.boutonsAction;

import com.lmu.SlitherThink.App;
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

/**
 * Gère les parties avec un chronomètre (Mode Aventure et Tutoriel).
 * S'occupe de l'enregistrement des scores en CSV et de la gestion du temps visuel.
 * @author Ilann
 */
public class PartieTimer extends Partie {
    private static int numPartie;

    @FXML
    private Label timerLabel;

    private Timeline chronometre;

    /**
     * Gère la fin de partie, arrête les chronos et affiche l'écran de résultats.
     * @param score L'objet score contenant les statistiques finales.
     */
    @Override
    public void onVictoire(Score score) {
        saveScore(score);
        if (chronometre != null) chronometre.stop();
        
        score.arreterChrono();
        score.calculerEtoiles();

        int aidesUtilisees = score.getNbAidesUtilisees();
        int aidesMax = score.getNbAidexMax();
        
        String tempsFinal = formatTime((int) score.getDureeEnSecondes());
        String tempsMaxEtoile = formatTime((int) score.getDureePourEtoile());

        changerVueFinPartie(aidesUtilisees, aidesMax, tempsFinal, tempsMaxEtoile, true);
    }

    /**
     * Sauvegarde le score final dans le fichier CSV et supprime la sauvegarde temporaire.
     * @param score Le score à enregistrer.
     */
    private void saveScore(Score score) {
        String grille = Partie.nomGrille;
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

    /**
     * Réagit aux changements d'état du moteur (pause/reprise) pour synchroniser le chrono.
     * @param etat Le nouvel état de la partie.
     */
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

    /** Initialise la Timeline du chronomètre visuel. */
    @FXML 
    public void initialize(){
        chronometre = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            actualiseChrono();
        }));
        chronometre.setCycleCount(Timeline.INDEFINITE);
    }

    /** Met à jour le label du timer avec le temps actuel du moteur de jeu. */
    private void actualiseChrono() {
        if (timerLabel != null && moteurJeu != null) {
            int secondes = (int) moteurJeu.getScore().getDureeEnSecondes();
            timerLabel.setText(formatTime(secondes));
        }
    }

    /**
     * Formate les secondes en chaîne MM:SS.
     * @param totalSeconds Le nombre de secondes.
     * @return Le temps formaté.
     */
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Met la partie en pause et change de fenêtre.
     * @param event L'événement d'action.
     */
    @FXML
    @Override
    public void menuPause(ActionEvent event) {
        if (moteurJeu != null) moteurJeu.mettreEnPause(); 
        super.menuPause(event);
    }

    /** Utilise une aide via le moteur. */
    @FXML
    private void aide(ActionEvent event) {
        if (moteurJeu != null) {
            moteurJeu.utiliserAide(); 
        }
    }

    /**
     * Initialise une partie aventure avec gestion des sauvegardes existantes.
     * @param numero Le numéro du niveau.
     * @param recommencer Si vrai, réinitialise la progression du niveau.
     */
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

        Partie.nomGrille = "partie" + numero;
        Score score = new Score();
        if(!recommencer){
            boolean loaded = mat.loadSave(Pseudo.nomJoueur, "./save/saveGrille/partie" + numero + ".json", true);
            if(!loaded){
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
            LoadSave save = LoadSave.getInstance("");
            save.rechargerSaveGlobal();

            var sauvegarde = rechercheSave.trouverSauvegardeParPseudoEtPath(
                Pseudo.nomJoueur,
                "./save/saveGrille/partie" + numero + ".json"
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

        this.moteurJeu = new com.lmu.SlitherThink.Partie.PartieGestion(new Profil(Pseudo.nomJoueur), mat, 3, score);
        this.moteurJeu.ajouterObserver(this);
        
        chargerMatrice(mat);
        actualiseChrono();
        
        this.moteurJeu.demarrer(); 
        chronometre.play();
    }

    /** Lance le mode tutoriel avec des aides illimitées. */
    @FXML 
    public void lancerTutoriel() {
        numPartie = 0; 
        Partie.setDernierMode("tutoriel");
        reinitialiserAffichageAide();
        
        Matrice mat = Matrice.loadGrille("tutoriel");
        if (mat == null) return;

        this.moteurJeu = new com.lmu.SlitherThink.Partie.PartieGestion(new Profil("Apprenti"), mat, 99, new Score());
        this.moteurJeu.ajouterObserver(this);

        chargerMatrice(mat);
        actualiseChrono();
        this.moteurJeu.demarrer();
        chronometre.play();
    }

    /** Reprend l'exécution du moteur de jeu. */
    public void reprendrePartie() {
        if (moteurJeu != null) {
            moteurJeu.demarrer(); 
        }
    }

    /** @return Le numéro de la partie aventure en cours. */
    public static int getNumPartie() {
        return numPartie;
    }
}