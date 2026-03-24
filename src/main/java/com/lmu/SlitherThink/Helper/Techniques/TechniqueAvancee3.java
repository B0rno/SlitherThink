package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Technique avancée 3 
 * Parcourt la matrice pour vérifier si une case possède exactement 3 traits pleins.
 * Si c'est le cas, le 4ème trait ne peut pas être plein (cela formerait une boucle minuscule et isolée), il doit donc obligatoirement être marqué d'une croix.
 */
public class TechniqueAvancee3 implements StrategieAide {

    private static final String NOM = "Technique avancée 3";

    @Override
    public boolean estApplicable(Matrice m) {

        for (int r = 0; r < m.getHauteur(); r++) {
            for (int c = 0; c < m.getLargeur(); c++) {

                Case currentCase = m.getCase(r, c);

                if (currentCase != null) {
                    
                    int nbPleins = 0;
                    int nbVides = 0;

                    // On analyse les 4 côtés de la case
                    for (int i = 0; i < 4; i++) {
                        Trait t = currentCase.getTrait(i);
                        if (t != null) {
                            if (t.getEtat() == ValeurTrait.PLEIN) {
                                nbPleins++;
                            } else if (t.getEtat() == ValeurTrait.VIDE) {
                                nbVides++;
                            }
                        }
                    }

                    // Si la case a déjà 3 traits pleins et un trait vide reste, on s'apprête à fermer une boucle de 1x1. 
                    // Pour éviter ça, le dernier trait vide devient une croix.
                    if (nbPleins == 3 && nbVides == 1) {
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