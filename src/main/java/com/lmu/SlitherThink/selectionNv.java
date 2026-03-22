package com.lmu.SlitherThink;

import java.util.List;

import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.structure.SaveGrille;

public class selectionNv {
    public static SaveGrille selectionNvDiff(int difficulte) {
        LoadSave loadSave = LoadSave.getInstance("");
        List<SaveGrille> grilles = loadSave.getGrillesNv(difficulte) ;
        if (grilles == null || grilles.isEmpty()) {
            return null;
        }
        return grilles.get((int) (Math.random() * grilles.size()));
    }

    public static void main(String[] args) {
        int difficulte = 1; // Exemple de difficulté
        SaveGrille grille = selectionNvDiff(difficulte);
        if (grille != null) {
            System.out.println("Grille sélectionnée pour la difficulté " + difficulte + ": " + grille);
        } else {
            System.out.println("Aucune grille trouvée pour la difficulté " + difficulte); 
        }
    }
}
