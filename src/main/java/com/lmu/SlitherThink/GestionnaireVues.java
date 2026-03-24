package com.lmu.SlitherThink;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GestionnaireVues {
    private static final Map<String, Parent> views = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();
    private static final Map<String, String> fxmlPaths = new HashMap<>();  // Nouveau : stocke les paths

    /**
     * Enregistre une vue sans la charger (lazy loading).
     */
    public static void registerView(String nom, String fxmlPath) {
        fxmlPaths.put(nom, fxmlPath);
    }

    /**
     * Charge une vue immédiatement (ancien comportement).
     */
    public static void loadView(String nom, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(GestionnaireVues.class.getResource(fxmlPath));
        Parent root = loader.load();
        views.put(nom, root);
        controllers.put(nom, loader.getController());
    }

    /**
     * Charge une vue à la demande si elle n'est pas déjà chargée.
     */
    private static void loadViewIfNeeded(String nom) {
        if (!views.containsKey(nom) && fxmlPaths.containsKey(nom)) {
            try {
                loadView(nom, fxmlPaths.get(nom));
            } catch (IOException e) {
                System.err.println("Erreur chargement vue " + nom + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static Object getController(String nom) {
        loadViewIfNeeded(nom);  // Charge si besoin
        return controllers.get(nom);
    }

    public static Parent getView(String nom) {
        loadViewIfNeeded(nom);  // Charge si besoin
        return views.get(nom);
    }
}