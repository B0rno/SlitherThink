package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique: Contraintes sur 3
 * Détecte les situations où un 3 est adjacent à un 0 et possède déjà un trait plein
 * du côté du 0. Cette configuration permet de déduire les autres traits autour du 3.
 * Pattern de base de SlitherLink niveau basique.
 *
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */
public class ContraintesSur3 implements StrategieAide {

    private static final String NOM = "Contraintes sur 3";

    /**
     * Vérifie si cette technique peut être appliquée sur la matrice.
     * Cherche un 0 adjacent à un 3 qui possède déjà un trait plein du côté du 0   .
     *
     * @param m la matrice du jeu à analyser
     * @return true si un cas applicable est trouvé, false sinon
     */
    @Override
    public boolean estApplicable(Matrice m) {
        for (int i = 0; i < m.getHauteur(); i++) {
            for (int j = 0; j < m.getLargeur(); j++) {
                Case c = m.getCase(i, j);
                if(c != null && c.getNumero() == 3) {
                    //Test Nord
                    if(m.getCase(i-1, j) != null && m.getCase(i-1, j).getNumero() == 0 && m.getCase(i-1, j).getTrait(0).getEtat() == ValeurTrait.PLEIN) {
                        return true;
                    }
                    //Test Sud
                    if(m.getCase(i+1, j) != null && m.getCase(i+1, j).getNumero() == 0 && m.getCase(i+1, j).getTrait(3).getEtat() == ValeurTrait.PLEIN) {
                        return true;
                    }
                    //Test Est
                    if(m.getCase(i, j+1) != null && m.getCase(i, j+1).getNumero() == 0 && m.getCase(i, j+1).getTrait(2).getEtat() == ValeurTrait.PLEIN) {
                        return true;
                    }
                    //Test Ouest
                    if(m.getCase(i, j-1) != null && m.getCase(i, j-1).getNumero() == 0 && m.getCase(i, j-1).getTrait(1).getEtat() == ValeurTrait.PLEIN) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Trouve et retourne une aide basée sur cette technique.
     * Charge la description complète depuis le JSON et crée un objet Aide.
     *
     * @param m la matrice du jeu à analyser
     * @return une aide avec le nom et la description de la technique, ou null si non applicable
     */
    @Override
    public Aide trouverAide(Matrice m) {
        if (estApplicable(m)) {
            String technique = LoadTechniqueJson.getInstance().getDescription(NOM);
            return new Aide(NOM, technique);
        }
        return null;
    }

    /**
     * Retourne le nom de cette technique.
     *
     * @return le nom "Contraintes sur 3"
     */
    @Override
    public String getNom() {
        return NOM;
    }
}
