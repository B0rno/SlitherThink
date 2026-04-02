package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

import java.util.ArrayList;
import java.util.List;

/**
 * Technique: Technique avancée 1 (Continuité de la boucle)
 * Parcourt chaque intersection (coin) de la grille pour garantir que la boucle ne se retrouve jamais dans un cul-de-sac et ne se divise pas.
 */
public class TechniqueAvancee1 implements StrategieAide {

    // Attention : l'espace à la fin est conservé pour matcher exactement avec le JSON
    private static final String NOM = "Technique avancée 1 ";

    @Override
    public boolean estApplicable(Matrice m) {
        int hauteur = m.getHauteur();
        int largeur = m.getLargeur();

        // On parcourt chaque point de la grille.
        // Il y a (hauteur + 1) points verticalement et (largeur + 1) horizontalement.
        for (int r = 0; r <= hauteur; r++) {
            for (int c = 0; c <= largeur; c++) {

                List<Trait> traitsDuPoint = new ArrayList<>();

                // Récupération des 4 traits connectés à cette intersection
                Trait haut = getTraitHaut(m, r, c);
                Trait bas = getTraitBas(m, r, c);
                Trait gauche = getTraitGauche(m, r, c);
                Trait droite = getTraitDroite(m, r, c);

                if (haut != null) traitsDuPoint.add(haut);
                if (bas != null) traitsDuPoint.add(bas);
                if (gauche != null) traitsDuPoint.add(gauche);
                if (droite != null) traitsDuPoint.add(droite);

                int nbPleins = 0;
                int nbVides = 0;

                for (Trait t : traitsDuPoint) {
                    if (t.getEtat() == ValeurTrait.PLEIN) nbPleins++;
                    else if (t.getEtat() != ValeurTrait.PLEIN) nbVides++;
                    else if (t.getEtat() == ValeurTrait.CROIX) nbVides++; // On traite les croix comme des vides pour la continuité
                }

                // Condition 2 : 1 trait plein arrive et 1 seule sortie possible
                // La boucle DOIT continuer, donc le trait vide restant devient forcément plein.
                if (nbPleins == 1 && nbVides == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // --- Méthodes pour extraire les traits autour d'une intersection ---

    private Trait getTraitHaut(Matrice m, int r, int c) {
        if (r == 0) 
            return null; // Bordure haute
        if (c < m.getLargeur()) 
            return m.getCase(r - 1, c).getTrait(1); // Trait gauche de la case Haut-Droite
        return m.getCase(r - 1, c - 1).getTrait(2); // Trait droit de la case Haut-Gauche
    }

    private Trait getTraitBas(Matrice m, int r, int c) {
        if (r == m.getHauteur()) 
            return null; // Bordure basse
        if (c < m.getLargeur()) 
            return m.getCase(r, c).getTrait(1); // Trait gauche de la case Bas-Droite
        return m.getCase(r, c - 1).getTrait(2); // Trait droit de la case Bas-Gauche
    }

    private Trait getTraitGauche(Matrice m, int r, int c) {
        if (c == 0) return null; // Bordure gauche
        if (r < m.getHauteur()) 
            return m.getCase(r, c - 1).getTrait(0); // Trait haut de la case Bas-Gauche
        return m.getCase(r - 1, c - 1).getTrait(3); // Trait bas de la case Haut-Gauche
    }

    private Trait getTraitDroite(Matrice m, int r, int c) {
        if (c == m.getLargeur()) return null; // Bordure droite
        if (r < m.getHauteur())
             return m.getCase(r, c).getTrait(0); // Trait haut de la case Bas-Droite
        return m.getCase(r - 1, c).getTrait(3); // Trait bas de la case Haut-Droite
    }

    @Override
    public Aide trouverAide(Matrice m) {
        if (estApplicable(m)) {
            String technique = LoadTechniqueJson.getInstance().getDescription(NOM);
            return new Aide(NOM, technique);
        }
        return null;
    }

    @Override
    public String getNom() {
        return NOM;
    }
}