/**
 * Représente une case du plateau de jeu SlitherLink.
 * 
 * Une case est caractérisée par :
 * - Un numéro unique identifiant la case
 * - Un tableau de 4 traits (haut, gauche, droite, bas) pour l'état actuel du jeu
 * - Un tableau de 4 traits pour la solution attendue
 * 
 * Les directions des traits sont indexées comme suit :
 * - 0 : haut (top)
 * - 1 : gauche (left)
 * - 2 : droite (right)
 * - 3 : bas (bottom)
 * 
 * @author Natp24109
 * @version 1.0
 */
public class Case {
    /** Numéro unique identifiant cette case */
    private final int numero_case;
    
    /** Tableau contenant l'état de la solution pour les 4 traits */
    private Trait[] solution;
    
    /** Tableau contenant l'état actuel du jeu pour les 4 traits */
    private Trait[] etatJeu;

    /**
     * Initialise l'état initial d'une case avec tous les traits en état VIDE.
     * 
     * @return un tableau de 4 traits tous initialisés à VIDE
     */
    private static Trait[] getEtatInitialCase(){
        return new Trait[]{new Trait(),new Trait(),new Trait(),new Trait()};
    }

    /**
     * Constructeur d'une case.
     * Initialise la case avec son numéro et place tous les traits en état VIDE.
     * 
     * @param num le numéro unique de la case
     */
    public Case(int num){
        this.numero_case = num;
        this.etatJeu = Case.getEtatInitialCase();
        this.solution = Case.getEtatInitialCase();
    }

    /**
     * Retourne le numéro unique de cette case.
     * 
     * @return le numéro de la case
     */
    public int getNumero() {
        return numero_case;
    }

    /**
     * Définit l'état de la solution pour cette case.
     * 
     * @param solutionCase un tableau de 4 traits représentant la solution attendue
     */
    public void setSolutionCase(Trait[] solutionCase){
        this.solution = solutionCase;
    }

    /**
     * Récupère l'état d'un trait spécifique de cette case.
     * 
     * @param direction l'index du trait (0=haut, 1=gauche, 2=droite, 3=bas)
     * @return l'état du trait à cette direction
     */
    public Trait getTrait(int direction){
        return etatJeu[direction];
    }

    /**
     * Définit les traits partagés pour cette case.
     * 
     * @param traits un tableau de 4 traits (haut, gauche, droite, bas)
     */
    public void setTraits(Trait[] traits) {
        this.etatJeu = traits;
    }

    /**
     * Récupère la représentation textuelle d'un trait spécifique.
     * 
     * Extrait la partie pertinente de la représentation du trait selon la direction :
     * - Pour les directions 0 (haut) et 3 (bas), utilise la deuxième partie si disponible
     * - Pour les autres directions, utilise la première partie
     * 
     * @param direction l'index du trait (0=haut, 1=gauche, 2=droite, 3=bas)
     * @return une chaîne représentant le trait
     */
    private String getStringOfTrait(int direction) {
        String representation = this.getTrait(direction).toString();
        String[] parts = representation.split("\\?");

        boolean useSecondPart = (direction == 0 || direction == 3);

        if (useSecondPart && parts.length > 1) {
            return parts[1];
        }
        return parts[0];
    }

    /**
     * Fait passer au trait suivant son état.
     * Utilise la séquence : VIDE → PLEIN → CROIX → VIDE
     * 
     * @param direction l'index du trait à modifier (0=haut, 1=gauche, 2=droite, 3=bas)
     */
    public void updateTrait(int direction){
        this.etatJeu[direction].etatSuivant();
    }

    /**
     * Retourne une représentation visuelle de la case au format texte.
     * Affiche la case avec son numéro au centre et les traits tout autour.
     * 
     * @return une chaîne représentant visuellement la case
     */
    public String toString(){
        return " " + getStringOfTrait(0) + " "
            + getStringOfTrait(1) + this.numero_case + getStringOfTrait(2) + " "
            + " " + getStringOfTrait(3) + " ";
    }
}