package com.lmu.SlitherThink.save;

import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.PositionTrait;

import java.util.List;

public class Savetest {
    public static void main(String[] args) {
        LoadSave save = LoadSave.getInstance("");
        System.out.println("Affichage load :");

        savePartieLienJoueur nouvellePartieAventure = savePartieLienJoueur.create(
            "gerome",
            152,
            "./save/saveGrille/grilleJeu1.json"
        );
        DetailleSavePartie detailGerome = DetailleSavePartie.create(
            List.of(
                PositionTrait.create(List.of(0, 0), List.of(1)),
                PositionTrait.create(List.of(0, 1), List.of(0, 2)),
                PositionTrait.create(List.of(1, 1), List.of(3))
            ),
            87,
            2
        );
        nouvellePartieAventure.setDetailleSave(detailGerome);
        save.getSaveGlobal().addSauvegardeAventure(nouvellePartieAventure);

        System.out.println("Partie aventure ajoutée pour gerome.");
        System.out.println("Total sauvegardes aventure: " + save.getSaveGlobal().getSauvegardeAventure().size());

        SaveManager saveManager = new SaveManager(save);

        saveManager.separerLesSauvegardes();
        saveManager.getDossiersJson().forEach((key, value) -> {
            System.out.println("Clé: " + key);
            System.out.println("Valeur JSON: " + value);
            System.out.println("-----------------------------");
        });

        saveManager.sauvegarderJsonDansArborescence("src/main/resources");
        System.out.println("Sauvegardes écrites dans: src/main/resources/save");

        String nomGrille = "grilleJeu1"; // ou "grille12", etc.

        if (save.getGrilles().containsKey(nomGrille)) {
            save.getListeTrait(nomGrille).forEach(trait -> {
                System.out.println("Position: " + trait.getPositionTrait());
                System.out.println("Etat: " + trait.getEtatTrait());
                System.out.println("-----------------------------");
            });
        } else {
            System.out.println("Grille introuvable: " + nomGrille);
            System.out.println("Grilles dispo: " + save.getGrilles().keySet());
        }
    }
}
