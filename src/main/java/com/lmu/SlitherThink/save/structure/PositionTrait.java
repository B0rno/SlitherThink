package com.lmu.SlitherThink.save.structure;

import java.util.List;

/**
 * Classe représentant un trait dans la grille.
 * Contient les coordonnées et l'état du trait.
 */
public class PositionTrait {
    private List<Integer> position; // Coordonnées du trait
    private List<Integer> etat; // État du trait (VIDE, PLEIN, CROIX)

    public PositionTrait() {}

    private PositionTrait(List<Integer> position, List<Integer> etat) {
        this.position = position;
        this.etat = etat;
    }

    /**
     * Crée une nouvelle instance de `PositionTrait`.
     *
     * @param position Les coordonnées du trait.
     * @param etat L'état du trait.
     * @return Une instance de `PositionTrait`.
     */
    public static PositionTrait create(List<Integer> position, List<Integer> etat) {
        return new PositionTrait(position, etat);
    }

    /**
     * Retourne les coordonnées du trait.
     *
     * @return Une liste contenant les coordonnées.
     */
    public List<Integer> getPositionTrait() {
        return position;
    }

    /**
     * Retourne l'état du trait.
     *
     * @return Une liste contenant l'état.
     */
    public List<Integer> getEtatTrait() {
        return etat;
    }

    @Override
    public String toString() {
        return 
        "PositionTrait{" +
            "position=" + position +
            ", etat=" + etat +
        '}';
    }
}
