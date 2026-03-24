package com.lmu.SlitherThink.Helper;

import com.lmu.SlitherThink.Grille.Matrice;

/**
 * Interface pour les différentes stratégies de détection d'aides.
 * Chaque stratégie implémente un pattern SlitherLink spécifique.
 *
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */
public interface StrategieAide {

    /**
     * Vérifie si cette stratégie peut trouver une aide sur la matrice.
     *
     * @param m la matrice du jeu
     * @return true si une aide est disponible
     */
    boolean estApplicable(Matrice m);

    /**
     * Trouve et retourne la première aide détectée par cette stratégie.
     *
     * @param m la matrice du jeu
     * @return l'aide trouvée, ou null si aucune aide n'est disponible
     */
    Aide trouverAide(Matrice m);

    /**
     * Retourne le nom de cette stratégie (pour le debug).
     *
     * @return le nom de la stratégie
     */
    String getNom();
}
