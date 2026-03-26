package com.lmu.SlitherThink;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.scene.transform.Scale;

import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.LoadSave;

public class App extends Application {

    // Conteneur GLOBAL qui va garder le zoom en permanence
    public static StackPane conteneurVues = new StackPane();

    @Override
    public void start(Stage stage) throws Exception {
        // Chargement de la save
        SaveManager saveManager = new SaveManager(LoadSave.getInstance(""));
        saveManager.separerLesSauvegardes();
        saveManager.sauvegarderJsonDansArborescence("");
        

        // Enregistrement des vues (lazy loading - chargées uniquement quand nécessaire)
        GestionnaireVues.registerView("pseudo", "/fxml/pseudo.fxml");
        GestionnaireVues.registerView("menuAccueil", "/fxml/menuAccueil.fxml");
        GestionnaireVues.registerView("choixMode", "/fxml/choixMode.fxml");
        GestionnaireVues.registerView("choixDifficulte", "/fxml/choixDifficulte.fxml");
        GestionnaireVues.registerView("choixPartieAventure", "/fxml/choixPartieAventure.fxml");
        GestionnaireVues.registerView("finPartieAventure", "/fxml/finPartieAventure.fxml");
        GestionnaireVues.registerView("leaderboards", "/fxml/leaderboards.fxml");
        GestionnaireVues.registerView("pause", "/fxml/pause.fxml");
        GestionnaireVues.registerView("techniques", "/fxml/technique.fxml");
        GestionnaireVues.registerView("partie", "/fxml/partie.fxml");
        GestionnaireVues.registerView("partieTimer", "/fxml/partieTimer.fxml");

        changerVue("pseudo"); 

        // Création du fond noir de l'écran
        Pane root = new Pane();
        root.getChildren().add(conteneurVues); // Le conteneur global est posé sur le fond noir

        Scene scene = new Scene(root, 1280, 720);
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

        String css = this.getClass().getResource("/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        // Configuration plein écran AVANT show()
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);  // Active AVANT show()

        stage.show();
    }

    // Méthode pour changer de page
    public static void changerVue(String nomVue) {
        Region nouvelleVue = (Region) GestionnaireVues.getView(nomVue);
        if (nouvelleVue == null) {
            System.err.println("Erreur : La vue " + nomVue + " est introuvable !");
            return;
        }
        conteneurVues.getChildren().setAll(nouvelleVue);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
