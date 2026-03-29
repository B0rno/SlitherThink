package com.lmu.SlitherThink.boutonsAction;

import java.util.List;
import java.util.stream.Collectors;

import com.lmu.SlitherThink.save.csvScore.LoadCSV;
import com.lmu.SlitherThink.save.csvScore.structure.StructureCSV;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Controller pour la page des leaderboards.
 * Affiche les meilleurs scores pour chaque partie.
 */
public class Leaderboards extends ChangementFenetre {

    @FXML
    private MenuButton menuLeaderboards;

    @FXML
    private ScrollPane scrollPaneScores;

    @FXML
    private VBox contentPane;

    private List<StructureCSV> tousLesScores;
    private String partieSelectionnee = "partie1";

    /**
     * Initialise le leaderboard au chargement de la page.
     * Charge tous les scores depuis Score.csv et affiche ceux de la partie 1 par défaut.
     */
    @FXML
    public void initialize() {
        chargerScores();
        menuLeaderboards.setText("Partie 1");
        afficherScores(partieSelectionnee);
    }

    /**
     * Charge tous les scores depuis le fichier CSV.
     */
    private void chargerScores() {
        tousLesScores = LoadCSV.lire(
            "/save/Score.csv",
            "./save/Score.csv",
            StructureCSV.class
        );
    }

    /**
     * Gère le changement de partie sélectionnée dans le menu.
     * Filtre et affiche les scores correspondants.
     */
    @FXML
    private void modifChamp(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        menuLeaderboards.setText(item.getText());

        // Extraire le numéro de la partie (ex: "Partie 1" -> "partie1")
        String texte = item.getText();
        int numero = Integer.parseInt(texte.replaceAll("[^0-9]", ""));
        partieSelectionnee = "partie" + numero;

        afficherScores(partieSelectionnee);
    }

    /**
     * Affiche les scores filtrés par partie dans le ScrollPane.
     * Les scores sont triés par temps croissant (meilleurs en premier).
     *
     * @param nomPartie le nom de la partie à filtrer (ex: "partie1")
     */
    private void afficherScores(String nomPartie) {
        contentPane.getChildren().clear();

        if (tousLesScores == null || tousLesScores.isEmpty()) {
            Label noData = new Label("Aucun score disponible");
            noData.setFont(Font.font(14));
            noData.setStyle("-fx-text-fill: gray;");
            contentPane.getChildren().add(noData);
            return;
        }

        // Filtrer par partie et trier par étoiles (décroissant) puis chrono (croissant)
        List<StructureCSV> scoresFiltres = tousLesScores.stream()
            .filter(score -> score.getCheminGrille().contains(nomPartie))
            .sorted((s1, s2) -> {
                int etoiles1 = calculerEtoiles(s1.getChrono(), s1.getNbAide());
                int etoiles2 = calculerEtoiles(s2.getChrono(), s2.getNbAide());
                // Trier par étoiles décroissant (3 avant 2 avant 1)
                int compareEtoiles = Integer.compare(etoiles2, etoiles1);
                if (compareEtoiles != 0) return compareEtoiles;
                // Si même nombre d'étoiles, trier par chrono croissant
                return Integer.compare(s1.getChrono(), s2.getChrono());
            })
            .limit(10) // Top 10
            .collect(Collectors.toList());

        if (scoresFiltres.isEmpty()) {
            Label noData = new Label("Aucun score pour\ncette partie");
            noData.setFont(Font.font(14));
            noData.setStyle("-fx-text-fill: gray;");
            noData.setAlignment(Pos.CENTER);
            contentPane.getChildren().add(noData);
            return;
        }

        // Afficher chaque score
        int rang = 1;
        for (StructureCSV score : scoresFiltres) {
            HBox scoreBox = creerLigneScore(rang, score);
            contentPane.getChildren().add(scoreBox);
            rang++;
        }
    }

    /**
     * Crée une ligne d'affichage pour un score.
     *
     * @param rang le rang du joueur (1 = premier)
     * @param score le score à afficher
     * @return un HBox contenant le rang, pseudo, étoiles, temps et aides
     */
    private HBox creerLigneScore(int rang, StructureCSV score) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(5, 10, 5, 10));
        box.setStyle("-fx-background-color: " + (rang % 2 == 0 ? "#f0f0f0" : "#ffffff") +
                     "; -fx-background-radius: 5;");

        // Rang
        Label labelRang = new Label(rang + ".");
        labelRang.setMinWidth(25);
        labelRang.setFont(Font.font(14));
        labelRang.setStyle("-fx-font-weight: bold;");

        // Pseudo
        Label labelPseudo = new Label(score.getPseudo());
        labelPseudo.setMinWidth(80);
        labelPseudo.setFont(Font.font(14));

        // Étoiles
        int nbEtoiles = calculerEtoiles(score.getChrono(), score.getNbAide());
        Label labelEtoiles = new Label(afficherEtoiles(nbEtoiles));
        labelEtoiles.setMinWidth(60);
        labelEtoiles.setFont(Font.font(14));
        labelEtoiles.setStyle("-fx-text-fill: #FFD700;");

        // Temps (en format MM:SS)
        int secondes = score.getChrono();
        int minutes = secondes / 60;
        int sec = secondes % 60;
        String temps = String.format("%d:%02d", minutes, sec);
        Label labelTemps = new Label(temps);
        labelTemps.setMinWidth(50);
        labelTemps.setFont(Font.font(13));
        labelTemps.setStyle("-fx-text-fill: #0066cc;");

        // Nombre d'aides
        Label labelAides = new Label("(" + score.getNbAide() + " aide" + (score.getNbAide() > 1 ? "s" : "") + ")");
        labelAides.setFont(Font.font(11));
        labelAides.setStyle("-fx-text-fill: gray;");

        box.getChildren().addAll(labelRang, labelPseudo, labelEtoiles, labelTemps, labelAides);
        return box;
    }

    /**
     * Retourne au menu d'accueil.
     */
    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }

    /**
     * Calcule le nombre d'étoiles obtenues selon le temps et les aides utilisées.
     * Logique identique à Score.calculerEtoiles().
     *
     * @param chrono le temps en secondes
     * @param nbAides le nombre d'aides utilisées
     * @return le nombre d'étoiles (1 à 3)
     */
    private int calculerEtoiles(int chrono, int nbAides) {
        final int DUREE_POUR_ETOILE = 300; // 5 minutes
        final int NB_AIDES_MAX = 3;

        if (chrono <= DUREE_POUR_ETOILE && nbAides == 0) return 3;
        else if (chrono <= DUREE_POUR_ETOILE && nbAides <= NB_AIDES_MAX) return 2;
        else return 1;
    }

    /**
     * Formate l'affichage des étoiles.
     *
     * @param nbEtoiles le nombre d'étoiles (1 à 3)
     * @return une chaîne avec des étoiles pleines et vides (ex: "★★☆")
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
