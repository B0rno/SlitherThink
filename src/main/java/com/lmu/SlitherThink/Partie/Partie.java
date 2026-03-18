package com.lmu.SlitherThink.Partie;
import java.util.ArrayList;
import java.util.List;

import com.lmu.SlitherThink.Grille.Matrice;

/**
 * Gère une partie de jeu SlitherLink.
 *
 * Cette classe coordonne tous les aspects d'une session de jeu :
 * la grille (Matrice), le joueur (Profil), le score et l'état de la partie.
 *
 * @author Enzo Desfaudais (B0rno)
 * @version 1.0
 * @see PartieObserver
 * @see EtatPartie
 * @see Score
 */
public class Partie {

    private Profil p;
    private Matrice m;
    private int nbAides;
    private Score s; 
    private EtatPartie etat;
    private List<PartieObserver> observers;



    /**
     * Crée une nouvelle partie avec une grille vierge.
     *
     * @param p Le profil du joueur (ne doit pas être null)
     * @param hauteur Nombre de lignes de la grille
     * @param largeur Nombre de colonnes de la grille 
     * @throws IllegalArgumentException si p est null ou si hauteur/largeur ≤ 0
     */
    public Partie(Profil p, int hauteur, int largeur){
        this.p = p;
        this.m = new Matrice(hauteur, largeur);
        this.nbAides = 3; 
        this.s = new Score();
        this.etat = EtatPartie.INIT;
        this.observers = new ArrayList<>();
    }

    /**
     * Reconstruit une partie depuis une sauvegarde.
     *
     * @param p Le profil du joueur 
     * @param m La matrice restaurée 
     * @param nbAides Le nombre d'aides restantes 
     * @param s Le score restauré avec le chrono accumulé 
     * @throws IllegalArgumentException si un paramètre est null ou invalide
     */
    public Partie(Profil p, Matrice m, int nbAides, Score s){
        this.p = p;
        this.m = m;
        this.nbAides = nbAides;
        this.s = s;
        this.etat = EtatPartie.PAUSE; // chargée = en pause
        this.observers = new ArrayList<>();
    }




    /**
     * Démarre ou reprend la partie.
     * Passe l'état de INIT ou PAUSE vers EN_COURS et lance le chronomètre.
     * 
     * @throws IllegalStateException si l'état actuel ne permet pas de démarrer
     */
    public void demarrer() {
        //INIT ou pause -> EN_COURS
        if(!etat.peutDemarrer()) 
            throw new IllegalStateException("L'etat actuel (" + etat + ") ne permet pas de démarrer la partie.");
        etat = EtatPartie.EN_COURS;
        s.demarrerChrono();
        notifierEtatChange();
    }

    /**
     * Termine la partie après détection de la victoire.
     * Arrête le chronomètre, calcule les étoiles et notifie les observers.
     */
    private void terminerPartie() {
        etat = EtatPartie.TERMINE;
        s.arreterChrono();
        s.calculerEtoiles();
        p.ajouterScore(s);
        
        // Notifie TOUS les observers de la victoire avec le score final
        for (PartieObserver o : observers) {
            o.onVictoire(s);
        }    
    }

    /**
    * Joue un coup en modifiant l'état d'un trait de la grille.
    * @param ligne Indice de ligne de la case
    * @param colonne Indice de colonne de la case
    * @param direction Direction du trait (0=haut, 1=gauche, 2=droite, 3=bas)
    * @return true si le coup a été joué, false si refusé
    */
    public boolean jouerCoup(int ligne, int colonne, int direction){
        if(!etat.peutJouer()) return false;
        m.cliquer(ligne, colonne, direction);
        s.incrementerCoups();
        if (verifierVictoire()) terminerPartie();
        return true;
    }

    /**
     * Notifie tous les observers enregistrés du changement d'état actuel.
     */
    private void notifierEtatChange() {
        for (PartieObserver o : observers) {
            o.onEtatChange(etat);
        }
    }

    /**
     * Notifie tous les observers qu'une aide a été consommée.
     */    
    private void notifierAideUtilisee() {
        for (PartieObserver o : observers) {
            o.onAideUtilisee();
        }
    }

    /**
     * Enregistre un observer pour recevoir les notifications d'événements.
     * 
     * @param obs L'observer à ajouter
     */
    public void ajouterObserver(PartieObserver obs) {
        if (obs != null) observers.add(obs);
    }

    /**
     * Supprime un observer de la liste de notification.
     * 
     * @param obs L'observer à retirer
     */
    public void supprimerObserver(PartieObserver obs) {
        observers.remove(obs);
    }

    /**
     * Met la partie en pause.
     * Suspend le chronomètre et bloque les coups jusqu'à la reprise.
     * 
     * @throws IllegalStateException si l'état ne permet pas la mise en pause
     */
    public void mettreEnPause() {
        if (!etat.peutMettreEnPause()) {
            throw new IllegalStateException("Impossible de mettre en pause depuis " + etat);
        }
        etat = EtatPartie.PAUSE;
        s.pauseChrono();
        notifierEtatChange();
    }

    /**
     * Vérifie si le joueur peut utiliser une aide.
     * 
     * @return true si la partie est en cours et qu'il reste des aides
     */
    public boolean peutUtiliserAide() {
        return etat.peutJouer() && nbAides > 0;
    }
    /**
     * Utilise une aide pour révéler un trait de la solution.
     * 
     * @return true si l'aide a été utilisée, false sinon
     */
    public boolean utiliserAide() {
        if (!peutUtiliserAide()) return false;
        
        nbAides--;
        s.utiliserAide();
        notifierAideUtilisee();
        
        // TODO : Appeler le Helper ici 
        return true;
    }


    /**
     * Vérifie si tous les traits de la grille correspondent à la solution.
     * 
     * @return true si la partie est gagnée, false sinon
     */
    public boolean verifierVictoire(){
        return m.verifierVictoire();
    }

    /**
     * Retourne le profil du joueur.
     * 
     * @return le profil associé à cette partie
     */
    public Profil getProfil(){
        return p;
    }

    /**
     * Retourne la grille de jeu.
     * 
     * @return la matrice contenant l'état de la grille
     */
    public Matrice getMatrice(){
        return m;
    }

    /**
     * Retourne le nombre d'aides restantes.
     * 
     * @return le nombre d'aides disponibles
     */
    public int getNbAides(){
        return nbAides;
    }

    /**
     * Retourne le score de la partie.
     * 
     * @return le score avec temps, coups et étoiles
     */
    public Score getScore(){
        return s;
    }

    /**
     * Définit le nombre d'aides disponibles.
     * 
     * @param n le nouveau nombre d'aides
     */
    public void SetNbAides(int n){
        nbAides = n ;
    }
}
