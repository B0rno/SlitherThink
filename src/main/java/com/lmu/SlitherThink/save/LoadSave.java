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

public class LoadSave {
    private static LoadSave instance;
    private SaveGrille grille;
    private Map<String, SaveGrille> grilles;
    private SaveTechnique technique;
    private SaveGlobal saveGlobal;
    private final String basePath;

    public static synchronized LoadSave getInstance(String pathBeforeSave) {
        if (instance == null) {
            instance = new LoadSave(pathBeforeSave);
        }
        return instance;
    }

    private LoadSave(String pathBeforeSave) {
        this.basePath = determinerBasePath(pathBeforeSave);
        savePartieLienJoueur.setBasePath(this.basePath);

        Gson gson = new Gson();
        technique = lireJson("/save/technique.json", cheminFichier("save/technique.json"), SaveTechnique.class, gson);
        saveGlobal = lireJson("/save/saveGlobal.json", cheminFichier("save/saveGlobal.json"), SaveGlobal.class, gson);

        grilles = chargerGrilles(gson);
        grille = choisirGrilleParDefaut();
    }

    private Map<String, SaveGrille> chargerGrilles(Gson gson) {
        Map<String, SaveGrille> resultat = new LinkedHashMap<>();
    
        // Liste des sous-dossiers à scanner dans GrilleJson
        String[] sousDossiers = {"", "Aventure"}; // "" pour la racine (Libre), "Aventure" 
    
        for (String sousDir : sousDossiers) {
            // Construction des chemins physiques possibles
            Path[] cheminsPossibles = {
                Paths.get(System.getProperty("user.dir"), "src/main/resources/GrilleJson", sousDir),
                Paths.get(basePath, "GrilleJson", sousDir),
                Paths.get("src/main/resources/GrilleJson", sousDir)
            };
    
            boolean dossierTrouveSurDisque = false;
            for (Path dossier : cheminsPossibles) {
                if (Files.isDirectory(dossier)) {
                    try (var fichiers = Files.list(dossier)) {
                        fichiers
                            .filter(Files::isRegularFile)
                            .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".json"))
                            .forEach(path -> {
                                String nom = retirerExtension(path.getFileName().toString());
                                // On utilise lireJson pour bénéficier de la double sécurité (Fichier puis Ressource)
                                SaveGrille g = lireJson("/GrilleJson/" + (sousDir.isEmpty() ? "" : sousDir + "/") + path.getFileName().toString(), 
                                                       path.toString(), SaveGrille.class, gson);
                                if (g != null) {
                                    resultat.put(nom, g);
                                }
                            });
                        dossierTrouveSurDisque = true;
                    } catch (IOException e) {
                        System.err.println("Erreur lors du scan du dossier : " + dossier);
                    }
                    break; // On a trouvé le dossier physique, on passe au sous-dossier suivant
                }
            }
    
            // Si le scan disque a échoué pour ce dossier (cas du JAR), on peut tenter un chargement manuel 
            // par ressource pour des grilles spécifiques si nécessaire.
            if (!dossierTrouveSurDisque) {
                System.out.println("Dossier physique non trouvé pour " + (sousDir.isEmpty() ? "racine" : sousDir) + ", utilisation des ressources.");
                // Note : Lister des fichiers à l'intérieur d'un JAR est complexe sans bibliothèque tierce.
                // Si tu es dans un JAR, assure-toi que tes chemins de ressources sont corrects.
            }
        }
    
        return resultat;
    }

    private Set<String> extraireNomsGrillesDepuisSaveGlobal() {
        Set<String> noms = new HashSet<>();
        if (saveGlobal == null) {
            return noms;
        }

        java.util.function.Consumer<List<savePartieLienJoueur>> collecteur = list -> {
            if (list == null) {
                return;
            }
            for (savePartieLienJoueur partie : list) {
                if (partie == null || partie.getPath() == null || partie.getPath().isBlank()) {
                    continue;
                }
                String fichier = Paths.get(partie.getPath().replace("\\", "/")).getFileName().toString();
                if (fichier.toLowerCase().endsWith(".json")) {
                    noms.add(retirerExtension(fichier));
                }
            }
        };

        collecteur.accept(saveGlobal.getSauvegardeLibre());
        collecteur.accept(saveGlobal.getSauvegardeAventure());
        return noms;
    }

    private String retirerExtension(String nomFichier) {
        int index = nomFichier.lastIndexOf('.');
        if (index <= 0) {
            return nomFichier;
        }
        return nomFichier.substring(0, index);
    }

    private SaveGrille choisirGrilleParDefaut() {
        if (grilles == null || grilles.isEmpty()) {
            return null;
        }

        if (grilles.containsKey("grilleJeu1")) {
            return grilles.get("grilleJeu1");
        }

        return grilles.values().iterator().next();
    }

    private String determinerBasePath(String pathBeforeSave) {
        if (pathBeforeSave != null && !pathBeforeSave.isBlank()) {
            return Paths.get(pathBeforeSave).toAbsolutePath().normalize().toString();
        }

        try {
            URI location = LoadSave.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            Path codeSourcePath = Paths.get(location).toAbsolutePath().normalize();

            if (Files.isRegularFile(codeSourcePath) && codeSourcePath.toString().endsWith(".jar")) {
                Path parent = codeSourcePath.getParent();
                if (parent != null) {
                    return parent.toString();
                }
            }
        } catch (Exception ignored) {
        }

        return Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().normalize().toString();
    }

    private String cheminFichier(String cheminRelatif) {
        return Paths.get(basePath, cheminRelatif).toString();
    }

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

    public void afficherTechn() {
        if (technique != null && technique.getStockageLangague() != null) {
            for (languageContenue langcont : technique.getStockageLangague()) {
                if (langcont == null) continue;
                System.out.println("Langage: " + langcont.getLangage());
                List<contenuTechnique> contenus = langcont.getContenu();
                if (contenus == null) continue;
                for (contenuTechnique contenu : contenus) {
                    if (contenu == null) continue;
                    System.out.println("----------------\nNiveau: " + contenu.getNv());
                    List<stockageTechnique> techs = contenu.getTechniqueParsNv();
                    if (techs == null) {
                        System.out.println("Aucune technique disponible pour ce niveau.");
                        continue;
                    }
                    for (stockageTechnique tech : techs) {
                        if (tech == null) continue;
                        System.out.println("----------------\nNom de la technique: " + tech.getName());
                        System.out.println("----------------Description----------------\n" + tech.getDescription());
                    }
                }
            }
        } else {
            System.out.println("Aucune technique disponible.");
        }
    }

    public void afficherGrille(String nomGrille, SaveGrille grille) {
        if (grille != null) {
            System.out.println("Nom de la grille: " + nomGrille);
            System.out.println("Taille de la grille: " + grille.getTailleGrille());
            List<positionGrille> cases = grille.getNumeroCases();
            if (cases != null) {
                for (positionGrille posgrille : cases) {
                    if (posgrille == null) continue;
                    System.out.println("----------------\nPosition: " + posgrille.getPositionGrille());
                    System.out.println("Valeur: " + posgrille.getValeurGrille());
                }
            } else {
                System.out.println("Aucune case disponible dans la grille.");
            }
        } else {
            System.out.println("Aucune grille disponible.");
        }
    }

    public void affichertoJson() {
        Gson gson = new Gson();
        String jsonGrille = gson.toJson(grille);
        String jsonTechnique = gson.toJson(technique);

        System.out.println("Grille en JSON:\n" + jsonGrille);
        System.out.println("Technique en JSON:\n" + jsonTechnique);
    }

    public List<PositionTrait> getListeTrait(String nomGrille) {
        return this.grilles.get(nomGrille).getListePositionTrait();
    }

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


    @Override
    public String toString() {
        this.afficherGrille(this.grilles.keySet().stream().findFirst().orElse("Grille par défaut"), this.grilles.values().stream().findFirst().orElse(grille));
        this.afficherTechn();
        System.out.println("\n\nAffichage en JSON:");
        this.affichertoJson();
        return "Affichage de la grille et des techniques terminé.";
    }


    public List<SaveGrille> getGrillesNv(int nvDifficulte) {
        return grilles.entrySet().stream()
            .filter(entry -> entry.getValue() != null && entry.getValue().getNvGrille() == nvDifficulte)
            .map(Map.Entry::getValue)
            .toList();
    }
}
