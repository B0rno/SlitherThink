package com.lmu.SlitherThink.Partie;

import java.time.Instant;
import java.time.Duration;


public class Score {
    private Instant debutSession;
    private Duration dureeAccumulee;
    private int nbCoups = 0;
    private int nbAidesUtilisees = 0;
    private int etoiles = 0;
    private long dureDeuxEtoile = 300; // 5 minutes pour 2 étoile
    private long dureeTroisEtoiles = 120; // 2 minutes pour 3 étoiles

    public void demarrerChrono() {
        debutSession = Instant.now(); //Démarre le timer
    }

    public void pauseChrono() {
        // Calcule temps écoulé depuis debutSession en excluant les pauses 
        dureeAccumulee = dureeAccumulee.plus(Duration.between(debutSession, Instant.now()));
    }

    public void incrementerCoups() { nbCoups++; }
    
    public void calculerEtoiles() {
        long secondes = dureeAccumulee.getSeconds();
        if (secondes <= dureeTroisEtoiles && nbAidesUtilisees == 0) etoiles = 3; // Rapide + 0 aide → 3★
        else if (secondes <= dureDeuxEtoile && nbAidesUtilisees <= 2) etoiles = 2;
        else etoiles = 1;
    }
}
