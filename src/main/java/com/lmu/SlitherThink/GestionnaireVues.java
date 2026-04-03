package com.lmu.SlitherThink;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Gère le chargement et la mise en cache des vues FXML (Lazy Loading).
 * Permet d'accéder aux interfaces et à leurs contrôleurs de manière centralisée.
 * @author Ilann
 */
public class GestionnaireVues {
    private static final Map<String, Parent> views = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();
    private static final Map<String, String> fxmlPaths = new HashMap<>();

    /**
     * Enregistre le chemin d'une vue pour un chargement ultérieur.
     * @param nom Le nom identifiant de la vue.
     * @param fxmlPath Le chemin vers le fichier FXML.
     */
    public static void registerView(String nom, String fxmlPath) {
        fxmlPaths.put(nom, fxmlPath);
    }

    /**
     * Charge physiquement une vue et son contrôleur.
     * @param nom Le nom identifiant de la vue.
     * @param fxmlPath Le chemin vers le fichier FXML.
     * @throws IOException Si le fichier FXML est introuvable ou illisible.
     */
    public static void loadView(String nom, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(GestionnaireVues.class.getResource(fxmlPath));
        Parent root = loader.load();
        views.put(nom, root);
        controllers.put(nom, loader.getController());
    }

    /**
     * Vérifie si une vue est en cache et la charge si nécessaire.
     * @param nom Le nom de la vue à vérifier.
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

    /**
     * Récupère le contrôleur associé à une vue.
     * @param nom Le nom de la vue.
     * @return L'instance du contrôleur, ou null si inexistante.
     */
    public static Object getController(String nom) {
        loadViewIfNeeded(nom);
        return controllers.get(nom);
    }

    /**
     * Récupère l'élément racine (Parent) d'une vue.
     * @param nom Le nom de la vue.
     * @return Le composant Parent pour l'affichage.
     */
    public static Parent getView(String nom) {
        loadViewIfNeeded(nom);
        return views.get(nom);
    }
}