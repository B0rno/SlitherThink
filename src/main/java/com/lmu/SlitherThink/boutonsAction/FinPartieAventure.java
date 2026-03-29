package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;

import com.lmu.SlitherThink.GestionnaireVues;
import com.lmu.SlitherThink.save.csvScore.LoadCSV;
import com.lmu.SlitherThink.save.csvScore.structure.StructureCSV;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.stream.Collectors;

public class FinPartieAventure extends ChangementFenetre {

    @FXML private Label txtTemps;
    @FXML private Label txtTempsMax;
    @FXML private Label txtAides;
    @FXML private Label txtComplete;

    @FXML private ImageView etoile1;
    @FXML private ImageView etoile2;
    @FXML private ImageView etoile3;

    @FXML private Label highScore1;
    @FXML private Label highScore2;
    @FXML private Label highScore3;

    // Chemins vers les images (à adapter selon ton projet)
    private final Image IMAGE_PLEINE = new Image(getClass().getResourceAsStream("/images/etoilePleine.png"));
    private final Image IMAGE_VIDE = new Image(getClass().getResourceAsStream("/images/etoileVide.png"));

    @FXML
    private void partieSuivante(ActionEvent event) {
        if (PartieTimer.getNumPartie() == 12){
            changerFenetre(event, "choixPartieAventure");
        }
        else{
            PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
            controller.initialiserPartie(PartieTimer.getNumPartie() + 1, false);
            changerFenetre(event, "partieTimer");
        }
    }

    @FXML
    private void recommencer(ActionEvent event) {
        Pause p = new Pause();
        p.recommencer(event);
    }

    @FXML
    private void choixPartieAventure(ActionEvent event) {
        changerFenetre(event, "choixPartieAventure");
    }

    @FXML
    private void menu(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }


    private boolean comparerTemps(String temps, String tempsMax) {
        String [] partsTemps = temps.split(":");
        String [] partsTempsMax = tempsMax.split(":");
        int totalSecondes = Integer.parseInt(partsTemps[0]) * 90 + Integer.parseInt(partsTemps[1]);
        int totalSecondesMax = Integer.parseInt(partsTempsMax[0]) * 60 + Integer.parseInt(partsTempsMax[1]);
        
        return totalSecondes <= totalSecondesMax;
    }

    /**
     * Met à jour l'affichage et calcule les étoiles selon les critères :
     * Etoile 1 : Partie complétée.
     * Etoile 2 : Temps respecté ou Aides respectées.
     * Etoile 3 : Tout est valide
     */
    public void mettreDonnees(int aides, int aidesMax, String temps, String tempsMax, boolean complete) {
        // Mise à jour des textes
        txtTemps.setText(temps);
        txtAides.setText("Aides utilisées : " + aides + " / " + aidesMax);
        txtTempsMax.setText("Temps : " + temps + " / " + tempsMax);
        txtComplete.setText("Partie complétée : " + (complete ? "oui" : "non"));

        // Calcul des conditions
        boolean aidesRespectees = (aides <= aidesMax);
        boolean tempsRespecte = comparerTemps(temps, tempsMax);

        boolean e1 = complete;
        boolean e2 = complete && (aidesRespectees || tempsRespecte);
        boolean e3 = complete && aidesRespectees && tempsRespecte;


        appliquerEtoile(etoile1, e1);
        appliquerEtoile(etoile2, e2);
        appliquerEtoile(etoile3, e3);

        // Charger et afficher les high scores
        chargerEtAfficherHighScores();
    }

    private void appliquerEtoile(ImageView iv, boolean pleine) {
        if (iv != null) {
            iv.setImage(pleine ? IMAGE_PLEINE : IMAGE_VIDE);
        }
    }

    /**
     * Charge les meilleurs scores depuis Score.csv et affiche le top 3.
     */
    private void chargerEtAfficherHighScores() {
        // Déterminer quelle partie afficher (via PartieTimer.getNumPartie())
        int numPartie = PartieTimer.getNumPartie();
        String nomPartie = numPartie > 0 ? "partie" + numPartie : "tutoriel";

        // Charger tous les scores depuis Score.csv
        List<StructureCSV> tousLesScores = LoadCSV.lire(
            "/save/Score.csv",
            "./save/Score.csv",
            StructureCSV.class
        );

        // Filtrer par partie et trier par chrono (meilleurs temps en premier)
        List<StructureCSV> top3 = tousLesScores.stream()
            .filter(score -> score.getCheminGrille().contains(nomPartie))
            .sorted((s1, s2) -> Integer.compare(s1.getChrono(), s2.getChrono()))
            .limit(3)
            .collect(Collectors.toList());

        // Afficher les 3 premiers (ou moins si pas assez de scores)
        Label[] labels = {highScore1, highScore2, highScore3};
        for (int i = 0; i < labels.length; i++) {
            if (i < top3.size()) {
                StructureCSV score = top3.get(i);
                String temps = formatTime(score.getChrono());
                labels[i].setText((i + 1) + ":  " + score.getPseudo() + "  :  " + temps);
            } else {
                labels[i].setText((i + 1) + ":  ---  :  --:--");
            }
        }
    }

    /**
     * Formate un temps en secondes en format MM:SS.
     *
     * @param secondes le temps en secondes
     * @return le temps formaté (ex: "3:26")
     */
    private String formatTime(int secondes) {
        int minutes = secondes / 60;
        int sec = secondes % 60;
        return String.format("%d:%02d", minutes, sec);
    }
}