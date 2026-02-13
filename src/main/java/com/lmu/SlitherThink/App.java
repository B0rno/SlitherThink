package com.lmu.SlitherThink;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        

        //préchargement des xml pour ne plus avoir de transition
        //possibilité de changer le chemin des xml mais pas le nom de la vue
        GestionnaireVues.loadView("pseudo", "/fxml/pseudo.fxml");
        GestionnaireVues.loadView("menuAccueil", "/fxml/menuAccueil.fxml");
        GestionnaireVues.loadView("choixMode", "/fxml/choixMode.fxml");
        GestionnaireVues.loadView("choixDifficulte", "/fxml/choixDifficulte.fxml");
        GestionnaireVues.loadView("choixPartieAventure", "/fxml/choixPartieAventure.fxml");
        GestionnaireVues.loadView("finPartieAventure", "/fxml/finPartieAventure.fxml");
        GestionnaireVues.loadView("leaderboards", "/fxml/leaderboards.fxml");
        GestionnaireVues.loadView("pause", "/fxml/pause.fxml");
        

        Scene scene = new Scene(GestionnaireVues.getView("pseudo"));
        stage.setTitle("SlitherThink");
        stage.setScene(scene);
        stage.setFullScreenExitHint("");

        
        javafx.application.Platform.runLater(() -> {
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        });
        stage.show();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //impossible de sortir du plein écran avec une touche
    }

    public static void main(String[] args) {
        launch(args);
    }
}
