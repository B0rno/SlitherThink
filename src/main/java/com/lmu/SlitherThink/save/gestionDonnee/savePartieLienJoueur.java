package com.lmu.SlitherThink.save.gestionDonnee;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;

public class savePartieLienJoueur {
    private static final String pathLienJoueurSave = "/save/saveJoueur/";
    private static String basePath = "";
    private String pseudo;
    private Integer id;
    private String path;
    private transient DetailleSavePartie detailleSave;

    savePartieLienJoueur() {}

    savePartieLienJoueur(String pseudo, Integer id, String path) {
        this.pseudo = pseudo;
        this.id = id;
        this.path = path;
    }

    public static void setBasePath(String basePathParam) {
        basePath = basePathParam == null ? "" : basePathParam;
    }

    public void loadDetailleSave() {
        if (id == null) {
            return;
        }

        String pathSaveJoeur = pathLienJoueurSave + id + ".json";
        Gson gson = new Gson();
        try (Reader reader = ouvrirReader(pathSaveJoeur)) {
            if (reader == null) {
                System.err.println("Ressource/Fichier introuvable: " + pathSaveJoeur);
                return;
            }
            detailleSave = gson.fromJson(reader, DetailleSavePartie.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.detailleSave != null) {
            this.detailleSave.setNameClass(this.id != null ? this.id.toString() : null);
        }
    }

    private Reader ouvrirReader(String cheminRessource) throws IOException {
        if (basePath != null && !basePath.isBlank()) {
            String relatif = cheminRessource.startsWith("/") ? cheminRessource.substring(1) : cheminRessource;
            Path fichierExterne = Paths.get(basePath, relatif);
            if (Files.exists(fichierExterne)) {
                return Files.newBufferedReader(fichierExterne, StandardCharsets.UTF_8);
            }
        }

        InputStream is = savePartieLienJoueur.class.getResourceAsStream(cheminRessource);
        if (is == null) {
            return null;
        }
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }

    public DetailleSavePartie getDetailleSave() {
        if (detailleSave == null) {
            loadDetailleSave();
        }
        return detailleSave;
    }

    public String getPseudo() {
        return pseudo;
    }

    public Integer getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
