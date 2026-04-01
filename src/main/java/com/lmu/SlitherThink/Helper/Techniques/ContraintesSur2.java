package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

/**
 * Technique : Contraintes sur un 2 (Diagonale forcée)
 * Détecte les situations où la boucle arrive sur le coin d'un '2' alors qu'un des
 * deux chemins possibles est bloqué par une croix (X). La boucle est alors forcée
 * d'emprunter l'autre côté adjacent au sommet pour satisfaire la contrainte du chiffre.
 *
 * @author Lucas Abeka-Doth (lucasabeka)
 * @version 1.0
 */
public class ContraintesSur2 implements StrategieAide {

    private static final String NOM = "Contraintes sur un 2";

    /**
     * Vérifie si cette technique peut être appliquée sur la matrice.
     * Cherche un segment de boucle arrivant sur un sommet d'un '2' dont l'une
     * des sorties est obstruée par une croix.
     *
     * @param m la matrice du jeu à analyser
     * @return true si un trait peut être déduit de cette configuration, false sinon
     */
    @Override
    public boolean estApplicable(Matrice m) {
        for (int i = 0; i < m.getHauteur(); i++) {
            for (int j = 0; j < m.getLargeur(); j++) {
                Case c = m.getCase(i, j);
                // On analyse uniquement les cases contenant le chiffre 2
                if (c != null && c.getNumero() == 2) {

                    // Coin haut gauche (nord ouest)
                    // Si la boucle arrive sur ce coin (trait horizontal à gauche ou vertical au-dessus)
                    if (estPleinExt(m, i, j - 1, true) || estPleinExt(m, i - 1, j, false)) {
                        // Si le haut (0) est une croix, alors la gauche (1) doit être plein (ou inversement)
                        if (verifContrainteCoin(c, 0, 1)) return true;
                    }

                    // Coin haut droite (nord est)
                    // Si la boucle arrive sur ce coin (trait horizontal à droite ou vertical au-dessus)
                    if (estPleinExt(m, i, j + 1, true) || estPleinExt(m, i - 1, j + 1, false)) {
                        if (verifContrainteCoin(c, 0, 2)) return true;
                    }

                    // Coin bas gauche (sud ouest)
                    // Si la boucle arrive sur ce coin (trait horizontal à gauche ou vertical en-dessous)
                    if (estPleinExt(m, i + 1, j - 1, true) || estPleinExt(m, i + 1, j, false)) {
                        if (verifContrainteCoin(c, 3, 1)) return true;
                    }

                    // Coin bas droite (sud est)
                    // Si la boucle arrive sur ce coin (trait horizontal à droite ou vertical en-dessous)
                    if (estPleinExt(m, i + 1, j + 1, true) || estPleinExt(m, i + 1, j + 1, false)) {
                        if (verifContrainteCoin(c, 3, 2)) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si, sur un coin donné, un côté est bloqué (croix) et l'autre est encore vide.
     *
     * @param c La case à analyser (contenant un 2).
     * @param dir1 L'index du premier trait formant le sommet.
     * @param dir2 L'index du second trait formant le sommet.
     * @return vrai si l'un est une croix et l'autre est vide (déduction possible).
     */
    private boolean verifContrainteCoin(Case c, int dir1, int dir2) {
        // Si le premier chemin est bloqué, le second devient obligatoire (s'il est encore vide)
        if (c.getTrait(dir1).getEtat() == ValeurTrait.CROIX && c.getTrait(dir2).getEtat() == ValeurTrait.VIDE)
            return true;
        // Si le second chemin est bloqué, le premier devient obligatoire (s'il est encore vide)
        if (c.getTrait(dir2).getEtat() == ValeurTrait.CROIX && c.getTrait(dir1).getEtat() == ValeurTrait.VIDE)
            return true;
        return false;
    }

    /**
     * Vérifie l'état d'un trait situé à la périphérie d'une case (potentiellement sur les bords de la grille).
     * Cette méthode sécurise l'accès aux traits horizontaux et verticaux en gérant les indices
     * limites pour éviter les erreurs de type IndexOutOfBounds.
     *
     * @param m La matrice de jeu à analyser.
     * @param ligne L'indice de ligne du trait.
     * @param col L'indice de colonne du trait.
     * @param horizontal Vrai si le trait testé est horizontal, faux s'il est vertical.
     * @return vrai (true) si le trait existe et qu'il est marqué comme "PLEIN", faux (false) sinon
     */
    private boolean estPleinExt(Matrice m, int ligne, int col, boolean horizontal) {
        if (horizontal) {
            if (ligne < 0 || ligne > m.getHauteur() || col < 0 || col >= m.getLargeur()) return false;
            // Accès sécurisé au trait horizontal via la matrice
            return m.getCase(ligne < m.getHauteur() ? ligne : ligne-1, col).getTrait(ligne < m.getHauteur() ? 0 : 3).getEtat() == ValeurTrait.PLEIN;
        } else {
            if (ligne < 0 || ligne >= m.getHauteur() || col < 0 || col > m.getLargeur()) return false;
            return m.getCase(ligne, col < m.getLargeur() ? col : col-1).getTrait(col < m.getLargeur() ? 1 : 2).getEtat() == ValeurTrait.PLEIN;
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
     * @return le nom "Contraintes sur un 2"
     */
    @Override
    public String getNom() {
        return NOM;
    }
}
