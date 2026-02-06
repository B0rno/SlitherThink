package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public abstract class ChangementFenetre {

    // Centralise la logique de récupération du bouton depuis l'événement
    protected void handleSceneChange(ActionEvent event, String fxmlPath) {
        Button btn = (Button) event.getSource();
        switchScene(btn, fxmlPath);
    }

    protected void switchScene(Button sourceButton, String fxmlPath) {
        try {
            URL location = getClass().getResource(fxmlPath);
            if (location == null) {
                System.err.println("ERREUR : FXML introuvable -> " + fxmlPath);
                return;
            }
            Parent root = FXMLLoader.load(location);
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}