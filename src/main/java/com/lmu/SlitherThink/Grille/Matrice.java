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
        
        // Créer les matrices de traits
        // traitsHorizontaux : (hauteur+1) x largeur
        this.traitsHorizontaux = new Trait[hauteur + 1][largeur];
        // traitsVerticaux : hauteur x (largeur+1)
        this.traitsVerticaux = new Trait[hauteur][largeur + 1];
        
        // Initialiser tous les traits
        for (int i = 0; i <= hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                traitsHorizontaux[i][j] = new Trait();
            }
        }
        
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j <= largeur; j++) {
                traitsVerticaux[i][j] = new Trait();
            }
        }
        
        // Créer les cases et assigner les traits partagés
        int numero = 0;
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                grille[i][j] = new Case(numero);
                
                // Assigner les traits à la case
                Trait[] traits = new Trait[4];
                traits[0] = traitsHorizontaux[i][j];        // haut
                traits[1] = traitsVerticaux[i][j];          // gauche
                traits[2] = traitsVerticaux[i][j + 1];      // droite
                traits[3] = traitsHorizontaux[i + 1][j];    // bas
                
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

    public void cliquer(int ligne, int colonne, int direction){
        grille[ligne][colonne].updateTrait(direction);
    }

    /**
     * Retourne une représentation textuelle de la matrice.
     * Affiche l'état actuel de toutes les cases.
     * 
     * @return une chaîne représentant visuellement la matrice
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                sb.append(grille[i][j]).append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
