package com.lmu.SlitherThink.boutonsAction;

import java.util.ArrayList;
import java.util.List;

import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.PositionTrait;
import com.lmu.SlitherThink.save.structure.SaveGlobal;

/**
 * Classe utilitaire facilitant la création et l'ajout de nouvelles sauvegardes.
 * Elle gère l'attribution des identifiants uniques et la structure initiale des fichiers.
 * @author Ilann
 */
public class SaveHelper {
    private static final SaveHelper instance = new SaveHelper();

    private SaveHelper() {}

    /** @return L'instance unique du SaveHelper (Singleton). */
    public static SaveHelper getInstance(){
        return SaveHelper.instance;
    }

    /**
     * Calcule l'identifiant suivant pour une nouvelle sauvegarde.
     * @param saveGlobal L'objet contenant l'ensemble des sauvegardes.
     * @return Le prochain identifiant entier disponible.
     */
    private static int prochainId(SaveGlobal saveGlobal) {
        int maxId = 0;
        if (saveGlobal == null) {
            return 1;
        }

        List<savePartieLienJoueur> toutes = new ArrayList<>();
        if (saveGlobal.getSauvegardeLibre() != null) {
            toutes.addAll(saveGlobal.getSauvegardeLibre());
        }
        if (saveGlobal.getSauvegardeAventure() != null) {
            toutes.addAll(saveGlobal.getSauvegardeAventure());
        }

        for (savePartieLienJoueur partie : toutes) {
            if (partie != null && partie.getId() != null && partie.getId() > maxId) {
                maxId = partie.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Crée et ajoute une nouvelle entrée de sauvegarde en mode Aventure.
     * @param save Le gestionnaire de chargement.
     * @param pseudo Le pseudo du joueur concerné.
     * @param nomGrille Le nom de la grille de jeu.
     */
    public static void ajouterPartieAventure(LoadSave save, String pseudo, String nomGrille) {
        Integer id = SaveHelper.prochainId(save.getSaveGlobal());
        savePartieLienJoueur partie = savePartieLienJoueur.create(
            pseudo,
            id,
            "./save/saveGrille/" + nomGrille + ".json"
        );
        partie.setDetailleSave(SaveHelper.buildDetail());
        save.getSaveGlobal().addSauvegardeAventure(partie);
        System.out.println("Ajout aventure -> pseudo=" + pseudo + ", id=");
        SaveManager saveManager = new SaveManager(save);
        saveManager.updateSaveFichierId(id);
    }

    /**
     * Crée et ajoute une nouvelle entrée de sauvegarde en mode Libre.
     * @param save Le gestionnaire de chargement.
     * @param pseudo Le pseudo du joueur concerné.
     * @param nomGrille Le nom de la grille de jeu.
     */
    public static void ajouterPartieLibre(LoadSave save, String pseudo, String nomGrille) {
        Integer id = SaveHelper.prochainId(save.getSaveGlobal());
        savePartieLienJoueur partie = savePartieLienJoueur.create(
            pseudo,
            id,
            "./save/saveGrille/" + nomGrille + ".json"
        );
        partie.setDetailleSave(SaveHelper.buildDetail());
        save.getSaveGlobal().addSauvegardeLibre(partie);
        System.out.println("Ajout libre -> pseudo=" + pseudo + ", id=");
        SaveManager saveManager = new SaveManager(save);
        saveManager.updateSaveFichierId(id);
    }

    /**
     * Construit un objet de détail de sauvegarde initial (vide).
     * @return Un nouvel objet DetailleSavePartie initialisé par défaut.
     */
    private static DetailleSavePartie buildDetail() {
        return DetailleSavePartie.create(
            new ArrayList<PositionTrait>(),
            0,
            0
        );
    }
}