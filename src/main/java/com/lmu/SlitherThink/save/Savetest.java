package com.lmu.SlitherThink.save;

public class Savetest {
    public static void main(String[] args) {
        LoadSave save = LoadSave.getInstance("");
        System.out.println("Affichage load :");
        //System.out.println(save);
        /*SaveManager saveManager = new SaveManager(save);

        saveManager.separerLesSauvegardes();
        saveManager.getDossiersJson().forEach((key, value) -> {
            System.out.println("Clé: " + key);
            System.out.println("Valeur JSON: " + value);
            System.out.println("-----------------------------");
        });

        saveManager.sauvegarderJsonDansArborescence(save.getBasePath()); */

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
