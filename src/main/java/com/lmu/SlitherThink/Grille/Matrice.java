package com.lmu.SlitherThink.Grille;

import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.structure.SaveGrille;
import com.lmu.SlitherThink.save.structure.PositionTrait;
import com.lmu.SlitherThink.save.structure.positionGrille;

import java.util.List;

/**
 * Représente une matrice de cases pour le jeu SlitherLink.
 * 
 * La matrice gère un plateau rectangulaire de cases où les traits sont partagés
 * entre les cases adjacentes. Par exemple, le trait droit d'une case est le même
 * objet que le trait gauche de sa voisine à droite.
 * 
 * Structure des indices :
 * - 0 : haut (top)
 * - 1 : gauche (left)
 * - 2 : droite (right)
 * - 3 : bas (bottom)
 * 
 * @author Natp24109
 * @version 1.0
 */

/* TODO APPLIQUER LE DESIGN PATTERN SINGLETON ICI !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */

public class Matrice {
    /** Nombre de lignes de la matrice */
    private final int hauteur;
    
    /** Nombre de colonnes de la matrice */
    private final int largeur;
    
    /** Matrice bidimensionnelle des cases */
    private Case[][] grille;
    
    /** Matrice des traits horizontaux (partagés entre haut/bas des cases) */
    private Trait[][] traitsHorizontaux;
    
    /** Matrice des traits verticaux (partagés entre gauche/droite des cases) */
    private Trait[][] traitsVerticaux;

    /** Matrice des traits horizontaux solution */
    private Trait[][] traitsHorizontauxSol;

    /** Matrice des traits verticaux solution */
    private Trait[][] traitsVerticauxSol;

    /** Compteur de traits dans le bon état */
    private int cpt;

    /** Booleen donnant la complétion de la grille */
    private boolean completed;

    /**
     * Constructeur de la matrice.
     * Initialise une matrice de cases de dimensions hauteur x largeur.
     * Les traits sont automatiquement créés et partagés entre les cases adjacentes.
     * 
     * @param hauteur le nombre de lignes de cases
     * @param largeur le nombre de colonnes de cases
     */
    public Matrice(int hauteur, int largeur) {
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.grille = new Case[hauteur][largeur];

        this.cpt = 0;
        this.completed = false;
        
        // Créer les matrices de traits
        // traitsHorizontaux : (hauteur+1) x largeur
        this.traitsHorizontaux = new Trait[hauteur + 1][largeur];
        // traitsVerticaux : hauteur x (largeur+1)
        this.traitsVerticaux = new Trait[hauteur][largeur + 1];

        this.traitsHorizontauxSol = new Trait[hauteur + 1][largeur];
        this.traitsVerticauxSol = new Trait[hauteur][largeur + 1];
        
        // Initialiser tous les traits
        for (int i = 0; i <= hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                traitsHorizontaux[i][j] = new Trait();
                traitsHorizontauxSol[i][j] = new Trait();
            }
        }
        
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j <= largeur; j++) {
                traitsVerticaux[i][j] = new Trait();
                traitsVerticauxSol[i][j] = new Trait();
            }
        }
        
        // Créer les cases et assigner les traits partagés
        int numero = 0;
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                grille[i][j] = new Case(numero);
                
                // Assigner les traits à la case
                Trait[] traits = new Trait[4];
                traits[0] = traitsHorizontaux[i][j];            // haut
                traits[1] = traitsVerticaux[i][j];              // gauche
                traits[2] = traitsVerticaux[i][j + 1];          // droite
                traits[3] = traitsHorizontaux[i + 1][j];        // bas
                
                grille[i][j].setTraits(traits);
            }
        }
    }

    /**
     * Retourne la case à la position spécifiée.
     * 
     * @param ligne l'indice de ligne (0-indexed)
     * @param colonne l'indice de colonne (0-indexed)
     * @return la case à cette position, ou null si les indices sont invalides
     */
    public Case getCase(int ligne, int colonne) {
        if (ligne < 0 || ligne >= hauteur || colonne < 0 || colonne >= largeur) {
            return null;
        }
        return grille[ligne][colonne];
    }

    public Trait getTraitHorizSolution(int ligne, int colonne) {
        return this.traitsHorizontauxSol[ligne][colonne];
    }

    public Trait getTraitVertiSolution(int ligne, int colonne) {
        return this.traitsVerticauxSol[ligne][colonne];
    }

    /**
     * Retourne la hauteur de la matrice.
     * 
     * @return le nombre de lignes
     */
    public int getHauteur() {
        return hauteur;
    }

    /**
     * Retourne la largeur de la matrice.
     * 
     * @return le nombre de colonnes
     */
    public int getLargeur() {
        return largeur;
    }

    /**
     * Charge la solution dans la matrice.
     * Définit les états des traits solution pour tous les traits.
     */
    public static Matrice loadGrille(int hauteur, int largeur){
        LoadSave save = LoadSave.getInstance("");

        SaveGrille = save.getGrille();

        Matrice loadedMatrice = new Matrice(hauteur, largeur);

        for(positionGrille pos : save.getNumeroCases()){
            List<Integer> coord = pos.getPositionGrille();
            loadedMatrice.getCase(coord.get(0), coord.get(1)).setNumero(pos.getValeurGrille());
        }

        for(PositionTrait pos : save.getListePositionTrait()){
            List<Integer> coord = pos.getPositionTrait();
            List<Integer> etat = pos.getEtatTrait();
            
            int ligne = coord.get(0);
            int colonne = coord.get(1);
            
            // Pour chaque direction qui fait partie de la solution
            for(Integer direction : etat) {
                switch(direction) {
                    case 0: // Trait haut
                        loadedMatrice.getTraitHorizSolution(ligne, colonne).setTrait(ValeurTrait.PLEIN);
                        break;
                    case 1: // Trait gauche
                        loadedMatrice.getTraitVertiSolution(ligne, colonne).setTrait(ValeurTrait.PLEIN);
                        break;
                    case 2: // Trait droite
                        loadedMatrice.getTraitVertiSolution(ligne, colonne + 1).setTrait(ValeurTrait.PLEIN);
                        break;
                    case 3: // Trait bas
                        loadedMatrice.getTraitHorizSolution(ligne + 1, colonne).setTrait(ValeurTrait.PLEIN);
                        break;
                }
            }
        }

        loadedMatrice.compterTraitsValides();
        return loadedMatrice;
    }

    /**
     * Compte le nombre total de traits valides dans la matrice.
     * Un trait est valide si son état actuel correspond à la solution.
     * 
     * @return le nombre de traits valides
     */
    private int compterTraitsValides() {
        int total = 0;
        
        // Vérifier les traits horizontaux
        for (int i = 0; i <= hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (traitsHorizontaux[i][j].getEtat() == traitsHorizontauxSol[i][j].getEtat() ||
                    (traitsHorizontaux[i][j].getEtat() == ValeurTrait.CROIX && 
                     traitsHorizontauxSol[i][j].getEtat() != ValeurTrait.PLEIN)) {
                    total++;
                }
            }
        }
        
        // Vérifier les traits verticaux
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j <= largeur; j++) {
                if (traitsVerticaux[i][j].getEtat() == traitsVerticauxSol[i][j].getEtat() ||
                    (traitsVerticaux[i][j].getEtat() == ValeurTrait.CROIX && 
                     traitsVerticauxSol[i][j].getEtat() != ValeurTrait.PLEIN)) {
                    total++;
                }
            }
        }
        
        this.cpt = total;
        return total;
    }

    /**
     * Modifie l'état d'un trait d'une case.
     * 
     * @param ligne l'indice de ligne de la case (0-indexed)
     * @param colonne l'indice de colonne de la case (0-indexed)
     * @param direction l'index du trait (0=haut, 1=gauche, 2=droite, 3=bas)
     */
    public void cliquer(int ligne, int colonne, int direction){
        this.grille[ligne][colonne].updateTrait(direction);
        this.cpt = this.compterTraitsValides();
        if(this.cpt == (hauteur + 1) * largeur + hauteur * (largeur + 1)) {
            System.out.println("GAGNE !!!!!!!!!!!!!!!!!!!");
            this.completed = true;
        }
    }

    /**
     * Vérifie la complétion de la grille.
     * 
     * @return un booleen donnant l'information de complétion
     */
    public boolean verifierVictoire(){
        return this.completed;
    }

    /**
     * Retourne une représentation textuelle de la matrice.
     * Affiche l'état actuel de toutes les cases avec les traits correctement positionnés.
     * 
     * @return une chaîne représentant visuellement la matrice
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < hauteur; i++) {
            // Ligne des traits horizontaux du haut
            for (int j = 0; j < largeur; j++) {
                sb.append(" ");
                sb.append(getStringOfTraitHorizontal(i, j, 0)); // trait haut
            }
            sb.append("\n");
            
            // Ligne des cases avec traits verticaux
            for (int j = 0; j < largeur; j++) {
                sb.append(getStringOfTraitVertical(i, j, 1));   // trait gauche
                sb.append(String.format("[%d]", grille[i][j].getNumero()));
            }
            sb.append(getStringOfTraitVertical(i,largeur-1,2)); // trait droit de la dernière case
            sb.append("\n");
        }
        
        // Dernière ligne des traits horizontaux du bas
        for (int j = 0; j < largeur; j++) {
            sb.append(" ");
            sb.append(getStringOfTraitHorizontal(hauteur - 1, j, 3)); // trait bas
        }
        
        int totalTraits = (hauteur + 1) * largeur + hauteur * (largeur + 1);
        sb.append("\nSolution : ").append(this.cpt).append("/").append(totalTraits);
        
        return sb.toString();
    }

    /**
     * Récupère la représentation textuelle d'un trait horizontal.
     * 
     * @param ligne l'indice de ligne de la case
     * @param colonne l'indice de colonne de la case
     * @param direction 0 pour haut, 3 pour bas
     * @return une chaîne représentant le trait horizontal
     */
    private String getStringOfTraitHorizontal(int ligne, int colonne, int direction) {
        Trait trait;
        if (direction == 0) {
            trait = traitsHorizontaux[ligne][colonne];
        } else {
            trait = traitsHorizontaux[ligne + 1][colonne];
        }
        
        String representation = trait.toString();
        String[] parts = representation.split("\\?");
        return parts[1] + parts[1] + parts[1];
    }

    /**
     * Récupère la représentation textuelle d'un trait vertical.
     * 
     * @param ligne l'indice de ligne de la case
     * @param colonne l'indice de colonne de la case
     * @param direction 1 pour gauche, 2 pour droite
     * @return une chaîne représentant le trait vertical
     */
    private String getStringOfTraitVertical(int ligne, int colonne, int direction) {
        Trait trait;
        if (direction == 1) {
            trait = traitsVerticaux[ligne][colonne];
        } else {
            trait = traitsVerticaux[ligne][colonne + 1];
        }
        
        String representation = trait.toString();
        String[] parts = representation.split("\\?");
        return parts[0];
    }
}
