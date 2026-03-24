package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Adjacents 0 et 3
 * Parcourt la matrice pour vérifier s'il y a un '3' adjacent à un '0'.
 * L'aide est applicable s'il manque encore des traits sur les trois autres côtés de la case '3' qui ne touchent pas le '0'.
 */
public class Adjacents0Et3 implements StrategieAide {

    private static final String NOM = "Adjacents 0 et 3";

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
                    
                    Case c1 = m.getCase(i + 1, j); // Bas
                    Case c2 = m.getCase(i - 1, j); // Haut
                    Case c3 = m.getCase(i, j + 1); // Droite
                    Case c4 = m.getCase(i, j - 1); // Gauche

                    if (c1 != null && c1.getNumero() == 0) {
                        if (c.getTrait(0).getEtat() == ValeurTrait.VIDE || c.getTrait(1).getEtat() == ValeurTrait.VIDE || c.getTrait(2).getEtat() == ValeurTrait.VIDE) 
                            return true;
                    }
                    if (c2 != null && c2.getNumero() == 0) {
                        if (c.getTrait(3).getEtat() == ValeurTrait.VIDE || c.getTrait(1).getEtat() == ValeurTrait.VIDE || c.getTrait(2).getEtat() == ValeurTrait.VIDE) 
                            return true;
                    }
                    if (c3 != null && c3.getNumero() == 0) {
                        if (c.getTrait(0).getEtat() == ValeurTrait.VIDE || c.getTrait(1).getEtat() == ValeurTrait.VIDE || c.getTrait(3).getEtat() == ValeurTrait.VIDE) 
                            return true;
                    }
                    if (c4 != null && c4.getNumero() == 0) {
                        if (c.getTrait(0).getEtat() == ValeurTrait.VIDE || c.getTrait(2).getEtat() == ValeurTrait.VIDE || c.getTrait(3).getEtat() == ValeurTrait.VIDE) 
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