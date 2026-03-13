package com.lmu.SlitherThink.save.structure;

import java.util.List;

/**
 * Classe représentant une position dans la grille.
 * Contient les coordonnées et la valeur associée à cette position.
 */
public class positionGrille {
    private List<Integer> position; // Coordonnées de la position (x, y)
    private int valeur; // Valeur associée à la position

    /**
     * Retourne les coordonnées de la position.
     *
     * @return Une liste contenant les coordonnées.
     */
    public List<Integer> getPositionGrille() {
        return position;
    }

    /**
     * Retourne la valeur associée à la position.
     *
     * @return La valeur de la position.
     */
    public int getValeurGrille() {
        return valeur;
    }
}
