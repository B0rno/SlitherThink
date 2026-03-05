package com.lmu.SlitherThink;

import java.util.List;

class SaveGrille {
    private int nv_grille;
    private int taille;
    private List<positionGrille> numero_cases;
    private List<positionTrait> liste_position_trait;

    public int getNvGrille() {
        return nv_grille;
    }

    public int getTailleGrille() {
        return taille;
    }

    public List<positionGrille> getNumeroCases() {
        return numero_cases;
    }

    public List<positionTrait> getListePositionTrait() {
        return liste_position_trait;
    }
}
