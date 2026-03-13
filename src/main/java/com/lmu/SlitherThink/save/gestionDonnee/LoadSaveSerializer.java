package com.lmu.SlitherThink.save.gestionDonnee;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.SaveGlobal;

/**
 * Sérialiseur personnalisé pour la classe {@link LoadSave}.
 * Permet de convertir un objet {@link LoadSave} en JSON avec des références spécifiques
 * pour les fichiers associés.
 */
public class LoadSaveSerializer implements JsonSerializer<LoadSave> {

    /**
     * Sérialise un objet {@link LoadSave} en JSON.
     * Ajoute des références spécifiques pour les fichiers associés (grille, technique, sauvegardes).
     *
     * @param src       L'objet {@link LoadSave} à sérialiser.
     * @param typeOfSrc Le type de l'objet source.
     * @param context   Le contexte de sérialisation.
     * @return Un objet JSON représentant l'objet {@link LoadSave}.
     */
    @Override
    public JsonElement serialize(LoadSave src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("grille", "fichier:SaveGrille");
        obj.addProperty("technique", "fichier:SaveTechnique");
        obj.addProperty("saveGlobal", "fichier:SaveGlobal");

        com.google.gson.JsonArray detailsRefs = new com.google.gson.JsonArray();
        SaveGlobal sg = src.getSaveGlobal();
        if (sg != null) {
            java.util.function.Consumer<java.util.List<savePartieLienJoueur>> addRefs = list -> {
                if (list == null) return;
                for (savePartieLienJoueur sp : list) {
                    DetailleSavePartie det = sp != null ? sp.getDetailleSave() : null;
                    if (det != null) {
                        String name = det.getNameClass();
                        detailsRefs.add("fichier:" + (name != null ? name : "DetailleSavePartie"));
                    }
                }
            };
            addRefs.accept(sg.getSauvegardeLibre());
            addRefs.accept(sg.getSauvegardeAventure());
        }
        if (detailsRefs.size() > 0) {
            obj.add("DetailleSavePartie", detailsRefs);
        }
        return obj;
    }
}
