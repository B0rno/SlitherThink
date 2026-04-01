package com.lmu.SlitherThink.boutonsAction;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Techniques extends ChangementFenetre {

    private static String vuePrecedente = "menuAccueil";

    @FXML
    private Accordion accordeonTechniques;

    public static void setVuePrecedente(String vue) {
        vuePrecedente = vue;
    }

    @FXML
    public void initialize() {
        chargerTechniques();
    }

    private void chargerTechniques() {
        try {
            // Charger le JSON
            InputStream is = getClass().getResourceAsStream("/technique.json");
            if (is == null) {
                System.err.println("Erreur : technique.json introuvable");
                return;
            }

            Gson gson = new Gson();
            JsonObject root = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), JsonObject.class);
            JsonArray stockage = root.getAsJsonArray("stockage_langague");

            if (stockage == null || stockage.size() == 0) {
                return;
            }

            JsonObject francais = stockage.get(0).getAsJsonObject();
            JsonArray contenu = francais.getAsJsonArray("contenu");

            // Map pour organiser par niveau (ordre: débutant, basique, confirmé)
            Map<String, VBox> niveaux = new LinkedHashMap<>();
            niveaux.put("debutant", new VBox(15));
            niveaux.put("basique", new VBox(15));
            niveaux.put("confirme", new VBox(15));

            // Charger les techniques par niveau
            for (JsonElement elem : contenu) {
                JsonObject niveau = elem.getAsJsonObject();
                String nv = niveau.get("nv").getAsString();
                JsonArray techniques = niveau.getAsJsonArray("techniqueParsNv");

                VBox container = niveaux.get(nv);
                if (container == null) continue;

                for (JsonElement techElem : techniques) {
                    JsonObject tech = techElem.getAsJsonObject();
                    String nom = tech.get("name").getAsString();
                    String description = tech.get("description").getAsString();

                    // Créer un bloc pour chaque technique
                    VBox techniqueBox = new VBox(5);
                    techniqueBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");

                    Label nomLabel = new Label(nom);
                    nomLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

                    Label descLabel = new Label(description);
                    descLabel.setWrapText(true);
                    descLabel.setFont(Font.font("System", 14));

                    techniqueBox.getChildren().addAll(nomLabel, descLabel);
                    container.getChildren().add(techniqueBox);
                }
            }

            // Créer les TitledPane pour l'Accordion
            TitledPane debutantPane = new TitledPane("Niveau Débutant", niveaux.get("debutant"));
            TitledPane basiquePane = new TitledPane("Niveau Basique", niveaux.get("basique"));
            TitledPane confirmePane = new TitledPane("Niveau Confirmé", niveaux.get("confirme"));

            // Ajouter du padding aux conteneurs
            niveaux.get("debutant").setPadding(new Insets(10));
            niveaux.get("basique").setPadding(new Insets(10));
            niveaux.get("confirme").setPadding(new Insets(10));

            // Ajouter à l'Accordion
            accordeonTechniques.getPanes().addAll(debutantPane, basiquePane, confirmePane);

            // Ouvrir le premier par défaut
            accordeonTechniques.setExpandedPane(debutantPane);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des techniques : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, vuePrecedente);
    }
}