package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique : Boucle atteignant un 1
 * Détecte les situations où un segment de la boucle arrive perpendiculairement sur
 * l'un des sommets d'une case '1'. Cette configuration force la boucle à ne pas emprunter
 * les deux côtés du '1' qui ne touchent pas ce sommet.
 * Pattern de base de SlitherLink pour la continuité du chemin.
 *
 * @author Lucas Abeka-Doth (lucasabeka)
 * @version 1.0
 */
public class BoucleSur1 implements StrategieAide {

    private static final String NOM = "Boucle atteignant un 1";

    /**
     * Vérifie si cette technique peut être appliquée sur la matrice.
     * Cherche un trait plein arrivant sur le coin d'un 1 depuis l'extérieur,
     * ce qui permet de déduire les croix opposés de cette case.
     *
     * @param m la matrice du jeu à analyser
     * @return true si des croix peuvent être déduits, false sinon
     */
    @Override
    public boolean estApplicable(Matrice m) {
        for (int i = 0; i < m.getHauteur(); i++) {
            for (int j = 0; j < m.getLargeur(); j++) {
                Case c = m.getCase(i, j);
                // On cible uniquement les cases contenant un 1
                if (c != null && c.getNumero() == 1) {
                    if (c.getTrait(0).getEtat() == ValeurTrait.PLEIN || 
                        c.getTrait(1).getEtat() == ValeurTrait.PLEIN || 
                        c.getTrait(2).getEtat() == ValeurTrait.PLEIN || 
                        c.getTrait(3).getEtat() == ValeurTrait.PLEIN) {
                        return false; // Si un trait est déjà plein, on ne peut pas appliquer cette technique
                    }

                    // Test coin haut-gauche (Nord-Ouest)
                    // Si un trait arrive de la gauche ou du haut vers ce coin
                    if (estPlein(m, i, j - 1, true) || estPlein(m, i - 1, j, false)) {
                        // Alors les murs opposés (droite : 2 et bas : 3) ne peuvent pas être tracés
                        if (c.getTrait(2).getEtat() != ValeurTrait.PLEIN ||
                                c.getTrait(3).getEtat() != ValeurTrait.PLEIN) {
                            return true;
                        }
                    }

                    // Test haut-droite (Nord-Est)
                    // Si un trait arrive de la droite ou du haut vers ce coin
                    if (estPlein(m, i, j + 1, true) || estPlein(m, i - 1, j + 1, false)) {
                        // Alors les murs opposés (gauche : 1 et bas : 3) ne peuvent pas être tracés
                        if (c.getTrait(1).getEtat() != ValeurTrait.PLEIN ||
                                c.getTrait(3).getEtat() != ValeurTrait.PLEIN) {
                            return true;
                        }
                    }

                    // Test bas-gauche (Sud-Ouest)
                    // Si un trait arrive de la gauche ou du bas vers ce coin
                    if (estPlein(m, i + 1, j - 1, true) || estPlein(m, i + 1, j, false)) {
                        // Alors les murs opposés (haut : 0 et droite : 2) ne peuvent pas être tracés
                        if (c.getTrait(0).getEtat() != ValeurTrait.PLEIN ||
                                c.getTrait(2).getEtat() != ValeurTrait.PLEIN) {
                            return true;
                        }
                    }

                    // Test bas-droite (Sud-Est)
                    // Si un trait arrive de la droite ou du bas vers ce coin
                    if (estPlein(m, i + 1, j + 1, true) || estPlein(m, i + 1, j + 1, false)) {
                        // Alors les murs opposés (haut : 0 et gauche : 1) ne peuvent pas être tracés
                        if (c.getTrait(0).getEtat() != ValeurTrait.PLEIN ||
                                c.getTrait(1).getEtat() != ValeurTrait.PLEIN) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si un trait est "Plein" sans sortir des limites de la grille.
     *
     * @param m la matrice du jeu contenant l'état actuel des traits
     * @param ligne l'indice de la ligne où se situe le trait
     * @param col l'indice de la colonne où se situe le trait
     * @param horizontal vrai (true) si on teste un trait horizontal, faux (false) pour un trait vertical
     * @return vrai (true) si le trait existe et qu'il est marqué comme "PLEIN", faux (false) sinon
     */
    private boolean estPlein(Matrice m, int ligne, int col, boolean horizontal) {
        if (horizontal) {
            if (ligne < 0 || ligne > m.getHauteur() || col < 0 || col >= m.getLargeur()) return false;
            Case c = m.getCase(ligne, col);
            return (c != null && c.getTrait(0).getEtat() == ValeurTrait.PLEIN);
        } else {
            if (ligne < 0 || ligne >= m.getHauteur() || col < 0 || col > m.getLargeur()) return false;
            Case c = m.getCase(ligne, col);
            return (c != null && c.getTrait(1).getEtat() == ValeurTrait.PLEIN);
        }
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
     * @return le nom "Boucle atteignant un 1"
     */
    @Override
    public String getNom() {
        return NOM;
    }
}
