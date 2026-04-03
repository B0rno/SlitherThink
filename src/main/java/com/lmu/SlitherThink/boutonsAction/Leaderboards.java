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
 * Contrôleur pour la vue des Leaderboards.
 * Cette classe gère l'affichage des 10 meilleurs scores pour chaque niveau du jeu.
 * Elle permet de filtrer les résultats par partie via un menu déroulant et génère 
 * dynamiquement l'interface graphique pour lister les performances des joueurs.
 * * Les scores sont classés par nombre d'étoiles puis par temps.
 * * @author Ilann
 */
public class Leaderboards extends ChangementFenetre {

    @FXML private MenuButton menuLeaderboards;
    @FXML private ScrollPane scrollPaneScores;
    @FXML private VBox contentPane;

    /** Liste complète des scores chargés depuis le système de sauvegarde. */
    private List<StructureCSV> tousLesScores;
    
    /** Identifiant de la partie actuellement sélectionnée dans le classement. */
    private String partieSelectionnee = "partie1";

    /**
     * Initialise le contrôleur au chargement de la vue FXML.
     * Charge les données initiales et affiche par défaut le classement de la "Partie 1".
     */
    @FXML
    public void initialize() {
        chargerScores();
        menuLeaderboards.setText("Partie 1");
        afficherScores(partieSelectionnee);
    }

    /**
     * Rafraîchit les données du leaderboard depuis le fichier source.
     * Utile pour mettre à jour l'affichage sans recharger toute la vue si un nouveau score est enregistré.
     */
    public void rafraichirDonnees() {
        chargerScores();
        afficherScores(partieSelectionnee);
    }

    /**
     * Charge l'intégralité des scores présents dans le fichier Score.csv.
     */
    private void chargerScores() {
        tousLesScores = LoadCSV.lire(
            "/save/Score.csv",
            "./save/Score.csv",
            StructureCSV.class
        );
    }

    /**
     * Gère le changement de niveau via le MenuButton.
     * Met à jour le texte du menu et déclenche un nouvel affichage filtré.
     * * @param event L'événement déclenché par la sélection d'un MenuItem.
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
     * Filtre les scores, les trie et génère les composants JavaFX dans le panneau de contenu.
     * Affiche un message spécifique si aucun score n'est trouvé pour le niveau donné.
     * * @param nomPartie Le nom technique de la partie (ex: "partie1").
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
                int compareEtoiles = Integer.compare(etoiles2, etoiles1);
                if (compareEtoiles != 0) return compareEtoiles;
                return Integer.compare(s1.getChrono(), s2.getChrono());
            })
            .limit(10) // Top 10 uniquement
            .collect(Collectors.toList());

        if (scoresFiltres.isEmpty()) {
            Label noData = new Label("Aucun score pour\ncette partie");
            noData.setFont(Font.font(14));
            noData.setStyle("-fx-text-fill: gray;");
            noData.setAlignment(Pos.CENTER);
            contentPane.getChildren().add(noData);
            return;
        }

        int rang = 1;
        for (StructureCSV score : scoresFiltres) {
            HBox scoreBox = creerLigneScore(rang, score);
            contentPane.getChildren().add(scoreBox);
            rang++;
        }
    }

    /**
     * Construit programmatiquement une ligne (HBox) représentant un score dans le classement.
     * * @param rang Le rang du joueur (1, 2, 3...).
     * @param score L'objet {@link StructureCSV} contenant les données du score.
     * @return Un conteneur {@link HBox} stylisé prêt à être inséré dans la vue.
     */
    private HBox creerLigneScore(int rang, StructureCSV score) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(5, 10, 5, 10));
        // Alternance de couleur pour la lisibilité
        box.setStyle("-fx-background-color: " + (rang % 2 == 0 ? "#f0f0f0" : "#ffffff") +
                     "; -fx-background-radius: 5;");

        Label labelRang = new Label(rang + ".");
        labelRang.setMinWidth(25);
        labelRang.setFont(Font.font(14));
        labelRang.setStyle("-fx-font-weight: bold;");

        Label labelPseudo = new Label(score.getPseudo());
        labelPseudo.setMinWidth(80);
        labelPseudo.setFont(Font.font(14));

        int nbEtoiles = calculerEtoiles(score.getChrono(), score.getNbAide());
        Label labelEtoiles = new Label(afficherEtoiles(nbEtoiles));
        labelEtoiles.setMinWidth(60);
        labelEtoiles.setFont(Font.font(14));
        labelEtoiles.setStyle("-fx-text-fill: #FFD700;");

        int secondes = score.getChrono();
        int minutes = secondes / 60;
        int sec = secondes % 60;
        String temps = String.format("%d:%02d", minutes, sec);
        Label labelTemps = new Label(temps);
        labelTemps.setMinWidth(50);
        labelTemps.setFont(Font.font(13));
        labelTemps.setStyle("-fx-text-fill: #0066cc;");

        Label labelAides = new Label("(" + score.getNbAide() + " aide" + (score.getNbAide() > 1 ? "s" : "") + ")");
        labelAides.setFont(Font.font(11));
        labelAides.setStyle("-fx-text-fill: gray;");

        box.getChildren().addAll(labelRang, labelPseudo, labelEtoiles, labelTemps, labelAides);
        return box;
    }

    /**
     * Redirige l'utilisateur vers le menu d'accueil.
     * @param event L'événement de clic.
     */
    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "menuAccueil");
    }

    /**
     * Calcule le nombre d'étoiles gagnées selon des seuils de temps et d'aide.
     * Logique de score :
     * - 3 étoiles : <= 5 min ET <= 3 aides.
     * - 2 étoiles : <= 5 min OU <= 3 aides.
     * - 1 étoile : complété au delà de ces seuils.
     * * @param chrono Temps écoulé en secondes.
     * @param nbAides Nombre d'aides utilisées.
     * @return Le nombre d'étoiles (1 à 3).
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
     * Formate le nombre d'étoiles en une représentation textuelle stylisée.
     * * @param nbEtoiles Le nombre d'étoiles à afficher.
     * @return Une chaîne de caractères composée de symboles '★' et '☆'.
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