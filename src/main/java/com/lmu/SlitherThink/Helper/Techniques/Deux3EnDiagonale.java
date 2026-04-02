package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Deux 3 en diagonale
 * Parcourt la matrice pour vérifier s'il y a deux '3' en diagonale.
 * L'aide est applicable s'il manque encore des traits sur les bords extérieurs de ces deux '3'.
 */
public class Deux3EnDiagonale implements StrategieAide {

    private static final String NOM = "Deux 3 en diagonale";

    /**
     * Vérifie si l'aide "Deux 3 en diagonale" est applicable.
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    @Override
    public boolean estApplicable(Matrice m) {

        for (int i = 0; i < m.getHauteur(); i++) {
            for (int j = 0; j < m.getLargeur(); j++) {

                Case c = m.getCase(i, j);

                if (c != null && c.getNumero() == 3) {

                    // La diagonale est orientée Haut-Gauche vers Bas-Droite
                    Case cDiagBD = m.getCase(i + 1, j + 1);

                    if (cDiagBD != null && cDiagBD.getNumero() == 3) {

                        // Pour la case (i,j), les traits Haut (0) et Gauche (1) doivent être pleins
                        // Pour la case en Bas-Droite, les traits Bas (3) et Droite (2) doivent être pleins
                        if (c.getTrait(0).getEtat() != ValeurTrait.PLEIN || 
                            c.getTrait(1).getEtat() != ValeurTrait.PLEIN ||
                            cDiagBD.getTrait(3).getEtat() != ValeurTrait.PLEIN ||
                            cDiagBD.getTrait(2).getEtat() != ValeurTrait.PLEIN) {
                                return true;
                        }
                    }

                    //  La diagonale est orientée Haut-Droite vers Bas-Gauche
                    Case cDiagBG = m.getCase(i + 1, j - 1);

                    if (cDiagBG != null && cDiagBG.getNumero() == 3) {

                        // Pour la case (i,j), les traits Haut (0) et Droite (2) doivent être pleins
                        // Pour la case en Bas-Gauche, les traits Bas (3) et Gauche (1) doivent être pleins
                        if (c.getTrait(0).getEtat() != ValeurTrait.PLEIN || 
                            c.getTrait(2).getEtat() != ValeurTrait.PLEIN ||
                            cDiagBG.getTrait(3).getEtat() != ValeurTrait.PLEIN ||
                            cDiagBG.getTrait(1).getEtat() != ValeurTrait.PLEIN) {
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