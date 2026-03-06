package com.lmu.SlitherThink.save.gestionDonnee;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EcrireenJson {
    public EcrireenJson(String nomFichier, String json) {
        ecrireJson(nomFichier, json);
    }

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
