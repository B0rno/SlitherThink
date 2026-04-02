package com.lmu.SlitherThink;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.csvScore.SaveCSV;
import com.lmu.SlitherThink.save.csvScore.structure.StructureCSV;
import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.PositionTrait;
import com.lmu.SlitherThink.save.structure.SaveGlobal;

public class Savetest {
    public static void main(String[] args) {
        String basePath = "src/main/resources";
        LoadSave save = LoadSave.getInstance(basePath);
        afficherHeader("1) CHARGER");
        System.out.println("Base path: " + save.getBasePath());
        System.out.println("Singleton LoadSave: OK");

        afficherHeader("2) ACCEDER AUX DONNEES CHARGEES");
        afficherInfosGrilles(save);
        afficherInfosSauvegardes(save);

        afficherHeader("3) CREER ET RAJOUTER DES PARTIES");
        int idAventure = prochainId(save.getSaveGlobal());
        ajouterPartieAventure(save, "gerome", idAventure, "grilleJeu1");
        int idLibre = prochainId(save.getSaveGlobal());
        ajouterPartieLibre(save, "gerome", idLibre, "grilleJeu1");
        afficherInfosSauvegardes(save);

        afficherHeader("4) SAUVEGARDER SUR DISQUE");
        SaveManager saveManager = new SaveManager(save);
        saveManager.separerLesSauvegardes();
        saveManager.sauvegarderJsonDansArborescence(basePath);
        System.out.println("Fichiers JSON générés: " + saveManager.getDossiersJson().keySet());
        System.out.println("Ecriture terminée dans: " + basePath + "/save");

        afficherHeader("5) COMMENT ACCEDER");
        System.out.println("- Grilles: save.getGrilles().keySet()");
        System.out.println("- Traits d'une grille: save.getListeTrait(\"nomGrille\")");
        System.out.println("- Sauvegardes aventure: save.getSaveGlobal().getSauvegardeAventure()");
        System.out.println("- Sauvegardes libre: save.getSaveGlobal().getSauvegardeLibre()");

        afficherHeader("5b) AFFICHER UTILISATEUR ID 153");
        save.afficherContenuUtilisateurParId(153);

        afficherHeader("6) loader CSV");
        List<StructureCSV> scores = save.getScores();
        System.out.println("Scores chargés: " + scores.size());
        if (!scores.isEmpty()) {
            StructureCSV premier = scores.get(0);
            System.out.println("Premier score -> pseudo: " + premier.getPseudo() + ", grille: " + premier.getCheminGrille() + ", nbAide: " + premier.getNbAide() + ", chrono: " + premier.getChrono());
        }

        afficherHeader("7)Creer et save CSV");
        List<StructureCSV> saveCSV = new ArrayList<>(scores);
        saveCSV.add(new StructureCSV("alice", "grilleJeu3.json", 2, 150));
        saveCSV.add(new StructureCSV("bob", "grilleJeu4.json", 1, 120));
        SaveCSV.sauvegarder(saveCSV, basePath + "/save/Score.csv");
        System.out.println("Scores ajoutés et sauvegardés dans: " + basePath + "/save/Score.csv");
    }

    private static void afficherHeader(String titre) {
        System.out.println("\n====================================");
        System.out.println(titre);
        System.out.println("====================================");
    }

    private static void afficherInfosGrilles(LoadSave save) {
        Set<String> grilles = save.getGrilles().keySet();
        System.out.println("Grilles disponibles: " + grilles);
        if (!grilles.isEmpty()) {
            String nomGrille = grilles.iterator().next();
            List<PositionTrait> traits = save.getListeTrait(nomGrille);
            System.out.println("Grille choisie: " + nomGrille);
            System.out.println("Nombre de traits: " + traits.size());
            if (!traits.isEmpty()) {
                PositionTrait premierTrait = traits.get(0);
                System.out.println("Premier trait -> position: " + premierTrait.getPositionTrait() + ", etat: " + premierTrait.getEtatTrait());
            }
        }
    }

    private static void afficherInfosSauvegardes(LoadSave save) {
        SaveGlobal saveGlobal = save.getSaveGlobal();
        int nbLibre = saveGlobal != null && saveGlobal.getSauvegardeLibre() != null ? saveGlobal.getSauvegardeLibre().size() : 0;
        int nbAventure = saveGlobal != null && saveGlobal.getSauvegardeAventure() != null ? saveGlobal.getSauvegardeAventure().size() : 0;
        System.out.println("Sauvegardes libre: " + nbLibre);
        System.out.println("Sauvegardes aventure: " + nbAventure);
    }

    private static int prochainId(SaveGlobal saveGlobal) {
        int maxId = 0;
        if (saveGlobal == null) {
            return 1;
        }

        List<savePartieLienJoueur> toutes = new ArrayList<>();
        if (saveGlobal.getSauvegardeLibre() != null) {
            toutes.addAll(saveGlobal.getSauvegardeLibre());
        }
        if (saveGlobal.getSauvegardeAventure() != null) {
            toutes.addAll(saveGlobal.getSauvegardeAventure());
        }

        for (savePartieLienJoueur partie : toutes) {
            if (partie != null && partie.getId() != null && partie.getId() > maxId) {
                maxId = partie.getId();
            }
        }
        return maxId + 1;
    }

    private static void ajouterPartieAventure(LoadSave save, String pseudo, int id, String nomGrille) {
        savePartieLienJoueur partie = savePartieLienJoueur.create(
            pseudo,
            id,
            "./save/saveGrille/" + nomGrille + ".json"
        );
        partie.setDetailleSave(buildDetail(id));
        save.getSaveGlobal().addSauvegardeAventure(partie);
        System.out.println("Ajout aventure -> pseudo=" + pseudo + ", id=" + id);
    }

    private static void ajouterPartieLibre(LoadSave save, String pseudo, int id, String nomGrille) {
        savePartieLienJoueur partie = savePartieLienJoueur.create(
            pseudo,
            id,
            "./save/saveGrille/" + nomGrille + ".json"
        );
        partie.setDetailleSave(buildDetail(id));
        save.getSaveGlobal().addSauvegardeLibre(partie);
        System.out.println("Ajout libre -> pseudo=" + pseudo + ", id=" + id);
    }

    private static DetailleSavePartie buildDetail(int seed) {
        return DetailleSavePartie.create(
            List.of(
                PositionTrait.create(List.of(0, 0), List.of(1)),
                PositionTrait.create(List.of(0, 1), List.of(0, 2)),
                PositionTrait.create(List.of(1, 1), List.of(3))
            ),
            60 + seed,
            seed % 3
        );
    }
}
