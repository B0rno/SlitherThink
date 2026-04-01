package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Technique avancée 4 (Symétrie des 2 adjacents)
 * Parcourt la matrice pour trouver deux cases '2' côte à côte.
 * Les côtés extérieurs parallèles de deux '2' adjacents sont obligatoirement identiques.
 */
public class TechniqueAvancee4 implements StrategieAide {

    private static final String NOM = "Technique avancée 4";

    @Override
    public boolean estApplicable(Matrice m) {

        for (int r = 0; r < m.getHauteur(); r++) {
            for (int c = 0; c < m.getLargeur(); c++) {

                Case currentCase = m.getCase(r, c);

                if (currentCase != null && currentCase.getNumero() == 2) {

                    // Vérification avec un '2' adjacent Horizontal (à droite)
                    if (c < m.getLargeur() - 1) {
                        Case caseDroite = m.getCase(r, c + 1);
                        
                        if (caseDroite != null && caseDroite.getNumero() == 2) {
                            Trait traitGaucheExt = currentCase.getTrait(1); // Côté gauche du 1er '2'
                            Trait traitDroiteExt = caseDroite.getTrait(2);  // Côté droit du 2ème '2'

                            // Si on connaît l'état du côté gauche, le côté droit doit être identique
                            if (traitGaucheExt.getEtat() != ValeurTrait.VIDE && traitDroiteExt.getEtat() == ValeurTrait.VIDE) {
                                return true;
                            }
                            // Inversement, si on connaît le côté droit, le gauche doit être identique
                            if (traitDroiteExt.getEtat() != ValeurTrait.VIDE && traitGaucheExt.getEtat() == ValeurTrait.VIDE) {
                                return true;
                            }
                        }
                    }

                    // Vérification avec un '2' adjacent Vertical (en bas)
                    if (r < m.getHauteur() - 1) {
                        Case caseBas = m.getCase(r + 1, c);
                        
                        if (caseBas != null && caseBas.getNumero() == 2) {
                            Trait traitHautExt = currentCase.getTrait(0); // Côté haut du 1er '2'
                            Trait traitBasExt = caseBas.getTrait(3);      // Côté bas du 2ème '2'

                            // Si on connaît l'état du côté haut, le côté bas doit être identique
                            if (traitHautExt.getEtat() != ValeurTrait.VIDE && traitBasExt.getEtat() == ValeurTrait.VIDE) {
                                return true;
                            }
                            // Inversement, si on connaît le côté bas, le haut doit être identique
                            if (traitBasExt.getEtat() != ValeurTrait.VIDE && traitHautExt.getEtat() == ValeurTrait.VIDE) {
                                return true;
                            }
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