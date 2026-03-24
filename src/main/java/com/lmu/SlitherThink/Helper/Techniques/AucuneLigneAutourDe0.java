package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Aucune ligne autour de 0
 * Parcourt la matrice pour vérifier s'il y a des cases '0'.
 * L'aide est applicable s'il manque encore des croix sur les traits autour du '0'.
 */
public class AucuneLigneAutourDe0 implements StrategieAide {

    private static final String NOM = "Aucune ligne autour de 0";

    /**
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    @Override
    public boolean estApplicable(Matrice m) {
        
        for (int i = 0; i < m.getHauteur(); i++) {
            for (int j = 0; j < m.getLargeur(); j++) {

                Case c = m.getCase(i, j);

                if (c != null && c.getNumero() == 0) {
                    for (int x = 0; x < 4; x++) {

                        Trait t = c.getTrait(x);

                        if (t != null && t.getEtat() == ValeurTrait.VIDE) {
                            return true;
                        }
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