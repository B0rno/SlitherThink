package com.lmu.SlitherThink.Helper;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Helper.Techniques.Adjacents0Et3;
import com.lmu.SlitherThink.Helper.Techniques.AucuneLigneAutourDe0;
import com.lmu.SlitherThink.Helper.Techniques.ContraintesSur3;
import com.lmu.SlitherThink.Helper.Techniques.Deux3Adjacents;
import com.lmu.SlitherThink.Helper.Techniques.Deux3EnDiagonale;
import com.lmu.SlitherThink.Helper.Techniques.Diagonale0Et3;
import com.lmu.SlitherThink.Helper.Techniques.NimporteQuelNumeroDansUnCoin;
import com.lmu.SlitherThink.Helper.Techniques.TechniqueAvancee1;
import com.lmu.SlitherThink.Helper.Techniques.TechniqueAvancee2;
import com.lmu.SlitherThink.Helper.Techniques.TechniqueAvancee3;
import com.lmu.SlitherThink.Helper.Techniques.TechniqueAvancee4;
import com.lmu.SlitherThink.Helper.Techniques.TechniqueAvancee5;
import com.lmu.SlitherThink.Helper.Techniques.TechniqueAvancee6;

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
        
        // Aides débutant :
        strategies.add(new AucuneLigneAutourDe0());
        strategies.add(new Adjacents0Et3());
        strategies.add(new Diagonale0Et3());
        strategies.add(new Deux3Adjacents());
        strategies.add(new NimporteQuelNumeroDansUnCoin());
        strategies.add(new Deux3EnDiagonale());

        // Aide basique :
        strategies.add(new ContraintesSur3());

        //Aide confirme 
        strategies.add(new TechniqueAvancee1());
        strategies.add(new TechniqueAvancee2());
        strategies.add(new TechniqueAvancee3());
        strategies.add(new TechniqueAvancee4());
        strategies.add(new TechniqueAvancee5());
        strategies.add(new TechniqueAvancee6());


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
