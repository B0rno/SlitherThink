package com.lmu.SlitherThink.Partie;

import java.time.Instant;
import java.time.Duration;

/**
 * Gère le score et le chronomètre d'une partie.
 * Calcule le nombre d'étoiles obtenues en fonction du temps et des aides utilisées.
 *
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */
public class Score {
    private Instant debutSession = null;
    private Duration dureeAccumulee = Duration.ZERO;
    private int nbCoups = 0;
    private int nbAidesUtilisees = 0;
    private int etoiles = 0;
    private long dureDeuxEtoile = 300; // 5 minutes pour 2 étoile
    private long dureeTroisEtoiles = 120; // 2 minutes pour 3 étoiles

    /**
     * Démarre le chronomètre.
     * Enregistre l'instant de début de la session de jeu.
     */
    public void demarrerChrono() {
        debutSession = Instant.now();
    }

    /**
     * Met le chronomètre en pause.
     * Accumule le temps écoulé depuis le dernier démarrage.
     */
    public void pauseChrono() {
        dureeAccumulee = dureeAccumulee.plus(Duration.between(debutSession, Instant.now()));
    }

    /**
     * Arrête définitivement le chronomètre.
     * Accumule le temps écoulé et réinitialise l'instant de début.
     */
    public void arreterChrono() {
        if (debutSession != null) {
            dureeAccumulee = dureeAccumulee.plus(Duration.between(debutSession, Instant.now()));
            debutSession = null;
        }
    }

    /**
     * Incrémente le compteur d'aides utilisées.
     */
    public void utiliserAide() {
        nbAidesUtilisees++;
    }

    /**
     * Incrémente le compteur de coups joués.
     */
    public void incrementerCoups() {
        nbCoups++;
    }

    /**
     * Calcule le nombre d'étoiles obtenues.
     * 3 étoiles : temps inférieur ou égal à 2 minutes sans aide.
     * 2 étoiles : temps inférieur ou égal à 5 minutes avec maximum 2 aides.
     * 1 étoile : sinon.
     */
    public void calculerEtoiles() {
        long secondes = dureeAccumulee.getSeconds();
        if (secondes <= dureeTroisEtoiles && nbAidesUtilisees == 0) etoiles = 3;
        else if (secondes <= dureDeuxEtoile && nbAidesUtilisees <= 2) etoiles = 2;
        else etoiles = 1;
    }

    /**
     * Retourne le nombre d'étoiles obtenues.
     *
     * @return le nombre d'étoiles (entre 0 et 3)
     */
    public int getEtoiles() {
        return etoiles;
    }

    /**
     * Retourne la durée totale de jeu accumulée.
     *
     * @return la durée en secondes
     */
    public long getDureeEnSecondes() {
        return dureeAccumulee.getSeconds();
    }

    /**
     * Retourne le nombre de coups joués.
     *
     * @return le nombre de coups
     */
    public int getNbCoups() {
        return nbCoups;
    }

    /**
     * Retourne le nombre d'aides utilisées.
     *
     * @return le nombre d'aides utilisées
     */
    public int getNbAidesUtilisees() {
        return nbAidesUtilisees;
    }
}
