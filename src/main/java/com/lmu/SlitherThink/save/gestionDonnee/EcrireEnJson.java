package com.lmu.SlitherThink.save.gestionDonnee;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;



/**
 * Classe utilitaire pour écrire des données JSON dans des fichiers.
 * Cette classe permet de créer des fichiers JSON ou de les écraser s'ils existent déjà.
 */
public class EcrireEnJson {

    /**
     * Constructeur qui écrit immédiatement le contenu JSON dans le fichier spécifié.
     *
     * @param nomFichier Le chemin du fichier où écrire le JSON.
     * @param json       Le contenu JSON à écrire.
     */
    public EcrireEnJson(String nomFichier, String json) {
        ecrireJson(nomFichier, json);
    }

    /**
     * Méthode statique pour écrire du contenu JSON dans un fichier.
     * Si le fichier ou les répertoires parents n'existent pas, ils sont créés.
     *
     * @param nomFichier Le chemin du fichier où écrire le JSON.
     * @param json       Le contenu JSON à écrire.
     */
    public static void ecrireJson(String nomFichier, String json) {
        Path chemin = Paths.get(nomFichier);
        try {
            Path parent = chemin.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(chemin, json, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, 
                StandardOpenOption.TRUNCATE_EXISTING, 
                StandardOpenOption.WRITE);
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
