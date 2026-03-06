package com.lmu.SlitherThink.Partie;

public enum EtatPartie {
    INIT, EN_COURS, PAUSE, TERMINE;
    
    public boolean peutJouer() { 
        return this == EN_COURS; 
    }

    public boolean peutDemarrer() { 
        return this == INIT || this == PAUSE; 
    }

    public boolean peutMettreEnPause() { 
        return this == EN_COURS; 
    }
    
}

