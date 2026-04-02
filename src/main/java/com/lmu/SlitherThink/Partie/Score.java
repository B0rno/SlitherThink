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

//modif : une etoile pour une partie complétée, une étoile pour un temps respecté ou aides respectées, une étoile si tout est respecté
public class Score {
    private Instant debutSession = null;
    private java.time.Duration dureeAccumulee = java.time.Duration.ZERO;
    private int nbCoups = 0;
    private int nbAidesUtilisees = 0;
    private int nbAidexMax = 3; //3 aides max pour l'étoile
    private int etoiles = 0;
    private long dureePourEtoile = 300; // 5 minutes pour l'étoile

    /**
     * Démarre le chronomètre.
     * Enregistre l'instant de début de la session de jeu.
     */
    public void demarrerChrono() {
        if (debutSession == null) {
            debutSession = Instant.now();
        }
    }

    /**
     * Met le chronomètre en pause.
     * Accumule le temps écoulé depuis le dernier démarrage.
     */
    public void pauseChrono() {
        if (debutSession != null) {
            dureeAccumulee = dureeAccumulee.plus(java.time.Duration.between(debutSession, Instant.now()));
            debutSession = null;
        }
    }

    /**
     * Arrête définitivement le chronomètre.
     * Accumule le temps écoulé et réinitialise l'instant de début.
     */
    public void arreterChrono() {
        if (debutSession != null) {
            dureeAccumulee = dureeAccumulee.plus(java.time.Duration.between(debutSession, Instant.now()));
            debutSession = null;
        }
    }


    /**
     * Calcule le nombre d'étoiles obtenues.
     * 3 étoiles : temps inférieur ou égal à 5 minutes avec moins de 3 aides.
     * 2 étoiles : temps inférieur ou égal à 5 minutes avec 3 aides ou plus.
     * 2 étoiles : temps supérieur à 5 minutes avec moins de 3 aides.
     * 1 étoile : sinon.
     */
    public void calculerEtoiles() {
        long secondes = getDureeEnSecondes();
        if (secondes <= dureePourEtoile && nbAidesUtilisees < nbAidexMax) etoiles = 3;
        else if (secondes <= dureePourEtoile) etoiles = 2;
        else if (nbAidesUtilisees < nbAidexMax) etoiles = 2;
        else etoiles = 1;
    }

    /**
     * Initialise les valeurs pour la reconstruction d'une partie sauvegardée.
     *
     * @param chronoEnSecondes la durée accumulée en secondes
     * @param aidesUtilisees le nombre d'aides utilisées
     */
    public void setReconstructionSave(long chronoEnSecondes, int aidesUtilisees) {
        this.dureeAccumulee = Duration.ofSeconds(chronoEnSecondes);
        this.nbAidesUtilisees = aidesUtilisees;
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
        if (debutSession != null) {
            return dureeAccumulee.plus(java.time.Duration.between(debutSession, Instant.now())).getSeconds();
        }
        return dureeAccumulee.getSeconds();
    }

    public void setDureeAccumulee(java.time.Duration dureeAccumulee) {
        this.dureeAccumulee = dureeAccumulee;
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

    public void setNbAidesUtilisees(int nbAidesUtilisees) {
        this.nbAidesUtilisees = nbAidesUtilisees;
    }

    public long getDureePourEtoile() {
        return dureePourEtoile;
    }

    public int getNbAidexMax() {
        return nbAidexMax;
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
}
