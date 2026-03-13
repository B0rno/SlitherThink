package com.lmu.SlitherThink.save.structure;

import java.util.List;

/**
 * Classe représentant les techniques sauvegardées.
 * Contient les techniques pour différents langages.
 */
public class SaveTechnique {
    private List<languageContenue> stockage_langague; // Liste des langages avec leurs contenus techniques

    /**
     * Retourne la liste des langages avec leurs contenus techniques.
     *
     * @return Une liste de contenus par langage.
     */
    public List<languageContenue> getStockageLangague() {
        return stockage_langague;
    }
}
