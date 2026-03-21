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

/**
 * Classe représentant le lien entre un joueur et une sauvegarde de partie.
 * Permet de charger et de gérer les détails d'une sauvegarde associée à un joueur.
 */
public class savePartieLienJoueur {
    private static final String pathLienJoueurSave = "/save/saveJoueur/";
    private static String basePath = "";
    private String pseudo;
    private Integer id;
    private String path;
    private transient DetailleSavePartie detailleSave;

    /**
     * Constructeur par défaut pour la désérialisation.
     */
    savePartieLienJoueur() {}

    /**
     * Constructeur pour créer un lien entre un joueur et une sauvegarde.
     *
     * @param pseudo Le pseudo du joueur.
     * @param id     L'identifiant unique de la sauvegarde.
     * @param path   Le chemin vers le fichier de sauvegarde.
     */
    savePartieLienJoueur(String pseudo, Integer id, String path) {
        this.pseudo = pseudo;
        this.id = id;
        this.path = path;
    }

    /**
     * Crée une instance de {@link savePartieLienJoueur}.
     *
     * @param pseudo Le pseudo du joueur.
     * @param id     L'identifiant unique de la sauvegarde.
     * @param path   Le chemin vers le fichier de sauvegarde.
     * @return Une nouvelle instance de {@link savePartieLienJoueur}.
     */
    public static savePartieLienJoueur create(String pseudo, Integer id, String path) {
        return new savePartieLienJoueur(pseudo, id, path);
    }

    /**
     * Définit le chemin de base pour les fichiers de sauvegarde.
     *
     * @param basePathParam Le chemin de base.
     */
    public static void setBasePath(String basePathParam) {
        basePath = basePathParam == null ? "" : basePathParam;
    }

    /**
     * Charge les détails de la sauvegarde associée à ce lien.
     * Lit le fichier JSON correspondant et désérialise les données.
     */
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

    /**
     * Ouvre un lecteur pour lire un fichier JSON.
     *
     * @param cheminRessource Le chemin vers la ressource ou le fichier.
     * @return Un {@link Reader} pour lire le fichier, ou null si le fichier est introuvable.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
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

    /**
     * Retourne les détails de la sauvegarde associée.
     * Charge les détails si ce n'est pas encore fait.
     *
     * @return Les détails de la sauvegarde.
     */
    public DetailleSavePartie getDetailleSave() {
        if (detailleSave == null) {
            loadDetailleSave();
        }
        return detailleSave;
    }

    /**
     * Définit les détails de la sauvegarde associée.
     *
     * @param detailleSave Les détails de la sauvegarde.
     */
    public void setDetailleSave(DetailleSavePartie detailleSave) {
        this.detailleSave = detailleSave;
        if (this.detailleSave != null && this.id != null) {
            this.detailleSave.setNameClass(this.id.toString());
        }
    }

    // Getters et setters pour les champs privés
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
