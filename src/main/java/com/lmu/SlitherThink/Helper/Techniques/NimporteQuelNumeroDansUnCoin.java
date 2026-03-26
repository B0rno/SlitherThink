package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: N’importe quel numéro dans un coin
 * Vérifie s'il y a un chiffre 1, 2 ou 3 dans un des 4 coins de la grille.
 * L'aide est applicable s'il manque des traits ou croix sur les bords du coin.
 */
public class NimporteQuelNumeroDansUnCoin implements StrategieAide {

    private static final String NOM = "N’importe quel numéro dans un coin";

    /**
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    @Override
    public boolean estApplicable(Matrice m) {
        int h = m.getHauteur();
        int w = m.getLargeur();

        // Coin Haut-Gauche
        Case c1 = m.getCase(0, 0);
        if (c1 != null) {
            int num = c1.getNumero();
            if (num == 3) {
                if (c1.getTrait(0).getEtat() == ValeurTrait.VIDE || c1.getTrait(1).getEtat() == ValeurTrait.VIDE) return true;
            }
            else if(num == 1){
                if (c1.getTrait(0).getEtat() == ValeurTrait.PLEIN || c1.getTrait(1).getEtat() == ValeurTrait.PLEIN) return true;
            }
            else if (num == 2) {

                Case cDroite = m.getCase(0, 1);
                Case cBas = m.getCase(1, 0);

                if ((cDroite != null && cDroite.getTrait(0).getEtat() == ValeurTrait.VIDE) || (cBas != null && cBas.getTrait(1).getEtat() == ValeurTrait.VIDE))
                    return true;
            }
        }

        // Coin Haut-Droite
        Case c2 = m.getCase(0, w - 1);
        if (c2 != null) {
            int num = c2.getNumero();
            if (num == 3) {
                if (c2.getTrait(0).getEtat() == ValeurTrait.VIDE || c2.getTrait(2).getEtat() == ValeurTrait.VIDE) return true;
            } 
            else if(num == 1){
                if (c2.getTrait(0).getEtat() == ValeurTrait.PLEIN || c2.getTrait(2).getEtat() == ValeurTrait.PLEIN) return true;
            }
            else if (num == 2) {
                Case cGauche = m.getCase(0, w - 2);
                Case cBas = m.getCase(1, w - 1);

                if ((cGauche != null && cGauche.getTrait(0).getEtat() == ValeurTrait.VIDE) || (cBas != null && cBas.getTrait(2).getEtat() == ValeurTrait.VIDE)) 
                    return true;
            }
        }

        // Coin Bas-Gauche
        Case c3 = m.getCase(h - 1, 0);
        if (c3 != null) {
            int num = c3.getNumero();
            if (num == 3) {
                if (c3.getTrait(3).getEtat() == ValeurTrait.VIDE || c3.getTrait(1).getEtat() == ValeurTrait.VIDE) return true;
            }
            else if(num == 1){
                if (c3.getTrait(3).getEtat() == ValeurTrait.PLEIN || c3.getTrait(1).getEtat() == ValeurTrait.PLEIN) return true;
            }
            else if (num == 2) {
                Case cDroite = m.getCase(h - 1, 1);
                Case cHaut = m.getCase(h - 2, 0);

                if ((cDroite != null && cDroite.getTrait(3).getEtat() == ValeurTrait.VIDE) || (cHaut != null && cHaut.getTrait(1).getEtat() == ValeurTrait.VIDE))
                    return true;
            }
        }

        // Coin Bas-Droite
        Case c4 = m.getCase(h - 1, w - 1);
        if (c4 != null) {
            int num = c4.getNumero();
            if (num == 1 || num == 3) {
                if (c4.getTrait(3).getEtat() == ValeurTrait.VIDE || c4.getTrait(2).getEtat() == ValeurTrait.VIDE) return true;
            }
            else if(num == 1){
                if (c4.getTrait(3).getEtat() == ValeurTrait.PLEIN || c4.getTrait(2).getEtat() == ValeurTrait.PLEIN) return true;
            }
            else if (num == 2) {
                Case cGauche = m.getCase(h - 1, w - 2);
                Case cHaut = m.getCase(h - 2, w - 1);

                if ((cGauche != null && cGauche.getTrait(3).getEtat() == ValeurTrait.VIDE) || (cHaut != null && cHaut.getTrait(2).getEtat() == ValeurTrait.VIDE)) 
                    return true;
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