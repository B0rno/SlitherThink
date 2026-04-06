package com.lmu.SlitherThink;

import java.util.List;

import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.structure.SaveGrille;

/**
 * Classe responsable de la sélection aléatoire d'un niveau en fonction de sa difficulté.
 * Elle pioche dans la liste des grilles chargées pour proposer un défi au joueur.
 * @author Ilann
 */
public class selectionNv {
    
    /**
     * Sélectionne aléatoirement une grille correspondant à la difficulté demandée.
     * @param difficulte L'entier représentant le niveau de difficulté.
     * @return Une instance de SaveGrille choisie au hasard, ou null si aucune grille n'est disponible.
     */
    public static SaveGrille selectionNvDiff(int difficulte) {
        LoadSave loadSave = LoadSave.getInstance("");
        List<SaveGrille> grilles = loadSave.getGrillesNv(difficulte) ;
        if (grilles == null || grilles.isEmpty()) {
            return null;
        }
        return grilles.get((int) (Math.random() * grilles.size()));
    }

    /**
     * Point d'entrée pour tester la sélection de niveau en console.
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        int difficulte = 1; // Exemple de difficulté
        SaveGrille grille = selectionNvDiff(difficulte);
    }
}