package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Technique avancée 6 (La ligne entrant dans un 1)
 * Si une ligne de la boucle (trait PLEIN) pointe directement sur l'un des coins d'une case '1' elle va obligatoirement longer l'un des deux côtés de ce coin.
 * Donc les deux autres côtés de la case '1' à l'opposé du coin doivent  être marqués d'une CROIX.
 */
public class TechniqueAvancee6 implements StrategieAide {

    private static final String NOM = "Technique avancée 6";

    @Override
    public boolean estApplicable(Matrice m) {

        for (int r = 0; r < m.getHauteur(); r++) {
            for (int c = 0; c < m.getLargeur(); c++) {

                Case currentCase = m.getCase(r, c);

                if (currentCase != null && currentCase.getNumero() == 1) {

                    // Coin Haut-Gauche : Si une ligne arrive par le haut ou par la gauche
                    if (estPlein(getExtHautGauche_Haut(m, r, c)) || estPlein(getExtHautGauche_Gauche(m, r, c))) {
                        // Les traits opposés (Droite et Bas) doivent être des CROIX
                        if (currentCase.getTrait(2).getEtat() == ValeurTrait.VIDE || 
                            currentCase.getTrait(3).getEtat() == ValeurTrait.VIDE) 
                                return true;
                    }

                    // Coin Haut-Droite : Si une ligne arrive par le haut ou par la droite
                    if (estPlein(getExtHautDroite_Haut(m, r, c)) || estPlein(getExtHautDroite_Droite(m, r, c))) {
                        // Les traits opposés (Gauche et Bas) doivent être des CROIX
                        if (currentCase.getTrait(1).getEtat() == ValeurTrait.VIDE || 
                            currentCase.getTrait(3).getEtat() == ValeurTrait.VIDE) 
                                return true;
                    }

                    // Coin Bas-Gauche : Si une ligne arrive par le bas ou par la gauche
                    if (estPlein(getExtBasGauche_Bas(m, r, c)) || estPlein(getExtBasGauche_Gauche(m, r, c))) {
                        // Les traits opposés (Haut et Droite) doivent être des CROIX
                        if (currentCase.getTrait(0).getEtat() == ValeurTrait.VIDE || 
                            currentCase.getTrait(2).getEtat() == ValeurTrait.VIDE) 
                                return true;
                    }

                    // Coin Bas-Droite : Si une ligne arrive par le bas ou par la droite
                    if (estPlein(getExtBasDroite_Bas(m, r, c)) || estPlein(getExtBasDroite_Droite(m, r, c))) {
                        // Les traits opposés (Haut et Gauche) doivent être des CROIX
                        if (currentCase.getTrait(0).getEtat() == ValeurTrait.VIDE || 
                            currentCase.getTrait(1).getEtat() == ValeurTrait.VIDE) 
                                return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean estPlein(Trait t) {
        return t != null && t.getEtat() == ValeurTrait.PLEIN;
    }

    // --- Méthodes pour récupérer les traits exterieurs pointant sur les coins de la case ---

    // Coin Haut-Gauche
    private Trait getExtHautGauche_Haut(Matrice m, int r, int c) {
        if (r == 0) return null;
        return m.getCase(r - 1, c).getTrait(1); // Trait gauche de la case au-dessus
    }
    private Trait getExtHautGauche_Gauche(Matrice m, int r, int c) {
        if (c == 0) return null;
        return m.getCase(r, c - 1).getTrait(0); // Trait haut de la case à gauche
    }

    // Coin Haut-Droite
    private Trait getExtHautDroite_Haut(Matrice m, int r, int c) {
        if (r == 0) return null;
        return m.getCase(r - 1, c).getTrait(2); // Trait droit de la case au-dessus
    }
    private Trait getExtHautDroite_Droite(Matrice m, int r, int c) {
        if (c == m.getLargeur() - 1) return null;
        return m.getCase(r, c + 1).getTrait(0); // Trait haut de la case à droite
    }

    // Coin Bas-Gauche
    private Trait getExtBasGauche_Bas(Matrice m, int r, int c) {
        if (r == m.getHauteur() - 1) return null;
        return m.getCase(r + 1, c).getTrait(1); // Trait gauche de la case en dessous
    }
    private Trait getExtBasGauche_Gauche(Matrice m, int r, int c) {
        if (c == 0) return null;
        return m.getCase(r, c - 1).getTrait(3); // Trait bas de la case à gauche
    }

    // Coin Bas-Droite
    private Trait getExtBasDroite_Bas(Matrice m, int r, int c) {
        if (r == m.getHauteur() - 1) return null;
        return m.getCase(r + 1, c).getTrait(2); // Trait droit de la case en dessous
    }
    private Trait getExtBasDroite_Droite(Matrice m, int r, int c) {
        if (c == m.getLargeur() - 1) return null;
        return m.getCase(r, c + 1).getTrait(3); // Trait bas de la case à droite
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