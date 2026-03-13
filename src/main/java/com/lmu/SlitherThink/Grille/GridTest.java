package com.lmu.SlitherThink.Grille;

import com.lmu.SlitherThink.Grille.Matrice;

public class GridTest {
    public static void main(String[] argv){
        Matrice mat = Matrice.loadGrille("grilleJeu1");
        System.out.println(mat);
    }
}
