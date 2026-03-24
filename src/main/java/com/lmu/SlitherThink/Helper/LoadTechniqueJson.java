package com.lmu.SlitherThink.Helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton qui charge et gère les descriptions détaillées des techniques depuis un fichier JSON.
 * Ce chargeur permet d'accéder rapidement aux descriptions complètes des techniques de résolution
 * pour affichage dans l'interface utilisateur ou le menu "Techniques".
 *
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */
public class LoadTechniqueJson {
    private static LoadTechniqueJson instance;
    private Map<String, String> techniqueDescriptions;

    private LoadTechniqueJson() {
        techniqueDescriptions = new HashMap<>();
        chargerJson();
    }

    /**
     * Retourne l'instance unique du chargeur de techniques.
     * Si l'instance n'existe pas encore, elle est créée et le JSON est chargé automatiquement.
     *
     * @return l'instance unique de LoadTechniqueJson
     */
    public static LoadTechniqueJson getInstance() {
        if (instance == null) {
            instance = new LoadTechniqueJson();
        }
        return instance;
    }

    /**
     * Charge le fichier JSON contenant toutes les techniques et remplit la Map techniqueDescriptions.
     * Le fichier doit se trouver dans /resources/techniques.json avec la structure :
     * stockage_langague > contenu > techniqueParsNv > techniques.
     * En cas d'erreur de lecture, la stacktrace est affichée.
     */
    private void chargerJson() {
        try (InputStreamReader reader = new InputStreamReader(getClass()
                                            .getResourceAsStream("/resources/techniques.json"))) {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            JsonArray stockage_langague = root.getAsJsonArray("stockage_langague");
            JsonObject firstJsonObject = stockage_langague.get(0).getAsJsonObject();
            JsonArray contenu = firstJsonObject.getAsJsonArray("contenu");

            for (int i = 0; i < contenu.size(); i++) {
                JsonObject nvDificulte = contenu.get(i).getAsJsonObject();
                JsonArray techniqueParsNv = nvDificulte.getAsJsonArray("techniqueParsNv");
                for(int j = 0; j< techniqueParsNv.size();j++){
                    JsonObject technique = techniqueParsNv.get(j).getAsJsonObject();
                    String nom = technique.get("name").getAsString();
                    String description = technique.get("description").getAsString();
                    techniqueDescriptions.put(nom, description);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère la description détaillée d'une technique à partir de son nom.
     *
     * @param nomTechnique le nom exact de la technique tel qu'il apparaît dans le JSON
     * @return la description complète de la technique, ou "Description non disponible" si non trouvée
     */
    public String getDescription(String nomTechnique) {
        return techniqueDescriptions.getOrDefault(nomTechnique, "Description non disponible");
    }
}
