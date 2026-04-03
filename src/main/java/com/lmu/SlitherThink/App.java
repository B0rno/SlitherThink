package com.lmu.SlitherThink;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.scene.transform.Scale;

import com.lmu.SlitherThink.boutonsAction.ChoixPartieAventure;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.LoadSave;

/**
 * Point d'entrée principal de l'application JavaFX.
 * Gère le cycle de vie de la fenêtre, le système de zoom adaptatif (scaling) 
 * et la navigation entre les différentes vues du jeu.
 * @author Ilann
 */
public class App extends Application {

    /** Conteneur global accueillant les différentes vues avec une transformation de mise à l'échelle. */
    public static StackPane conteneurVues = new StackPane();

    /**
     * Initialise l'application, configure le gestionnaire de vues et lance la scène principale.
     * @param stage Le support principal de l'application.
     * @throws Exception Si le chargement des ressources FXML ou CSS échoue.
     */
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
        GestionnaireVues.registerView("finPartieLibre", "/fxml/finPartieLibre.fxml");
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

        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();

        // Solution cross-platform 
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(250));
        pause.setOnFinished(event -> {
            // Demander le focus avant le plein écran 
            stage.requestFocus();
            stage.toFront();

            // Petit délai supplémentaire après requestFocus
            javafx.animation.PauseTransition pause2 = new javafx.animation.PauseTransition(javafx.util.Duration.millis(50));
            pause2.setOnFinished(e -> {
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
            });
            pause2.play();
        });
        pause.play();
    }

    /**
     * Remplace la vue actuelle par une nouvelle vue chargée depuis le gestionnaire.
     * @param nomVue Le nom identifiant de la vue à afficher.
     */
    public static void changerVue(String nomVue) {
        Region nouvelleVue = (Region) GestionnaireVues.getView(nomVue);
        if (nouvelleVue == null) {
            System.err.println("Erreur : La vue " + nomVue + " est introuvable !");
            return;
        }
        conteneurVues.getChildren().setAll(nouvelleVue);

        // Rafraichissement specifique pour les vues mises en cache qui en ont besoin.
        if ("choixPartieAventure".equals(nomVue)) {
            Object controller = GestionnaireVues.getController(nomVue);
            if (controller instanceof ChoixPartieAventure choixPartieAventureController) {
                choixPartieAventureController.onViewShown();
            }
        }
    }

    /**
     * Point d'entrée du programme.
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }
}