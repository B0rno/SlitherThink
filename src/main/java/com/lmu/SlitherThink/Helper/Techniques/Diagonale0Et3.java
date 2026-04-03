package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Diagonale 0 et 3
 * Parcourt la matrice pour vérifier s'il y a un '3' en diagonale d'un '0'.
 * L'aide est applicable s'il manque encore des traits sur les côtés opposés au '0'.
 */
public class Diagonale0Et3 implements StrategieAide {

    private static final String NOM = "Diagonale 0 et 3";

    private static final int HAUT = 0;
    private static final int DROITE = 1;
    private static final int BAS = 2;
    private static final int GAUCHE = 3;

    /**
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    @Override
    public boolean estApplicable(Matrice m) {
        for (int i = 0; i < m.getHauteur(); i++) {
            for (int j = 0; j < m.getLargeur(); j++) {
                
                Case c = m.getCase(i, j);

                if (c != null && c.getNumero() == 3) {
                    Case cDiagHG = m.getCase(i - 1, j - 1);
                    Case cDiagHD = m.getCase(i - 1, j + 1);
                    Case cDiagBG = m.getCase(i + 1, j - 1);
                    Case cDiagBD = m.getCase(i + 1, j + 1);

                    if (cDiagHG != null && cDiagHG.getNumero() == 0) {
                        if (c.getTrait(HAUT).getEtat() != ValeurTrait.PLEIN || c.getTrait(GAUCHE).getEtat() != ValeurTrait.PLEIN) 
                            return true;
                    }
                    if (cDiagHD != null && cDiagHD.getNumero() == 0) {
                        if (c.getTrait(HAUT).getEtat() != ValeurTrait.PLEIN || c.getTrait(DROITE).getEtat() != ValeurTrait.PLEIN) 
                            return true;
                    }
                    if (cDiagBG != null && cDiagBG.getNumero() == 0) {
                        if (c.getTrait(BAS).getEtat() != ValeurTrait.PLEIN || c.getTrait(GAUCHE).getEtat() != ValeurTrait.PLEIN) 
                            return true;
                    }
                    if (cDiagBD != null && cDiagBD.getNumero() == 0) {
                        if (c.getTrait(BAS).getEtat() != ValeurTrait.PLEIN || c.getTrait(DROITE).getEtat() != ValeurTrait.PLEIN) 
                            return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Aide trouverAide(Matrice m) {
        if (estApplicable(m)) {
            String technique = LoadTechniqueJson.getInstance().getDescription(NOM);
            return new Aide(NOM, technique);
        }
        return null;
    }

    @Override
    public String getNom() {
        return NOM;
    }
}