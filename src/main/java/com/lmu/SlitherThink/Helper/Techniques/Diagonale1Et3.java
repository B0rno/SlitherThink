package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Technique avancée 5 (Le 1 et 3 en diagonale)
 * Parcourt la matrice pour trouver un '3' et un '1' placés en diagonale.
 * On force les deux bords extérieurs du '3' à être PLEINS et les deux bords extérieurs du '1' à être marqués d'une CROIX.
 */
public class Diagonale1Et3 implements StrategieAide {

    private static final String NOM = "Technique avancée 5";

    @Override
    public boolean estApplicable(Matrice m) {

        for (int r = 0; r < m.getHauteur(); r++) {
            for (int c = 0; c < m.getLargeur(); c++) {

                Case currentCase = m.getCase(r, c);

                // On cherche une case '3'
                if (currentCase != null && currentCase.getNumero() == 3) {

                    // Diagonale Haut-Gauche (Le 1 est en Haut-Gauche du 3)
                    Case caseHG = m.getCase(r - 1, c - 1);
                    if (caseHG != null && caseHG.getNumero() == 1) {

                        // Extérieurs du 3 (Bas et Droite) -> Doivent être PLEINS
                        if (currentCase.getTrait(3).getEtat() != ValeurTrait.PLEIN || 
                            currentCase.getTrait(2).getEtat() != ValeurTrait.PLEIN) 
                            return true;

                        // Extérieurs du 1 (Haut et Gauche) -> Doivent être des CROIX
                        if (caseHG.getTrait(0).getEtat() != ValeurTrait.PLEIN || 
                            caseHG.getTrait(1).getEtat() != ValeurTrait.PLEIN) 
                            return true;
                    }

                    // Diagonale Haut-Droite (Le 1 est en Haut-Droite du 3)
                    Case caseHD = m.getCase(r - 1, c + 1);
                    if (caseHD != null && caseHD.getNumero() == 1) {

                        // Extérieurs du 3 (Bas et Gauche) -> Doivent être PLEINS
                        if (currentCase.getTrait(3).getEtat() != ValeurTrait.PLEIN || 
                            currentCase.getTrait(1).getEtat() != ValeurTrait.PLEIN) 
                                return true;

                        // Extérieurs du 1 (Haut et Droite) -> Doivent être des CROIX
                        if (caseHD.getTrait(0).getEtat() != ValeurTrait.PLEIN || 
                            caseHD.getTrait(2).getEtat() != ValeurTrait.PLEIN) 
                                return true;
                    }

                    // Diagonale Bas-Gauche (Le 1 est en Bas-Gauche du 3)
                    Case caseBG = m.getCase(r + 1, c - 1);

                    if (caseBG != null && caseBG.getNumero() == 1) {

                        // Extérieurs du 3 (Haut et Droite) -> Doivent être PLEINS
                        if (currentCase.getTrait(0).getEtat() != ValeurTrait.PLEIN || 
                            currentCase.getTrait(2).getEtat() != ValeurTrait.PLEIN) 
                                return true;

                        // Extérieurs du 1 (Bas et Gauche) -> Doivent être des CROIX
                        if (caseBG.getTrait(3).getEtat() != ValeurTrait.PLEIN || 
                            caseBG.getTrait(1).getEtat() != ValeurTrait.PLEIN) 
                                return true;
                    }

                    // Diagonale Bas-Droite (Le 1 est en Bas-Droite du 3)
                    Case caseBD = m.getCase(r + 1, c + 1);
                    if (caseBD != null && caseBD.getNumero() == 1) {

                        // Extérieurs du 3 (Haut et Gauche) -> Doivent être PLEINS
                        if (currentCase.getTrait(0).getEtat() != ValeurTrait.PLEIN || 
                            currentCase.getTrait(1).getEtat() != ValeurTrait.PLEIN) 
                                return true;

                        // Extérieurs du 1 (Bas et Droite) -> Doivent être des CROIX
                        if (caseBD.getTrait(3).getEtat() != ValeurTrait.PLEIN || 
                            caseBD.getTrait(2).getEtat() != ValeurTrait.PLEIN) 
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