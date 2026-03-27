package com.lmu.SlitherThink.save.gestionDonnee;

import java.util.List;

import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.SaveGlobal;

/**
 * Utilitaire de recherche de sauvegardes.
 * Permet de retrouver des sauvegardes de parties par ID ou par combinaison pseudo/chemin.
 * Gère à la fois les sauvegardes en mode libre et en mode aventure.
 */
public class rechercheSave {

    private static SaveGlobal saveGlobalCourant;

    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private rechercheSave() {
    }

    /**
     * Définit la sauvegarde globale courante utilisée par défaut dans les recherches.
     *
     * @param saveGlobal la sauvegarde globale à utiliser
     */
    public static void setSaveGlobalCourant(SaveGlobal saveGlobal) {
        saveGlobalCourant = saveGlobal;
    }

    /**
     * Recherche une sauvegarde par son identifiant unique.
     * Cherche d'abord dans les sauvegardes libres, puis dans les sauvegardes aventure.
     *
     * @param saveGlobal la sauvegarde globale dans laquelle chercher
     * @param idUtilisateur l'identifiant de la sauvegarde à trouver
     * @return la sauvegarde trouvée, ou null si aucune correspondance
     */
    public static savePartieLienJoueur chercherSauvegardeParId(SaveGlobal saveGlobal, int idUtilisateur) {
        if (saveGlobal == null) {
            return null;
        }

        savePartieLienJoueur sauvegarde = chercherSauvegardeDansListe(saveGlobal.getSauvegardeLibre(), idUtilisateur);
        if (sauvegarde != null) {
            return sauvegarde;
        }

        return chercherSauvegardeDansListe(saveGlobal.getSauvegardeAventure(), idUtilisateur);
    }

    /**
     * Trouve les détails d'une sauvegarde par son identifiant.
     *
     * @param saveGlobal la sauvegarde globale dans laquelle chercher
     * @param idUtilisateur l'identifiant de la sauvegarde
     * @return les détails de la sauvegarde, ou null si introuvable
     */
    public static DetailleSavePartie trouverDetailleSaveParId(SaveGlobal saveGlobal, int idUtilisateur) {
        savePartieLienJoueur sauvegarde = chercherSauvegardeParId(saveGlobal, idUtilisateur);
        if (sauvegarde == null) {
            return null;
        }
        return sauvegarde.getDetailleSave();
    }

    /**
     * Trouve l'identifiant d'une sauvegarde par pseudo et chemin de grille.
     *
     * @param saveGlobal la sauvegarde globale dans laquelle chercher
     * @param pseudo le pseudo du joueur
     * @param pathGrille le chemin de la grille
     * @return l'identifiant de la sauvegarde, ou null si introuvable
     */
    public static Integer trouverIdSauvegardeParPseudoEtPath(SaveGlobal saveGlobal, String pseudo, String pathGrille) {
        savePartieLienJoueur sauvegarde = trouverSauvegardeParPseudoEtPath(saveGlobal, pseudo, pathGrille);
        if (sauvegarde == null) {
            return null;
        }
        return sauvegarde.getId();
    }

    /**
     * Trouve l'identifiant d'une sauvegarde par pseudo et chemin de grille.
     * Utilise la sauvegarde globale courante définie par setSaveGlobalCourant.
     *
     * @param pseudo le pseudo du joueur
     * @param pathGrille le chemin de la grille
     * @return l'identifiant de la sauvegarde, ou null si introuvable
     */
    public static Integer trouverIdSauvegardeParPseudoEtPath(String pseudo, String pathGrille) {
        savePartieLienJoueur sauvegarde = trouverSauvegardeParPseudoEtPath(pseudo, pathGrille);
        if (sauvegarde == null) {
            return null;
        }
        return sauvegarde.getId();
    }

    /**
     * Trouve une sauvegarde par pseudo et chemin de grille.
     * Utilise la sauvegarde globale courante définie par setSaveGlobalCourant.
     *
     * @param pseudo le pseudo du joueur
     * @param pathGrille le chemin de la grille
     * @return la sauvegarde trouvée, ou null si introuvable
     */
    public static savePartieLienJoueur trouverSauvegardeParPseudoEtPath(String pseudo, String pathGrille) {
        return trouverSauvegardeParPseudoEtPath(saveGlobalCourant, pseudo, pathGrille);
    }

    /**
     * Trouve une sauvegarde par pseudo et chemin de grille.
     * Normalise le pseudo et le chemin avant la recherche pour gérer les variations de casse et de format.
     * Cherche d'abord dans les sauvegardes libres, puis dans les sauvegardes aventure.
     *
     * @param saveGlobal la sauvegarde globale dans laquelle chercher
     * @param pseudo le pseudo du joueur
     * @param pathGrille le chemin de la grille
     * @return la sauvegarde trouvée, ou null si introuvable
     */
    public static savePartieLienJoueur trouverSauvegardeParPseudoEtPath(SaveGlobal saveGlobal, String pseudo, String pathGrille) {
        if (saveGlobal == null || pseudo == null || pathGrille == null) {
            return null;
        }

        String pseudoNormalise = pseudo.trim();
        String pathNormalise = normaliserChemin(pathGrille);

        savePartieLienJoueur sauvegarde = trouverSauvegardeDansListe(saveGlobal.getSauvegardeLibre(), pseudoNormalise, pathNormalise);
        if (sauvegarde != null) {
            return sauvegarde;
        }

        return trouverSauvegardeDansListe(saveGlobal.getSauvegardeAventure(), pseudoNormalise, pathNormalise);
    }

    /**
     * Cherche une sauvegarde par ID dans une liste donnée.
     */
    private static savePartieLienJoueur chercherSauvegardeDansListe(List<savePartieLienJoueur> sauvegardes, int idUtilisateur) {
        if (sauvegardes == null) {
            return null;
        }

        for (savePartieLienJoueur sauvegarde : sauvegardes) {
            if (sauvegarde != null && sauvegarde.getId() != null && sauvegarde.getId().equals(idUtilisateur)) {
                return sauvegarde;
            }
        }
        return null;
    }

    /**
     * Cherche une sauvegarde par pseudo et chemin normalisé dans une liste donnée.
     */
    private static savePartieLienJoueur trouverSauvegardeDansListe(List<savePartieLienJoueur> sauvegardes, String pseudo, String pathNormalise) {
        if (sauvegardes == null) {
            return null;
        }

        for (savePartieLienJoueur sp : sauvegardes) {
            if (sp == null || sp.getId() == null || sp.getPseudo() == null || sp.getPath() == null) {
                continue;
            }

            if (sp.getPseudo().trim().equalsIgnoreCase(pseudo)
                && normaliserChemin(sp.getPath()).equals(pathNormalise)) {
                return sp;
            }
        }

        return null;
    }

    /**
     * Normalise un chemin de fichier en convertissant les backslash en slash,
     * supprimant les ./ initiaux et convertissant en minuscules.
     */
    private static String normaliserChemin(String path) {
        String normalise = path.replace('\\', '/').trim();
        while (normalise.startsWith("./")) {
            normalise = normalise.substring(2);
        }
        return normalise.toLowerCase();
    }
}
