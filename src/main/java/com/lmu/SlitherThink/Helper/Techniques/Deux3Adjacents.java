package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Deux 3 adjacents
 * Parcourt la matrice pour vérifier s'il y a deux '3' adjacents.
 * L'aide est applicable s'il manque encore des traits parallèles sur et à l'opposé de leur côté partagé.
 */
public class Deux3Adjacents implements StrategieAide {

    private static final String NOM = "Deux 3 adjacents";

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
                    Case cHaut = m.getCase(i - 1, j);
                    Case cBas = m.getCase(i + 1, j);
                    Case cGauche = m.getCase(i, j - 1);
                    Case cDroite = m.getCase(i, j + 1);

                    if ((cGauche != null && cGauche.getNumero() == 3) || (cDroite != null && cDroite.getNumero() == 3)) {
                        if (c.getTrait(1).getEtat() == ValeurTrait.VIDE || c.getTrait(2).getEtat() == ValeurTrait.VIDE) 
                            return true;
                    }
                    if ((cHaut != null && cHaut.getNumero() == 3) || (cBas != null && cBas.getNumero() == 3)) {
                        if (c.getTrait(0).getEtat() == ValeurTrait.VIDE || c.getTrait(3).getEtat() == ValeurTrait.VIDE) 
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