package com.lmu.SlitherThink.save.structure;

import java.util.List;

/**
 * Classe représentant une grille sauvegardée.
 * Contient les informations sur la taille, les cases et les traits de la grille.
 */
public class SaveGrille {
    private int nv_grille; // Niveau de la grille
    private int taille; // Taille de la grille
    private List<positionGrille> numero_cases; // Liste des cases avec leurs positions et valeurs
    private List<PositionTrait> liste_position_trait; // Liste des traits avec leurs positions et états

    /**
     * Retourne le niveau de la grille.
     *
     * @return Le niveau de la grille.
     */
    public int getNvGrille() {
        return nv_grille;
    }

    /**
     * Retourne la taille de la grille.
     *
     * @return La taille de la grille.
     */
    public int getTailleGrille() {
        return taille;
    }

    /**
     * Retourne la liste des cases de la grille.
     *
     * @return Une liste de cases.
     */
    public List<positionGrille> getNumeroCases() {
        return numero_cases;
    }

    /**
     * Retourne la liste des traits de la grille.
     *
     * @return Une liste de traits.
     */
    public List<PositionTrait> getListePositionTrait() {
        return liste_position_trait;
    }

    @Override
    public String toString() {
        return 
        "SaveGrille{" +
            "nv_grille=" + nv_grille +
            ", taille=" + taille +
            ", numero_cases=" + numero_cases +
            ", liste_position_trait=" + liste_position_trait +
        '}';
    }
}
