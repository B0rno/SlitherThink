package com.lmu.SlitherThink;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.scene.transform.Scale;

public class App extends Application {

    // Conteneur GLOBAL qui va garder le zoom en permanence
    public static StackPane conteneurVues = new StackPane();

    @Override
    public void start(Stage stage) throws Exception {

        // Chargement des vues
        GestionnaireVues.loadView("pseudo", "/fxml/pseudo.fxml");
        GestionnaireVues.loadView("menuAccueil", "/fxml/menuAccueil.fxml");
        GestionnaireVues.loadView("choixMode", "/fxml/choixMode.fxml");
        GestionnaireVues.loadView("choixDifficulte", "/fxml/choixDifficulte.fxml");
        GestionnaireVues.loadView("choixPartieAventure", "/fxml/choixPartieAventure.fxml");
        GestionnaireVues.loadView("finPartieAventure", "/fxml/finPartieAventure.fxml");
        GestionnaireVues.loadView("leaderboards", "/fxml/leaderboards.fxml");
        GestionnaireVues.loadView("pause", "/fxml/pause.fxml");

        changerVue("pseudo");

        // Création du fond noir de l'écran
        Pane root = new Pane();
        root.getChildren().add(conteneurVues); // Le conteneur global est posé sur le fond noir

        Scene scene = new Scene(root);
        stage.setTitle("SlitherThink");
        stage.setScene(scene);

        Scale scale = new Scale();
        scale.setPivotX(0);
        scale.setPivotY(0);
        conteneurVues.getTransforms().add(scale);

        // Mise à jour du zoom quand la fenêtre change
        javafx.beans.value.ChangeListener<Number> listener = (obs, oldVal, newVal) -> {
            double scaleFactor = Math.min(scene.getWidth() / 1280.0, scene.getHeight() / 720.0);

            scale.setX(scaleFactor);
            scale.setY(scaleFactor);

            // Centre le conteneur global au milieu de l'écran
            conteneurVues.setLayoutX((scene.getWidth() - (1280 * scaleFactor)) / 2);
            conteneurVues.setLayoutY((scene.getHeight() - (720 * scaleFactor)) / 2);
        };

        scene.widthProperty().addListener(listener);
        scene.heightProperty().addListener(listener);

        // Passage en Plein Écran
        stage.setFullScreenExitHint("");
        javafx.application.Platform.runLater(() -> {
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        });
        stage.show();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }

    // Méthode pour changer de page
    public static void changerVue(String nomVue) {
        Region nouvelleVue = (Region) GestionnaireVues.getView(nomVue);
        conteneurVues.getChildren().setAll(nouvelleVue);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
