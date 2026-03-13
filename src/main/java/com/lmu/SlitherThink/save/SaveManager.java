package com.lmu.SlitherThink.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lmu.SlitherThink.save.gestionDonnee.EcrireEnJson;
import com.lmu.SlitherThink.save.gestionDonnee.LoadSaveSerializer;
import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.SaveGlobal;
import com.lmu.SlitherThink.save.structure.SaveGrille;
import com.lmu.SlitherThink.save.structure.SaveTechnique;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveManager {
    private LoadSave save;
    private List<Class<?>> classesAExtraire = List.of(
        SaveGrille.class, DetailleSavePartie.class, SaveGlobal.class, SaveTechnique.class
    );
    private Map<String, String> dossiersJson;

    SaveManager(LoadSave save) {
        this.save = save;
        dossiersJson = new HashMap<>();
    }

    public Map<String, String> getDossiersJson() {
        return dossiersJson;
    }

    public void separerLesSauvegardes() {
        Gson gsonBasique = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        JsonSerializer<Object> separateur = new JsonSerializer<Object>() {
            @Override
            public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
                String nomClasse;
                if (src instanceof DetailleSavePartie) {
                    DetailleSavePartie d = (DetailleSavePartie) src;
                    String customName = d.getNameClass();
                    nomClasse = (customName != null && !customName.isEmpty())
                        ? customName
                        : d.getClass().getSimpleName();
                } else {
                    nomClasse = src.getClass().getSimpleName();
                }
                String jsonExtrait = gsonBasique.toJson(src);
                dossiersJson.put(nomClasse, jsonExtrait);

                return new JsonPrimitive("fichier:" + nomClasse);
            }
        };

        if (save.getGrilles() != null && !save.getGrilles().isEmpty()) {
            save.getGrilles().forEach((nomGrille, grilleObj) -> {
                dossiersJson.put(nomGrille, gsonBasique.toJson(grilleObj));
            });
            if (save.getGrille() != null) {
                dossiersJson.put("SaveGrille", gsonBasique.toJson(save.getGrille()));
            }
        } else {
            dossiersJson.put("SaveGrille", gsonBasique.toJson(save.getGrille()));
        }
        dossiersJson.put("SaveTechnique", gsonBasique.toJson(save.getTechnique()));
        dossiersJson.put("SaveGlobal", gsonBasique.toJson(save.getSaveGlobal()));

        if (save.getSaveGlobal() != null) {
            if (save.getSaveGlobal().getSauvegardeLibre() != null) {
                for (savePartieLienJoueur sp : save.getSaveGlobal().getSauvegardeLibre()) {
                    DetailleSavePartie det = sp.getDetailleSave();
                    if (det != null) {
                        dossiersJson.put(det.getNameClass() != null ? det.getNameClass() : "DetailleSavePartie", gsonBasique.toJson(det));
                    }
                }
            }
            if (save.getSaveGlobal().getSauvegardeAventure() != null) {
                for (savePartieLienJoueur sp : save.getSaveGlobal().getSauvegardeAventure()) {
                    DetailleSavePartie det = sp.getDetailleSave();
                    if (det != null) {
                        dossiersJson.put(det.getNameClass() != null ? det.getNameClass() : "DetailleSavePartie", gsonBasique.toJson(det));
                    }
                }
            }
        }

        GsonBuilder gsonbuilder = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(LoadSave.class, new LoadSaveSerializer());

        for (Class<?> clazz : classesAExtraire) {
            gsonbuilder.registerTypeAdapter(clazz, separateur);
        }

        Gson gsonPrincipal = gsonbuilder.create();
        String jsonPrincipal = gsonPrincipal.toJson(save);
        dossiersJson.put("LoadSave", jsonPrincipal);
    }

    public void sauvegarderJsonDansArborescence(String base) {
        if (dossiersJson.isEmpty()) {
            separerLesSauvegardes();
        }

        dossiersJson.forEach((nomFichier, contenuJson) -> {
            String cheminFichier = determinerChemin(base, nomFichier);
            EcrireEnJson.ecrireJson(cheminFichier, contenuJson);
        });
    }

    private String determinerChemin(String base, String nomFichier) {
        String baseNormalisee = base == null ? "" : base;
        if (!baseNormalisee.isEmpty() && !baseNormalisee.endsWith("/")) {
            baseNormalisee += "/";
        }

        String nomNettoye = nomFichier == null ? "inconnu" : nomFichier.trim();
        if (nomNettoye.isEmpty()) {
            nomNettoye = "inconnu";
        }

        String prefixeSave = baseNormalisee + "save/";

        if (nomNettoye.equalsIgnoreCase("SaveTechnique") || nomNettoye.equalsIgnoreCase("technique")) {
            return prefixeSave + "technique.json";
        }
        if (nomNettoye.equalsIgnoreCase("SaveGlobal")) {
            return prefixeSave + "saveGlobal.json";
        }
        if (nomNettoye.equalsIgnoreCase("LoadSave")) {
            return prefixeSave + "loadSave.json";
        }
        if (nomNettoye.equalsIgnoreCase("SaveGrille")) {
            return prefixeSave + "saveGrille/grilleJeu1.json";
        }

        if (nomNettoye.matches("\\d+")) {
            return prefixeSave + "saveJoueur/" + nomNettoye + ".json";
        }

        if (nomNettoye.toLowerCase().contains("grille")) {
            return prefixeSave + "saveGrille/" + nomNettoye + ".json";
        }

        return prefixeSave + "autres/" + nomNettoye + ".json";
    }

}
