package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Technique avancée 2 (Coin extérieur bloqué d'un 3)
 * Si les deux chemins situés à l'extérieur d'un coin d'une case '3' sont bloqués (croix ou bord du plateau), la boucle ne peut pas s'échapper par ce coin.
 * Elle doit donc obligatoirement longer les deux traits de la case formant ce coin.
 */
public class CoinsBloquesSurUn3 implements StrategieAide {

    private static final String NOM = "Coins bloqués sur un 3";

    @Override
    public boolean estApplicable(Matrice m) {

        for (int r = 0; r < m.getHauteur(); r++) {
            for (int c = 0; c < m.getLargeur(); c++) {

                Case currentCase = m.getCase(r, c);
                
                if (currentCase != null && currentCase.getNumero() == 3) {
                    
                    // Vérification du coin Haut-Gauche 
                    if (estBloque(getTraitHaut(m, r, c)) && estBloque(getTraitGauche(m, r, c))) {
                        // Les traits Haut (0) et Gauche (1) de la case doivent être PLEINS
                        if (currentCase.getTrait(0).getEtat() != ValeurTrait.PLEIN || 
                            currentCase.getTrait(1).getEtat() != ValeurTrait.PLEIN) {
                                return true;
                        }
                    }
                    
                    // Vérification du coin Haut-Droite
                    if (estBloque(getTraitHaut(m, r, c + 1)) && estBloque(getTraitDroite(m, r, c + 1))) {
                        // Les traits Haut (0) et Droite (2) de la case doivent être PLEINS
                        if (currentCase.getTrait(0).getEtat() != ValeurTrait.PLEIN || 
                            currentCase.getTrait(2).getEtat() != ValeurTrait.PLEIN) {
                                return true;
                        }
                    }

                    // Vérification du coin Bas-Gauche 
                    if (estBloque(getTraitBas(m, r + 1, c)) && estBloque(getTraitGauche(m, r + 1, c))) {
                        // Les traits Bas (3) et Gauche (1) de la case doivent être PLEINS
                        if (currentCase.getTrait(3).getEtat() != ValeurTrait.PLEIN || 
                            currentCase.getTrait(1).getEtat() != ValeurTrait.PLEIN) {
                                return true;
                        }
                    }

                    // Vérification du coin Bas-Droite
                    if (estBloque(getTraitBas(m, r + 1, c + 1)) && estBloque(getTraitDroite(m, r + 1, c + 1))) {
                        // Les traits Bas (3) et Droite (2) de la case doivent être PLEINS
                        if (currentCase.getTrait(3).getEtat() != ValeurTrait.PLEIN || 
                            currentCase.getTrait(2).getEtat() != ValeurTrait.PLEIN) {
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Un chemin est considéré comme bloqué s'il est une croix ou s'il n'existe pas
     */
    private boolean estBloque(Trait t) {
        return t == null || t.getEtat() == ValeurTrait.CROIX;
    }

    // --- Méthodes pour extraire les traits autour d'une intersection ---

    private Trait getTraitHaut(Matrice m, int r, int c) {
        if (r == 0) 
            return null; // Bordure haute
        if (c < m.getLargeur()) 
            return m.getCase(r - 1, c).getTrait(1); // Trait gauche de la case Haut-Droite
        return m.getCase(r - 1, c - 1).getTrait(2); // Trait droit de la case Haut-Gauche
    }

    private Trait getTraitBas(Matrice m, int r, int c) {
        if (r == m.getHauteur()) 
            return null; // Bordure basse
        if (c < m.getLargeur()) 
            return m.getCase(r, c).getTrait(1); // Trait gauche de la case Bas-Droite
        return m.getCase(r, c - 1).getTrait(2); // Trait droit de la case Bas-Gauche
    }

    private Trait getTraitGauche(Matrice m, int r, int c) {
        if (c == 0) return null; // Bordure gauche
        if (r < m.getHauteur()) 
            return m.getCase(r, c - 1).getTrait(0); // Trait haut de la case Bas-Gauche
        return m.getCase(r - 1, c - 1).getTrait(3); // Trait bas de la case Haut-Gauche
    }

    private Trait getTraitDroite(Matrice m, int r, int c) {
        if (c == m.getLargeur()) return null; // Bordure droite
        if (r < m.getHauteur())
             return m.getCase(r, c).getTrait(0); // Trait haut de la case Bas-Droite
        return m.getCase(r - 1, c).getTrait(3); // Trait bas de la case Haut-Droite
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