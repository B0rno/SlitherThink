package com.lmu.SlitherThink.save.structure;

import java.util.List;

/**
 * Classe représentant le contenu des techniques pour un langage donné.
 * Chaque langage contient une liste de niveaux avec leurs techniques associées.
 */
public class languageContenue {
    private String langage; // Nom du langage
    private List<contenuTechnique> contenu; // Liste des contenus techniques pour ce langage

    /**
     * Retourne le nom du langage.
     *
     * @return Le nom du langage.
     */
    public String getLangage() {
        return langage;
    }

    /**
     * Retourne la liste des contenus techniques pour ce langage.
     *
     * @return Une liste de contenus techniques.
     */
    public List<contenuTechnique> getContenu() {
        return contenu;
    }
}
