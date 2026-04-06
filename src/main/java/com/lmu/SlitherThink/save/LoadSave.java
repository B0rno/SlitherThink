package com.lmu.SlitherThink.save;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.lmu.SlitherThink.save.csvScore.LoadCSV;
import com.lmu.SlitherThink.save.csvScore.structure.StructureCSV;
import com.lmu.SlitherThink.save.gestionDonnee.rechercheSave;
import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;
import com.lmu.SlitherThink.save.structure.PositionTrait;
import com.lmu.SlitherThink.save.structure.SaveGlobal;
import com.lmu.SlitherThink.save.structure.SaveGrille;
import com.lmu.SlitherThink.save.structure.positionGrille;

/**
 * Gestionnaire central de chargement des données du jeu (Singleton).
 * Charge et gère toutes les ressources JSON et CSV au démarrage de l'application.
 *
 * Données gérées:
 * - Grilles de jeu (mode libre et aventure)
 * - Sauvegardes globales
 * - Scores CSV
 *
 * Utilise un système de fallback: fichier système puis ressource embarquée.
 */
public class LoadSave {
    private static LoadSave instance;
    private SaveGrille grille;
    private Map<String, SaveGrille> grilles;
    private SaveGlobal saveGlobal;
    private List<StructureCSV> scores;
    private final String basePath;

    /**
     * Récupère l'instance unique du gestionnaire de chargement.
     * Crée l'instance si elle n'existe pas encore.
     *
     * @param pathBeforeSave le chemin de base pour les fichiers (vide pour auto-détection)
     * @return l'instance unique de LoadSave
     */
    public static synchronized LoadSave getInstance(String pathBeforeSave) {
        if (instance == null) {
            instance = new LoadSave(pathBeforeSave);
        }
        return instance;
    }

    /**
     * Constructeur privé (Singleton).
     * Charge toutes les données du jeu au démarrage: grilles, sauvegardes et scores.
     *
     * @param pathBeforeSave le chemin de base pour les fichiers
     */
    private LoadSave(String pathBeforeSave) {
        this.basePath = determinerBasePath(pathBeforeSave);
        savePartieLienJoueur.setBasePath(this.basePath);

        Gson gson = new Gson();
        saveGlobal = lireJson("/save/saveGlobal.json", cheminFichier("save/saveGlobal.json"), SaveGlobal.class, gson);
        
        // Initialiser avec un objet par défaut si le fichier n'existe pas
        if (saveGlobal == null) {
            saveGlobal = creerSaveGlobalParDefaut();
        }
        
        rechercheSave.setSaveGlobalCourant(saveGlobal);


        grilles = chargerGrilles(gson);
        grille = choisirGrilleParDefaut();
        scores = LoadCSV.lire("/save/Score.csv", cheminFichier("save/Score.csv") , StructureCSV.class);
    }

    /**
     * Charge toutes les grilles depuis les dossiers GrilleJson (racine et Aventure).
     * Scanne le système de fichiers puis utilise les ressources en fallback.
     */
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
             
            }
        }
    
        return resultat;
    }

    /*
    // si on veux prendre en prioriter les fichier sauvegarder coter joueur 
    
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
    
    */

    /**
     * Extrait les noms de grilles depuis les sauvegardes globales.
     */
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

    /**
     * Retire l'extension d'un nom de fichier.
     */
    private String retirerExtension(String nomFichier) {
        int index = nomFichier.lastIndexOf('.');
        if (index <= 0) {
            return nomFichier;
        }
        return nomFichier.substring(0, index);
    }

    /**
     * Choisit la grille par défaut (priorité à grilleJeu1, sinon la première disponible).
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
     * Détermine le chemin de base pour les fichiers.
     * Essaie le chemin fourni, puis le JAR, puis le répertoire courant.
     */
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

    /**
     * Crée un SaveGlobal par défaut avec des listes vides.
     * Utilisé quand le fichier saveGlobal.json n'existe pas.
     */
    private SaveGlobal creerSaveGlobalParDefaut() {
        SaveGlobal sauvegardeParDefaut = new SaveGlobal();
        sauvegardeParDefaut.setSauvegardeLibre(new ArrayList<>());
        sauvegardeParDefaut.setSauvegardeAventure(new ArrayList<>());
        return sauvegardeParDefaut;
    }

    /**
     * Construit un chemin de fichier absolu à partir du basePath et d'un chemin relatif.
     */
    private String cheminFichier(String cheminRelatif) {
        return Paths.get(basePath, cheminRelatif).toString();
    }

    /**
     * Lit un fichier JSON avec fallback sur les ressources embarquées.
     * Essaie d'abord de lire depuis le système de fichiers, puis depuis les ressources.
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

    /**
     * Recharge le SaveGlobal depuis le fichier JSON .
     * Permet de récupérer les dernières modifications après une sauvegarde.
     */
    public void rechargerSaveGlobal() {
        Gson gson = new Gson();
        saveGlobal = lireJson("/save/saveGlobal.json", cheminFichier("save/saveGlobal.json"), SaveGlobal.class, gson);
        rechercheSave.setSaveGlobalCourant(saveGlobal);
    }

    /**
     * Récupère la liste des traits d'une grille spécifique.
     *
     * @param nomGrille le nom de la grille
     * @return la liste des positions de traits
     */
    public List<PositionTrait> getListeTrait(String nomGrille) {
        return this.grilles.get(nomGrille).getListePositionTrait();
    }

    /**
     * @return la grille par défaut
     */
    public SaveGrille getGrille() {
        return grille;
    }

    /**
     * @return la map de toutes les grilles (nom -> grille)
     */
    public Map<String, SaveGrille> getGrilles() {
        return grilles;
    }


    /**
     * @return la sauvegarde globale
     */
    public SaveGlobal getSaveGlobal() {
        return saveGlobal;
    }

    /**
     * @return le chemin de base des fichiers
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * @return la liste des scores CSV
     */
    public List<StructureCSV> getScores() {
        return scores == null ? Collections.emptyList() : scores;
    }

    /**
     * Ajoute un score à la liste.
     *
     * @param score le score à ajouter
     */
    public void ajouterScore(StructureCSV score) {
        if (score == null) {
            return;
        }
        if (scores == null || scores == Collections.<StructureCSV>emptyList()) {
            scores = new ArrayList<>();
        }
        scores.add(score);
    }

    /**
     * Affiche le contenu d'une sauvegarde par son ID (debug).
     * Utilisé dans les tests pour vérifier que les données sont correctement chargées.
     *
     * @param idUtilisateur l'identifiant de la sauvegarde
     */
    public void afficherContenuUtilisateurParId(int idUtilisateur) {
        savePartieLienJoueur partie = chercherSauvegardeParId(idUtilisateur);
        if (partie == null) {
            System.out.println("Aucune sauvegarde trouvée pour l'id: " + idUtilisateur);
            return;
        }

        System.out.println("Sauvegarde trouvée pour l'id: " + idUtilisateur);
        System.out.println("Pseudo: " + partie.getPseudo());
        System.out.println("Grille: " + partie.getPath());

        var detaille = partie.getDetailleSave();
        if (detaille == null) {
            System.out.println("Aucun détail de partie disponible.");
            return;
        }

        System.out.println("Chronometre: " + detaille.getChronometre());
        System.out.println("Nb aides: " + detaille.getNbAides());
        List<PositionTrait> etatGrille = detaille.getEtatGrille();
        if (etatGrille == null || etatGrille.isEmpty()) {
            System.out.println("Etat de grille vide.");
            return;
        }

        System.out.println("Etat de grille:");
        for (PositionTrait trait : etatGrille) {
            if (trait == null) {
                continue;
            }
            System.out.println("- position=" + trait.getPositionTrait() + ", etat=" + trait.getEtatTrait());
        }
    }

    /**
     * Recherche une sauvegarde par son identifiant.
     *
     * @param idUtilisateur l'identifiant de la sauvegarde
     * @return la sauvegarde trouvée, ou null
     */
    public savePartieLienJoueur chercherSauvegardeParId(int idUtilisateur) {
        return rechercheSave.chercherSauvegardeParId(saveGlobal, idUtilisateur);
    }

    /**
     * Récupère toutes les grilles d'un niveau de difficulté donné.
     *
     * @param nvDifficulte le niveau de difficulté (0=facile, 1=moyen, 2=difficile)
     * @return la liste des grilles correspondantes
     */
    public List<SaveGrille> getGrillesNv(int nvDifficulte) {
        return grilles.entrySet().stream()
            .filter(entry -> entry.getValue() != null && entry.getValue().getNvGrille() == nvDifficulte)
            .map(Map.Entry::getValue)
            .toList();
    }
}
