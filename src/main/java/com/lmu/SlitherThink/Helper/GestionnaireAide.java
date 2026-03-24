package com.lmu.SlitherThink.Helper;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Helper.Techniques.ContraintesSur3;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire central des aides pour le jeu SlitherLink.
 * Il contient une liste de stratégies d'aide et peut trouver la première aide applicable.
 *
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */
public class GestionnaireAide {
    private List<StrategieAide> strategies ; // Liste des stratégies d'aide à appliquer selon leur priorité

    /**
     * Construit un gestionnaire d'aides et initialise la liste des stratégies.
     * L'ordre d'ajout des stratégies définit leur priorité : les techniques les plus simples
     * et les plus fréquentes doivent être ajoutées en premier pour optimiser la recherche.
     */
    public GestionnaireAide() {
        this.strategies = new ArrayList<>();
        // Ordre d'ajout = ordre de priorité (les stratégies les plus simples en premier)
        strategies.add(new ContraintesSur3());
    }

    /**
     * Cherche la première aide applicable sur la matrice donnée.
     * Parcourt les stratégies dans l'ordre de priorité et retourne la première aide trouvée.
     *
     * @param m la matrice du jeu à analyser
     * @return la première aide trouvée, ou null si aucune aide n'est disponible
     */
    public Aide trouverAide(Matrice m) {
        for (StrategieAide strategie : strategies) {
            if (strategie.estApplicable(m)) {
                return strategie.trouverAide(m);
            }
        }
        return null; // Aucune aide trouvée
    }
}
