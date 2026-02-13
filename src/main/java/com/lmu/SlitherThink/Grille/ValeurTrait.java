/**
 * Énumération représentant les états possibles d'un trait dans une case du jeu SlitherLink.
 * 
 * Un trait peut être dans trois états :
 * - VIDE : le trait n'est pas marqué
 * - PLEIN : le trait est dessiné (ligne du SlitherLink)
 * - CROIX : le trait est marqué comme ne devant pas être utilisé
 * 
 * @author Natp24109
 * @version 1.0
 */
public enum ValeurTrait {
    /** État initial d'un trait, non marqué */
    VIDE,
    /** État d'un trait qui fait partie de la solution */
    PLEIN,
    /** État d'un trait marqué comme impossible */
    CROIX;

    /**
     * Retourne l'état suivant du trait selon une séquence circulaire.
     * La séquence est : VIDE → PLEIN → CROIX → VIDE
     * 
     * @return le nouvel état du trait
     */
    public ValeurTrait etatSuivant(){
        return switch(this){
            case VIDE -> PLEIN;
            case PLEIN -> CROIX;
            case CROIX -> VIDE;
        };
    }

    /**
     * Retourne une représentation textuelle du trait.
     * 
     * @return une chaîne de caractères représentant visuellement l'état du trait :
     *         - " ?  " pour VIDE
     *         - "┃?━" pour PLEIN
     *         - "*?*" pour CROIX
     */
    public String toString(){
        return switch(this){
            case VIDE -> " ? ";
            case PLEIN -> "┃?━";
            case CROIX -> "*?*";
        };
    }
}