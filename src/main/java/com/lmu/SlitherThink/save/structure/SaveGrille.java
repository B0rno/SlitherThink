package com.lmu.SlitherThink.save.structure;

import java.util.List;

public class SaveGrille {
    private int nv_grille;
    private int taille;
    private List<positionGrille> numero_cases;
    private List<PositionTrait> liste_position_trait;

    public int getNvGrille() {
        return nv_grille;
    }

    public int getTailleGrille() {
        return taille;
    }

    public List<positionGrille> getNumeroCases() {
        return numero_cases;
    }

    public List<PositionTrait> getListePositionTrait() {
        return liste_position_trait;
    }
}
