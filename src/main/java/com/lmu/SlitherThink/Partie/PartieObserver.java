package com.lmu.SlitherThink.Partie;

/**
 * Utilisation du pattern Observer pour notifier les changements d'état de la partie aux composants intéressés (ex: interface utilisateur).
 * 
 *  
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 */

public interface PartieObserver {

    void onVictoire(Score score);        // Appelé quand la partie est gagnée
    void onEtatChange(EtatPartie etat);  // Appelé quand changement d'état de la partie
    void onAideUtilisee(int restantes);  // Appelé quand le joueur utilise une aide

}
