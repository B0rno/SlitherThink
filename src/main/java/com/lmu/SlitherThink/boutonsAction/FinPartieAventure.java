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

/**
 * Contrôleur de la vue de fin de partie pour le mode Aventure.
 * Cette classe affiche les statistiques du joueur (temps, aides), calcule le nombre 
 * d'étoiles obtenues et présente un classement des trois meilleurs scores pour le niveau concerné.
 * * @author Ilann
 */
public class FinPartieAventure extends ChangementFenetre {

    @FXML private Label txtTemps, txtTempsMax, txtAides, txtComplete;
    @FXML private ImageView etoile1, etoile2, etoile3;
    @FXML private Label highScore1, highScore2, highScore3;

    /** Image représentant une étoile pleine (critère rempli). */
    private final Image IMAGE_PLEINE = new Image(getClass().getResourceAsStream("/images/etoilePleine.png"));
    
    /** Image représentant une étoile vide (critère non rempli). */
    private final Image IMAGE_VIDE = new Image(getClass().getResourceAsStream("/images/etoileVide.png"));

    /**
     * Gère l'action pour passer au niveau suivant. 
     * Si le niveau actuel est le dernier (12), redirige vers la sélection des niveaux.
     * Sinon, initialise et charge le niveau n+1.
     * * @param event L'événement de clic.
     */
    @FXML
    private void partieSuivante(ActionEvent event) {
        if (PartieTimer.getNumPartie() == 12){
            changerFenetre(event, "choixPartieAventure");
        }
        else {
            PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
            controller.initialiserPartie(PartieTimer.getNumPartie() + 1, false);
            changerFenetre(event, "partieTimer");
        }
    }

    /**
     * Relance la partie actuelle en utilisant la logique de la classe Pause.
     * @param event L'événement de clic.
     */
    @FXML
    private void recommencer(ActionEvent event) {
        Pause p = new Pause();
        p.recommencer(event);
    }

    /**
     * Redirige l'utilisateur vers le menu de sélection des parties aventure.
     * @param event L'événement de clic.
     */
    @FXML
    private void choixPartieAventure(ActionEvent event) {
        changerFenetre(event, "choixPartieAventure");
    }

    /**
     * Redirige l'utilisateur vers le menu d'accueil.
     * @param event L'événement de clic.
     */
    @FXML
    private void menu(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }

    /**
     * Compare le temps réalisé au temps maximum autorisé.
     * @param temps Temps réalisé (format MM:SS).
     * @param tempsMax Temps limite (format MM:SS).
     * @return true si le temps est inférieur ou égal au maximum.
     */
    private boolean comparerTemps(String temps, String tempsMax) {
        String[] partsTemps = temps.split(":");
        String[] partsTempsMax = tempsMax.split(":");
        int totalSecondes = Integer.parseInt(partsTemps[0]) * 60 + Integer.parseInt(partsTemps[1]);
        int totalSecondesMax = Integer.parseInt(partsTempsMax[0]) * 60 + Integer.parseInt(partsTempsMax[1]);
        return totalSecondes <= totalSecondesMax;
    }

    /**
     * Met à jour les éléments graphiques avec les résultats de la partie venant d'être jouée.
     * Calcule et affiche les étoiles selon la complétion, le temps et les aides.
     * * @param aides Nombre d'aides utilisées.
     * @param aidesMax Seuil d'aides pour obtenir une étoile bonus.
     * @param temps Temps final réalisé.
     * @param tempsMax Temps limite pour obtenir une étoile bonus.
     * @param complete Indique si la grille a été résolue avec succès.
     */
    public void mettreDonnees(int aides, int aidesMax, String temps, String tempsMax, boolean complete) {
        txtTemps.setText(temps);
        txtAides.setText("Aides utilisées : " + aides + " / " + aidesMax);
        txtTempsMax.setText("Temps : " + temps + " / " + tempsMax);
        txtComplete.setText("Partie complétée : " + (complete ? "oui" : "non"));

        int chronoSecondes = parseTempsEnSecondes(temps);
        int nbEtoiles = complete ? calculerEtoiles(chronoSecondes, aides) : 0;

        appliquerEtoile(etoile1, nbEtoiles >= 1);
        appliquerEtoile(etoile2, nbEtoiles >= 2);
        appliquerEtoile(etoile3, nbEtoiles >= 3);

        chargerEtAfficherHighScores();
    }

    /**
     * Modifie l'image d'un ImageView pour afficher une étoile pleine ou vide.
     * @param iv Le conteneur d'image.
     * @param pleine {@code true} pour une étoile pleine, {@code false} pour une vide.
     */
    private void appliquerEtoile(ImageView iv, boolean pleine) {
        if (iv != null) {
            iv.setImage(pleine ? IMAGE_PLEINE : IMAGE_VIDE);
        }
    }

    /**
     * Convertit une chaîne de caractères au format "MM:SS" en nombre total de secondes.
     * @param temps La chaîne à parser.
     * @return Le nombre de secondes, ou 0 si le format est invalide.
     */
    private int parseTempsEnSecondes(String temps) {
        if (temps == null || !temps.contains(":")) return 0;
        String[] parts = temps.split(":");
        if (parts.length != 2) return 0;
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    /**
     * Charge les scores depuis le fichier CSV, filtre les données pour le niveau actuel,
     * trie les joueurs par performance (Etoiles > Temps) et affiche le Top 3.
     */
    private void chargerEtAfficherHighScores() {
        int numPartie = PartieTimer.getNumPartie();
        String nomPartie = numPartie > 0 ? "partie" + numPartie : "tutoriel";

        List<StructureCSV> tousLesScores = LoadCSV.lire(
            "/save/Score.csv",
            "./save/Score.csv",
            StructureCSV.class
        );

        List<StructureCSV> top3 = tousLesScores.stream()
            .filter(score -> score.getCheminGrille().contains(nomPartie))
            .sorted((s1, s2) -> {
                int etoiles1 = calculerEtoiles(s1.getChrono(), s1.getNbAide());
                int etoiles2 = calculerEtoiles(s2.getChrono(), s2.getNbAide());
                int compareEtoiles = Integer.compare(etoiles2, etoiles1);
                if (compareEtoiles != 0) return compareEtoiles;
                return Integer.compare(s1.getChrono(), s2.getChrono());
            })
            .limit(3)
            .collect(Collectors.toList());

        Label[] labels = {highScore1, highScore2, highScore3};
        for (int i = 0; i < labels.length; i++) {
            if (i < top3.size()) {
                StructureCSV score = top3.get(i);
                int nbEtoiles = calculerEtoiles(score.getChrono(), score.getNbAide());
                String etoiles = afficherEtoiles(nbEtoiles);
                String temps = formatTime(score.getChrono());
                labels[i].setText((i + 1) + ":  " + score.getPseudo() + "  " + etoiles + "  " + temps);
            } else {
                labels[i].setText((i + 1) + ":  ---  :  --:--");
            }
        }
    }

    /**
     * Formate un nombre de secondes en chaîne "M:SS".
     * @param secondes Temps en secondes.
     * @return Temps formaté.
     */
    private String formatTime(int secondes) {
        int minutes = secondes / 60;
        int sec = secondes % 60;
        return String.format("%d:%02d", minutes, sec);
    }

    /**
     * Calcule le nombre d'étoiles (1 à 3) selon les critères de performance.
     * Critères : 
     * - 1 étoile : Finir la partie.
     * - 2 étoiles : Finir sous 5 min OU utiliser <= 3 aides.
     * - 3 étoiles : Finir sous 5 min ET utiliser <= 3 aides.
     * * @param chrono Temps en secondes.
     * @param nbAides Nombre d'aides utilisées.
     * @return Nombre d'étoiles gagnées.
     */
    private int calculerEtoiles(int chrono, int nbAides) {
        final int DUREE_POUR_ETOILE = 300; 
        final int NB_AIDES_MAX = 3;

        if (chrono <= DUREE_POUR_ETOILE && nbAides <= NB_AIDES_MAX) return 3;
        else if (chrono <= DUREE_POUR_ETOILE) return 2;
        else if (nbAides <= NB_AIDES_MAX) return 2;
        else return 1;
    }

    /**
     * Génère une représentation textuelle des étoiles.
     * @param nbEtoiles Nombre d'étoiles obtenues.
     * @return Chaîne de caractères (ex: "★★☆").
     */
    private String afficherEtoiles(int nbEtoiles) {
        String etoilePleine = "★";
        String etoileVide = "☆";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(i < nbEtoiles ? etoilePleine : etoileVide);
        }
        return sb.toString();
    }
}