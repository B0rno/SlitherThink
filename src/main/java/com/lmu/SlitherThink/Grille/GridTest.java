package com.lmu.SlitherThink.Grille;

import com.lmu.SlitherThink.Grille.Matrice;

public class GridTest {
    public static void main(String[] argv){
        Matrice mat = Matrice.loadGrille("grilleJeu1");
        mat.loadSave(150, "./save/saveGrille/grilleJeu1.json");
        mat.cliquer(2,2,2);
        //mat.cliquer(2,2,2);
        mat.saveGrille(150, "./save/saveGrille/grilleJeu1.json", 2, 2, 2);
        System.out.println(mat);
    }
}
