package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique : Boucle atteignant un 3
 * Détecte les situations où un segment de la boucle arrive perpendiculairement sur
 * l'un des sommets d'une case '3'. Cette configuration force la boucle à emprunter
 * les deux côtés du '3' qui ne touchent pas ce sommet.
 * Pattern de base de SlitherLink pour la continuité du chemin.
 *
 * @author Lucas Abeka-Doth (lucasabeka)
 * @version 1.0
 */
public class BoucleSur3 implements StrategieAide {

    private static final String NOM = "Boucle atteignant un 3";

    /**
     * Vérifie si cette technique peut être appliquée sur la matrice.
     * Cherche un trait plein arrivant sur le coin d'un 3 depuis l'extérieur,
     * ce qui permet de déduire les segments opposés de cette case.
     *
     * @param m la matrice du jeu à analyser
     * @return true si des traits peuvent être déduits, false sinon
     */
    @Override
    public boolean estApplicable(Matrice m) {
        for (int i = 0; i < m.getHauteur(); i++) {
            for (int j = 0; j < m.getLargeur(); j++) {
                Case c = m.getCase(i, j);
                // On cible uniquement les cases contenant un 3
                if (c != null && c.getNumero() == 3) {

                    // Compter le nombre de traits déjà pleins
                    int nbPleins = 0;
                    for (int dir = 0; dir < 4; dir++) {
                        if (c.getTrait(dir).getEtat() == ValeurTrait.PLEIN) {
                            nbPleins++;
                        }
                    }

                    // Si déjà 3 traits pleins, la contrainte est satisfaite, ne rien suggérer
                    if (nbPleins >= 3) {
                        continue;
                    }

                    // Test coin haut-gauche (Nord-Ouest)
                    // Si un trait arrive de la gauche ou du haut vers ce coin
                    if (estPlein(m, i, j - 1, 2) || estPlein(m, i - 1, j, 3)) {
                        // Alors les murs opposés (droite : 2 et bas : 3) doivent être tracés
                        if (c.getTrait(2).getEtat() != ValeurTrait.PLEIN ||
                                c.getTrait(3).getEtat() != ValeurTrait.PLEIN) {
                            return true;
                        }
                    }

                    // Test haut-droite (Nord-Est)
                    // Si un trait arrive de la droite ou du haut vers ce coin
                    if (estPlein(m, i, j + 1, 1) || estPlein(m, i - 1, j, 3)) {
                        // Alors les murs opposés (gauche : 1 et bas : 3) doivent être tracés
                        if (c.getTrait(1).getEtat() != ValeurTrait.PLEIN ||
                                c.getTrait(3).getEtat() != ValeurTrait.PLEIN) {
                            return true;
                        }
                    }

                    // Test bas-gauche (Sud-Ouest)
                    // Si un trait arrive de la gauche ou du bas vers ce coin
                    if (estPlein(m, i, j - 1, 2) || estPlein(m, i + 1, j, 0)) {
                        // Alors les murs opposés (haut : 0 et droite : 2) doivent être tracés
                        if (c.getTrait(0).getEtat() != ValeurTrait.PLEIN ||
                                c.getTrait(2).getEtat() != ValeurTrait.PLEIN) {
                            return true;
                        }
                    }

                    // Test bas-droite (Sud-Est)
                    // Si un trait arrive de la droite ou du bas vers ce coin
                    if (estPlein(m, i, j + 1, 1) || estPlein(m, i + 1, j, 0)) {
                        // Alors les murs opposés (haut : 0 et gauche : 1) doivent être tracés
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
     * @param ligne l'indice de la ligne de la case
     * @param col l'indice de la colonne de la case
     * @param indexTrait l'indice du trait à tester (0=Nord, 1=Ouest, 2=Est, 3=Sud)
     * @return vrai (true) si le trait existe et qu'il est marqué comme "PLEIN", faux (false) sinon
     */
    private boolean estPlein(Matrice m, int ligne, int col, int indexTrait) {
        if (ligne < 0 || ligne >= m.getHauteur() || col < 0 || col >= m.getLargeur()) {
            return false;
        }
        Case c = m.getCase(ligne, col);
        return (c != null && c.getTrait(indexTrait).getEtat() == ValeurTrait.PLEIN);
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
     * @return le nom "Boucle atteignant un 3"
     */
    @Override
    public String getNom() {
        return NOM;
    }
}
