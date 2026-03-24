package com.lmu.SlitherThink.Helper;

/**
 * Représente une aide trouvée par le système.
 * Contient le nom et les informations sur la technique d'aide.
 *
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */
public class Aide {

    private final String nom;
    private final String techniqueLiee;

    /**
     * Constructeur complet d'une aide.
     *
     * @param nom nom de la technique d'aide (ex: "Contraintes sur 3")
     * @param techniqueLiee description détaillée de la technique d'aide, extraite du JSON 
     */
    public Aide(String nom, String techniqueLiee) {
        this.nom = nom;
        this.techniqueLiee = techniqueLiee;
    }

    // Getters pour les champs de l'aide

    public String getNom() {
        return nom;
    }

    public String getTechniqueLiee() {
        return techniqueLiee;
    }
}
