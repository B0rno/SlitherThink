package com.lmu.SlitherThink.save.structure;

import java.util.ArrayList;
import java.util.List;

import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;

/**
 * Classe représentant les sauvegardes globales.
 * Contient les sauvegardes en mode libre et en mode aventure.
 */
public class SaveGlobal {
    private List<savePartieLienJoueur> SauvegardeLibre; // Liste des sauvegardes libres
    private List<savePartieLienJoueur> SauvegardeAventure; // Liste des sauvegardes aventure

    /**
     * Retourne la liste des sauvegardes libres.
     *
     * @return Une liste de sauvegardes libres.
     */
    public List<savePartieLienJoueur> getSauvegardeLibre() {
        return SauvegardeLibre;
    }

    /**
     * Retourne la liste des sauvegardes aventure.
     *
     * @return Une liste de sauvegardes aventure.
     */
    public List<savePartieLienJoueur> getSauvegardeAventure() {
        return SauvegardeAventure;
    }

    /**
     * Ajoute une sauvegarde au mode aventure.
     *
     * @param partie La sauvegarde à ajouter.
     */
    public void addSauvegardeAventure(savePartieLienJoueur partie) {
        if (partie == null) {
            return;
        }
        if (SauvegardeAventure == null) {
            SauvegardeAventure = new ArrayList<>();
        }
        SauvegardeAventure.add(partie);
    }

    /**
     * Ajoute une sauvegarde au mode libre.
     *
     * @param partie La sauvegarde à ajouter.
     */
    public void addSauvegardeLibre(savePartieLienJoueur partie) {
        if (partie == null) {
            return;
        }
        if (SauvegardeLibre == null) {
            SauvegardeLibre = new ArrayList<>();
        }
        SauvegardeLibre.add(partie);
    }
}
