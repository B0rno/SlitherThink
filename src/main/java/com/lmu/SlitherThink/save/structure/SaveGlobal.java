package com.lmu.SlitherThink.save.structure;

import java.util.ArrayList;
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

    public void addSauvegardeAventure(savePartieLienJoueur partie) {
        if (partie == null) {
            return;
        }
        if (SauvegardeAventure == null) {
            SauvegardeAventure = new ArrayList<>();
        }
        SauvegardeAventure.add(partie);
    }

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
