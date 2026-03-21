package com.lmu.SlitherThink.save.structure;

import java.util.List;

/**
 * Classe représentant le contenu des techniques pour un niveau donné.
 * Chaque niveau contient une liste de techniques associées.
 */
public class contenuTechnique {
    private String nv; // Niveau de difficulté
    private List<stockageTechnique> techniqueParsNv; // Liste des techniques pour ce niveau

    /**
     * Retourne le niveau de difficulté.
     *
     * @return Le niveau de difficulté.
     */
    public String getNv() {
        return nv;
    }

    /**
     * Retourne la liste des techniques associées à ce niveau.
     *
     * @return Une liste de techniques.
     */
    public List<stockageTechnique> getTechniqueParsNv() {
        return techniqueParsNv;
    }
}
