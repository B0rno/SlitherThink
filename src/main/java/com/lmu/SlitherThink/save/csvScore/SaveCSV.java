package com.lmu.SlitherThink.save.csvScore;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

/**
 * Utilitaire de sauvegarde de fichiers CSV.
 * Permet de convertir une liste d'objets Java en fichier CSV.
 * Crée automatiquement les répertoires parents si nécessaire.
 */
public class SaveCSV {

    /**
     * Sauvegarde une liste d'objets dans un fichier CSV.
     * Crée les répertoires parents si nécessaire.
     *
     * @param <T> le type des objets à sauvegarder
     * @param listToSave la liste d'objets à convertir en CSV
     * @param fichier le chemin du fichier de destination
     */
    public static <T> void sauvegarder(List<T> listToSave, String fichier) {
        Path path = Paths.get(fichier);
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).build();
                beanToCsv.write(listToSave == null ? Collections.emptyList() : listToSave);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
