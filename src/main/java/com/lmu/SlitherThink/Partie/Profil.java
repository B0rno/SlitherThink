package com.lmu.SlitherThink.Partie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Représente le profil d'un joueur.
 * Conserve le pseudo du joueur et l'historique de ses scores.
 *
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */
public class Profil {
    private String pseudo;
    private List<Score> historique = new ArrayList<>();

    /**
     * Crée un profil avec le pseudo donné.
     *
     * @param pseudo le pseudo du joueur
     */
    public Profil(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Retourne le pseudo du joueur.
     *
     * @return le pseudo du joueur
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Ajoute un score à l'historique du profil.
     *
     * @param s le score à ajouter
     */
    public void ajouterScore(Score s) {
        historique.add(s);
    }

    /**
     * Retourne le meilleur score basé sur le nombre d'étoiles.
     *
     * @return le score avec le plus d'étoiles, ou null si l'historique est vide
     */
    public Score getMeilleurScore() {
        return historique.stream()
            .max(Comparator.comparingInt(Score::getEtoiles))
            .orElse(null);
    }

}
