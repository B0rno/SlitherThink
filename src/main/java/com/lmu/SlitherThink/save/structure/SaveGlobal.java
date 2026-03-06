package com.lmu.SlitherThink.save.structure;

import java.util.List;

import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;

public class SaveGlobal {
    private List<savePartieLienJoueur> SauvegardeLibre;
    private List<savePartieLienJoueur> SauvegardeAventure;

    public List<savePartieLienJoueur> getSauvegardeLibre() {
        return SauvegardeLibre;
    }

    public List<savePartieLienJoueur> getSauvegardeAventure() {
        return SauvegardeAventure;
    }
}
