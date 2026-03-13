package com.lmu.SlitherThink.save;

import com.google.gson.Gson;
import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;
import com.lmu.SlitherThink.save.structure.SaveGlobal;
import com.lmu.SlitherThink.save.structure.SaveGrille;
import com.lmu.SlitherThink.save.structure.SaveTechnique;
import com.lmu.SlitherThink.save.structure.contenuTechnique;
import com.lmu.SlitherThink.save.structure.languageContenue;
import com.lmu.SlitherThink.save.structure.positionGrille;
import com.lmu.SlitherThink.save.structure.stockageTechnique;
import com.lmu.SlitherThink.save.structure.PositionTrait;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe principale pour charger et gérer les sauvegardes du jeu.
 * Cette classe utilise le pattern Singleton pour garantir une seule instance.
 * Elle permet de charger les grilles, techniques et sauvegardes globales depuis des fichiers JSON.
 */
public class LoadSave {
    private static LoadSave instance;
    private SaveGrille grille;
    private Map<String, SaveGrille> grilles;
    private SaveTechnique technique;
    private SaveGlobal saveGlobal;
    private final String basePath;

    /**
     * Retourne l'instance unique de `LoadSave`.
     * Si l'instance n'existe pas, elle est créée avec le chemin spécifié.
     *
     * @param pathBeforeSave Le chemin de base pour les fichiers de sauvegarde.
     * @return L'instance unique de `LoadSave`.
     */
    public static synchronized LoadSave getInstance(String pathBeforeSave) {
        if (instance == null) {
            instance = new LoadSave(pathBeforeSave);
        }
        return instance;
    }

    /**
     * Constructeur privé pour initialiser les données de sauvegarde.
     *
     * @param pathBeforeSave Le chemin de base pour les fichiers de sauvegarde.
     */
    private LoadSave(String pathBeforeSave) {
        this.basePath = determinerBasePath(pathBeforeSave);
        savePartieLienJoueur.setBasePath(this.basePath);

        Gson gson = new Gson();
        technique = lireJson("/save/technique.json", cheminFichier("save/technique.json"), SaveTechnique.class, gson);
        saveGlobal = lireJson("/save/saveGlobal.json", cheminFichier("save/saveGlobal.json"), SaveGlobal.class, gson);

        grilles = chargerGrilles(gson);
        grille = choisirGrilleParDefaut();
    }

    /**
     * Charge toutes les grilles disponibles depuis les fichiers JSON.
     *
     * @param gson L'instance de Gson pour la désérialisation.
     * @return Une map contenant les grilles avec leurs noms comme clés.
     */
    private Map<String, SaveGrille> chargerGrilles(Gson gson) {
        Map<String, SaveGrille> resultat = new LinkedHashMap<>();

        Path dossierGrilles = Paths.get(basePath, "save", "saveGrille");
        if (Files.isDirectory(dossierGrilles)) {
            try (var fichiers = Files.list(dossierGrilles)) {
                fichiers
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".json"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .forEach(path -> {
                        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                            SaveGrille grilleChargee = gson.fromJson(reader, SaveGrille.class);
                            if (grilleChargee != null) {
                                String nom = retirerExtension(path.getFileName().toString());
                                resultat.put(nom, grilleChargee);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!resultat.isEmpty()) {
            return resultat;
        }

        Set<String> nomsCandidates = extraireNomsGrillesDepuisSaveGlobal();
        nomsCandidates.add("grilleJeu1");

        for (String nomGrille : nomsCandidates) {
            String ressource = "/save/saveGrille/" + nomGrille + ".json";
            SaveGrille grilleChargee = lireJson(ressource, null, SaveGrille.class, gson);
            if (grilleChargee != null) {
                resultat.put(nomGrille, grilleChargee);
            }
        }

        return resultat;
    }

    /**
     * Retourne la grille par défaut (ou la première grille disponible).
     *
     * @return La grille par défaut.
     */
    private SaveGrille choisirGrilleParDefaut() {
        if (grilles == null || grilles.isEmpty()) {
            return null;
        }

        if (grilles.containsKey("grilleJeu1")) {
            return grilles.get("grilleJeu1");
        }

        return grilles.values().iterator().next();
    }

    /**
     * Lit un fichier JSON et le désérialise en un objet de type spécifié.
     *
     * @param cheminRessource Le chemin de la ressource.
     * @param cheminFichier   Le chemin du fichier.
     * @param type            Le type de l'objet à désérialiser.
     * @param gson            L'instance de Gson pour la désérialisation.
     * @param <T>             Le type de l'objet.
     * @return L'objet désérialisé ou null en cas d'erreur.
     */
    private <T> T lireJson(String cheminRessource, String cheminFichier, Class<T> type, Gson gson) {
        if (cheminFichier != null) {
            Path path = Paths.get(cheminFichier);
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    return gson.fromJson(reader, type);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        InputStream is = LoadSave.class.getResourceAsStream(cheminRessource);
        if (is == null) {
            System.err.println("Ressource introuvable: " + cheminRessource);
            return null;
        }
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getters pour accéder aux données chargées
    public SaveGrille getGrille() {
        return grille;
    }

    public Map<String, SaveGrille> getGrilles() {
        return grilles;
    }

    public SaveTechnique getTechnique() {
        return technique;
    }

    public SaveGlobal getSaveGlobal() {
        return saveGlobal;
    }

    public String getBasePath() {
        return basePath;
    }
}
