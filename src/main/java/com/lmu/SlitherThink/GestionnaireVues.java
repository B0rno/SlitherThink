package com.lmu.SlitherThink;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GestionnaireVues {
    private static final Map<String, Parent> views = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();

    public static void loadView(String nom, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(GestionnaireVues.class.getResource(fxmlPath));
        Parent root = loader.load();
        views.put(nom, root);
        // On stocke aussi le contrôleur associé à cette vue
        controllers.put(nom, loader.getController());
    }

    public static Object getController(String nom) {
        return controllers.get(nom);
    }

    public static Parent getView(String nom) {
        return views.get(nom);
    }
}