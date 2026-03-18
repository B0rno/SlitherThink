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

public class LoadCSV {
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

    private static <T> List<T> construireLecture(Reader reader, Class<T> clazz) {
        return new CsvToBeanBuilder<T>(reader)
            .withType(clazz)
            .withIgnoreLeadingWhiteSpace(true)
            .build()
            .parse();
    }
}
