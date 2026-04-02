package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Symétrie des 2 adjacents
 * Parcourt la matrice pour trouver deux cases '2' côte à côte.
 * Les côtés extérieurs parallèles de deux '2' adjacents sont obligatoirement identiques.
 */
public class Symetrie2Adjacents implements StrategieAide {

    private static final String NOM = "La Symétrie des 2 Adjacents";

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
                            Trait traitHautCurr = currentCase.getTrait(0);   // Côté haut du 1er '2'
                            Trait traitHautExt = caseDroite.getTrait(0);    // Côté haut du 2eme '2'
                            Trait traitGaucheCurr = currentCase.getTrait(2); // Côté gauche du 1er '2'
                            Trait traitDroiteExt = caseDroite.getTrait(1);  // Côté droit du 2ème '2'
                            Trait traitBasExt = caseDroite.getTrait(3);
                            Trait traitBasCurr = currentCase.getTrait(3);

                            // Si on connaît l'état du côté gauche, le côté droit doit être identique et inversement
                            if (traitGaucheCurr.getEtat() != ValeurTrait.VIDE && traitDroiteExt.getEtat() != ValeurTrait.PLEIN ||
                                traitDroiteExt.getEtat() != ValeurTrait.VIDE && traitGaucheCurr.getEtat() != ValeurTrait.PLEIN) {
                                return true;
                            } //Si 
                            if (traitHautExt.getEtat() != ValeurTrait.VIDE && traitHautCurr.getEtat() != ValeurTrait.PLEIN ||
                                traitHautCurr.getEtat() != ValeurTrait.VIDE && traitHautExt.getEtat() != ValeurTrait.PLEIN) {
                                return true;
                            }
                            if (traitBasExt.getEtat() != ValeurTrait.VIDE && traitBasCurr.getEtat() != ValeurTrait.PLEIN ||
                                traitBasCurr.getEtat() != ValeurTrait.VIDE && traitBasExt.getEtat() != ValeurTrait.PLEIN) {
                                return true;
                            }
                        }
                    }

                    // Vérification avec un '2' adjacent Vertical (en bas)
                    if (r < m.getHauteur() - 1) {
                        Case caseBas = m.getCase(r + 1, c);
                        
                        if (caseBas != null && caseBas.getNumero() == 2) {
                            Trait traitHautCurr = currentCase.getTrait(0); // Côté haut du 1er '2'
                            Trait traitDroiteCurr = currentCase.getTrait(2); // Côté droit du 1er '2'
                            Trait traitGaucheCurr = currentCase.getTrait(1); // Côté gauche du 1er '2'
                            Trait traitBasExt = caseBas.getTrait(3);      // Côté bas du 2ème '2'
                            Trait traitGaucheExt = caseBas.getTrait(1); // Côté gauche du 2ème '2'
                            Trait traitDroiteExt = caseBas.getTrait(2); // Côté droit du 2ème '2'


                            // Si on connaît l'état du côté haut, le côté bas doit être identique et inversement
                            if (traitHautCurr.getEtat() != ValeurTrait.VIDE && traitBasExt.getEtat() != ValeurTrait.PLEIN ||
                                traitBasExt.getEtat() != ValeurTrait.VIDE && traitHautCurr.getEtat() != ValeurTrait.PLEIN) {
                                return true;
                            } // Si on connaît l'état du côté droit, le côté gauche doit être identique et inversement
                            else if (traitDroiteCurr.getEtat() != ValeurTrait.VIDE && traitDroiteExt.getEtat() != ValeurTrait.PLEIN ||
                                     traitDroiteExt.getEtat() != ValeurTrait.VIDE && traitDroiteCurr.getEtat() != ValeurTrait.PLEIN) {
                                return true;
                            } // Si on connaît l'état du côté gauche, le côté droit doit être identique et inversement
                            else if (traitGaucheCurr.getEtat() != ValeurTrait.VIDE && traitGaucheExt.getEtat() != ValeurTrait.PLEIN ||
                                     traitGaucheExt.getEtat() != ValeurTrait.VIDE && traitGaucheCurr.getEtat() != ValeurTrait.PLEIN) {
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