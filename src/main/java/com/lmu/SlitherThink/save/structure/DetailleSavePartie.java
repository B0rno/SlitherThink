package com.lmu.SlitherThink.save.structure;

import java.util.List;

/**
 * Classe représentant les détails d'une sauvegarde de partie.
 * Contient l'état de la grille, le chronomètre et le nombre d'aides utilisées.
 */
public class DetailleSavePartie {
    private List<PositionTrait> etatgrille; // État des traits de la grille
    private Integer Chronometre; // Temps écoulé en secondes
    private Integer nbAides; // Nombre d'aides utilisées
    private transient String nameClass = null; // Nom personnalisé pour la sauvegarde

    public DetailleSavePartie() {}

    private DetailleSavePartie(List<PositionTrait> etatgrille, Integer chronometre, Integer nbAides) {
        this.etatgrille = etatgrille;
        this.Chronometre = chronometre;
        this.nbAides = nbAides;
    }

    /**
     * Crée une nouvelle instance de `DetailleSavePartie`.
     *
     * @param etatgrille L'état des traits de la grille.
     * @param chronometre Le temps écoulé.
     * @param nbAides Le nombre d'aides utilisées.
     * @return Une instance de `DetailleSavePartie`.
     */
    public static DetailleSavePartie create(List<PositionTrait> etatgrille, Integer chronometre, Integer nbAides) {
        return new DetailleSavePartie(etatgrille, chronometre, nbAides);
    }

    /**
     * Retourne l'état des traits de la grille.
     *
     * @return Une liste de traits.
     */
    public List<PositionTrait> getEtatGrille() {
        return etatgrille;
    }

    /**
     * Retourne le temps écoulé.
     *
     * @return Le chronomètre en secondes.
     */
    public Integer getChronometre() {
        return Chronometre;
    }

    /**
     * Retourne le nombre d'aides utilisées.
     *
     * @return Le nombre d'aides.
     */
    public Integer getNbAides() {
        return nbAides;
    }

    /**
     * Retourne le nom personnalisé de la sauvegarde.
     *
     * @return Le nom de la sauvegarde.
     */
    public String getNameClass() {
        return nameClass;
    }

    /**
     * Définit le nom personnalisé de la sauvegarde.
     *
     * @param nameClass Le nom à définir.
     */
    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }
}
