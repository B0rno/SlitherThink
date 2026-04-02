package com.lmu.SlitherThink.Helper.Techniques;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.LoadTechniqueJson;
import com.lmu.SlitherThink.Helper.StrategieAide;

import java.util.HashSet;
import java.util.Set;

/**
 * Technique : Éviter une boucle séparée
 * Vérifie si l'ajout d'un trait fermerait une boucle isolée avant la fin de la partie.
 * Selon les règles, une seule boucle unique doit être formée sur toute la grille.
 * Pattern de base de SlitherLink niveau basique.
 *
 * @author Lucas Abeka-Doth (lucasabeka)
 * @version 1.0
 */
public class EviterBoucleSeparee implements StrategieAide {

    private static final String NOM = "Éviter une boucle séparée";

    /**
     * Vérifie si cette technique peut être appliquée.
     * La logique consiste à détecter si deux extrémités d'un même segment de chemin
     * sont séparées par un seul trait vide. Fermer ce trait créerait une boucle locale.
     *
     * @param m la matrice du jeu à analyser
     * @return true si une fermeture de boucle illégale est détectée
     */
    @Override
    public boolean estApplicable(Matrice m) {
        int totalTraitsSolution = compterTraitsSolution(m);
        int totalTraitsActuels = compterTraitsPlein(m);

        // Si on est sur le point de poser le dernier trait, la fermeture de la boucle est autorisée
        if (totalTraitsActuels >= totalTraitsSolution - 1) return false;

        // Analyse des traits horizontaux vides
        for (int i = 0; i <= m.getHauteur(); i++) {
            for (int j = 0; j < m.getLargeur(); j++) {
                if (m.getCase(i < m.getHauteur() ? i : i-1, j).getTrait(i < m.getHauteur() ? 0 : 3).getEtat() != ValeurTrait.PLEIN) {
                    // Vérifie si un chemin relie déjà le sommet gauche (i, j) au sommet droit (i, j+1)
                    if (existeChemin(m, i, j, i, j + 1)) return true;
                }
            }
        }

        // Analyse des traits verticaux vides
        for (int i = 0; i < m.getHauteur(); i++) {
            for (int j = 0; j <= m.getLargeur(); j++) {
                if (m.getCase(i, j < m.getLargeur() ? j : j-1).getTrait(j < m.getLargeur() ? 1 : 2).getEtat() != ValeurTrait.PLEIN) {
                    // Vérifie si un chemin relie déjà le sommet haut (i, j) au sommet bas (i+1, j)
                    if (existeChemin(m, i, j, i + 1, j)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie par un algorithme de parcours si un chemin de traits pleins relie deux sommets.
     *
     * @param m la matrice du jeu.
     * @param startL ligne du sommet de départ.
     * @param startC colonne du sommet de départ.
     * @param targetL ligne du sommet cible.
     * @param targetC colonne du sommet cible.
     * @return true si un chemin existe entre les deux sommets, false sinon.
     */
    private boolean existeChemin(Matrice m, int startL, int startC, int targetL, int targetC) {
        Set<String> visite = new HashSet<>();
        return dfs(m, startL, startC, targetL, targetC, visite);
    }

    /**
     * Algorithme de recherche en profondeur (DFS) pour explorer les traits pleins.
     *
     * @param m la matrice du jeu.
     * @param l ligne actuelle.
     * @param c colonne actuelle.
     * @param targetL ligne cible.
     * @param targetC colonne cible.
     * @param visite ensemble des sommets déjà explorés pour éviter les boucles infinies.
     * @return true si la cible est atteinte, false sinon.
     */
    private boolean dfs(Matrice m, int l, int c, int targetL, int targetC, Set<String> visite) {
        if (l == targetL && c == targetC) return true;
        visite.add(l + "," + c);

        // Directions cardinales : Haut, Bas, Gauche, Droite
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] d : directions) {
            int nextL = l + d[0];
            int nextC = c + d[1];
            String key = nextL + "," + nextC;

            if (nextL >= 0 && nextL <= m.getHauteur() && nextC >= 0 && nextC <= m.getLargeur() && !visite.contains(key)) {
                // On ne suit le chemin que si le trait entre le sommet actuel et le suivant est PLEIN
                if (estTraitPleinEntre(m, l, c, nextL, nextC)) {
                    if (dfs(m, nextL, nextC, targetL, targetC, visite)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Détermine si le trait situé entre deux sommets adjacents est marqué comme PLEIN.
     *
     * @param m la matrice du jeu.
     * @param l1 ligne du premier sommet.
     * @param c1 colonne du premier sommet.
     * @param l2 ligne du second sommet.
     * @param c2 colonne du second sommet.
     * @return true si le trait entre les deux points est PLEIN, false sinon.
     */
    private boolean estTraitPleinEntre(Matrice m, int l1, int c1, int l2, int c2) {
        // Trait Horizontal (même ligne)
        if (l1 == l2) {
            int minC = Math.min(c1, c2);
            Case cell = m.getCase(l1 < m.getHauteur() ? l1 : l1 - 1, minC);
            return cell.getTrait(l1 < m.getHauteur() ? 0 : 3).getEtat() == ValeurTrait.PLEIN;
        }
        // Trait Vertical (même colonne)
        if (c1 == c2) {
            int minL = Math.min(l1, l2);
            Case cell = m.getCase(minL, c1 < m.getLargeur() ? c1 : c1 - 1);
            return cell.getTrait(c1 < m.getLargeur() ? 1 : 2).getEtat() == ValeurTrait.PLEIN;
        }
        return false;
    }

    /**
     * Calcule le nombre total de traits que le joueur a tracé (état PLEIN) sur la grille.
     *
     * @param m la matrice actuelle.
     * @return le nombre de traits pleins.
     */
    private int compterTraitsPlein(Matrice m) {
        int count = 0;
        int hauteur = m.getHauteur();
        int largeur = m.getLargeur();

        // On parcourt tous les traits horizontaux (hauteur + 1)
        for (int i = 0; i <= hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                // Utilisation d'une case adjacente pour accéder au trait via getTrait
                // (Le trait 0 d'une case est le trait horizontal du haut)
                Case c = (i < hauteur) ? m.getCase(i, j) : m.getCase(i - 1, j);
                int direction = (i < hauteur) ? 0 : 3;
                if (c.getTrait(direction).getEtat() == ValeurTrait.PLEIN) {
                    count++;
                }
            }
        }

        // On parcourt tous les traits verticaux (largeur + 1)
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j <= largeur; j++) {
                // Le trait 1 d'une case est le trait vertical de gauche
                Case c = (j < largeur) ? m.getCase(i, j) : m.getCase(i, j - 1);
                int direction = (j < largeur) ? 1 : 2;
                if (c.getTrait(direction).getEtat() == ValeurTrait.PLEIN) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Calcule le nombre total de traits PLEIN requis dans la solution de la grille.
     *
     * @param m la matrice contenant la solution.
     * @return le nombre total de traits dans la boucle finale attendue.
     */
    private int compterTraitsSolution(Matrice m) {
        int count = 0;
        int hauteur = m.getHauteur();
        int largeur = m.getLargeur();

        // On utilise les getters de Matrice pour accéder aux traits de solution
        for (int i = 0; i <= hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (m.getTraitHorizSolution(i, j).getEtat() == ValeurTrait.PLEIN) {
                    count++;
                }
            }
        }

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j <= largeur; j++) {
                if (m.getTraitVertiSolution(i, j).getEtat() == ValeurTrait.PLEIN) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Trouve et retourne une aide basée sur cette technique.
     * Charge la description complète depuis le JSON et crée un objet Aide.
     *
     * @param m la matrice du jeu à analyser
     * @return une aide avec le nom et la description de la technique, ou null si non applicable
     */
    @Override
    public Aide trouverAide(Matrice m) {
        if (estApplicable(m)) {
            String technique = LoadTechniqueJson.getInstance().getDescription(NOM);
            return new Aide(NOM, technique);
        }
        return null;
    }

    /**
     * Retourne le nom de cette technique.
     *
     * @return le nom "Éviter une boucle séparée"
     */
    @Override
    public String getNom() {
        return NOM;
    }
}
