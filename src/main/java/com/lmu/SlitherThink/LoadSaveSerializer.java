package com.lmu.SlitherThink;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class LoadSaveSerializer implements JsonSerializer<loadSave> {
    @Override
    public JsonElement serialize(loadSave src, Type typeOfSrc, JsonSerializationContext context) {
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
