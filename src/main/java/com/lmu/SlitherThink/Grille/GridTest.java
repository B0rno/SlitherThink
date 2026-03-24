package com.lmu.SlitherThink.Grille;

import com.lmu.SlitherThink.Grille.Matrice;

public class GridTest {
    public static void main(String[] argv){
        Matrice mat = Matrice.loadGrille("GrilleMoyen13X13_1");
        System.out.println(mat.loadSave("150", "./save/saveGrille/GrilleMoyen13X13_1.json", false));
        mat.cliquer(2,2,2);
        //mat.cliquer(2,2,2);
        mat.saveGrille("150", "./save/saveGrille/GrilleMoyen13X13_1.json", false, 2, 2, 2);
        System.out.println(mat);
    }
}
