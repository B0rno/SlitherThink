package com.lmu.SlitherThink.save.gestionDonnee;

import java.util.List;

import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.SaveGlobal;

public class rechercheSave {

    private static SaveGlobal saveGlobalCourant;

    private rechercheSave() {
    }

    public static void setSaveGlobalCourant(SaveGlobal saveGlobal) {
        saveGlobalCourant = saveGlobal;
    }

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

    public static DetailleSavePartie trouverDetailleSaveParId(SaveGlobal saveGlobal, int idUtilisateur) {
        savePartieLienJoueur sauvegarde = chercherSauvegardeParId(saveGlobal, idUtilisateur);
        if (sauvegarde == null) {
            return null;
        }
        return sauvegarde.getDetailleSave();
    }

    public static Integer trouverIdSauvegardeParPseudoEtPath(SaveGlobal saveGlobal, String pseudo, String pathGrille) {
        savePartieLienJoueur sauvegarde = trouverSauvegardeParPseudoEtPath(saveGlobal, pseudo, pathGrille);
        if (sauvegarde == null) {
            return null;
        }
        return sauvegarde.getId();
    }

    public static Integer trouverIdSauvegardeParPseudoEtPath(String pseudo, String pathGrille) {
        savePartieLienJoueur sauvegarde = trouverSauvegardeParPseudoEtPath(pseudo, pathGrille);
        if (sauvegarde == null) {
            return null;
        }
        return sauvegarde.getId();
    }

    public static savePartieLienJoueur trouverSauvegardeParPseudoEtPath(String pseudo, String pathGrille) {
        return trouverSauvegardeParPseudoEtPath(saveGlobalCourant, pseudo, pathGrille);
    }

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

    private static String normaliserChemin(String path) {
        String normalise = path.replace('\\', '/').trim();
        while (normalise.startsWith("./")) {
            normalise = normalise.substring(2);
        }
        return normalise.toLowerCase();
    }
}
