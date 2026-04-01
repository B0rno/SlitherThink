package com.lmu.SlitherThink.Partie;

/**
 * Énumération représentant les différents états d'une partie.
 * 
 * 
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */

public enum EtatPartie {
    INIT, EN_COURS, PAUSE, TERMINE;

    /**
     * Vérifie si un coup peut être joué dans cet état.
     *
     * @return true uniquement si l'état est EN_COURS
     */
    public boolean peutJouer() {
        return this == EN_COURS;
    }

    /**
     * Vérifie si la partie peut être démarrée ou reprise.
     *
     * @return true si l'état est INIT ou PAUSE
     */
    public boolean peutDemarrer() {
        return this == INIT || this == PAUSE;
    }

    /**
     * Vérifie si la partie peut être mise en pause.
     *
     * @return true uniquement si l'état est EN_COURS
     */
    public boolean peutMettreEnPause() {
        return this == EN_COURS;
    }

}

