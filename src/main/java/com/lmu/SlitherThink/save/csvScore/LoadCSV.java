package com.lmu.SlitherThink.save.csvScore;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import com.lmu.SlitherThink.save.LoadSave;
import com.opencsv.bean.CsvToBeanBuilder;

/**
 * Utilitaire de chargement de fichiers CSV.
 * Permet de lire des fichiers CSV et de les convertir en objets Java.
 * Gère le chargement depuis le système de fichiers ou depuis les ressources.
 *
 */
public class LoadCSV {

    /**
     * Lit un fichier CSV et le convertit en liste d'objets.
     * Tente d'abord de lire depuis le système de fichiers, puis depuis les ressources si le fichier n'existe pas.
     *
     * @param <T> le type des objets à créer depuis le CSV
     * @param cheminRessource le chemin de la ressource embarquée (fallback)
     * @param cheminFichier le chemin du fichier sur le système de fichiers
     * @param clazz la classe des objets à créer
     * @return la liste des objets lus, ou une liste vide si erreur
     */
    public static <T> List<T> lire(String cheminRessource, String cheminFichier, Class<T> clazz) {
        if (cheminFichier != null) {
            Path path = Paths.get(cheminFichier);
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    return construireLecture(reader, clazz);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        InputStream inputStream = LoadSave.class.getResourceAsStream(cheminRessource);
        if (inputStream == null) {
            System.err.println("Ressource CSV introuvable: " + cheminRessource);
            return Collections.emptyList();
        }

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return construireLecture(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Construit et configure le parser CSV pour lire le fichier.
     *
     * @param <T> le type des objets à créer
     * @param reader le lecteur de flux CSV
     * @param clazz la classe cible pour la désérialisation
     * @return la liste des objets désérialisés depuis le CSV
     */
    private static <T> List<T> construireLecture(Reader reader, Class<T> clazz) {
        return new CsvToBeanBuilder<T>(reader)
            .withType(clazz)
            .withIgnoreLeadingWhiteSpace(true)
            .build()
            .parse();
    }
}
