package com.lmu.SlitherThink.boutonsAction;

import java.util.ArrayList;
import java.util.List;

import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.PositionTrait;
import com.lmu.SlitherThink.save.structure.SaveGlobal;

public class SaveHelper {
	private static final SaveHelper instance = new SaveHelper();

	private SaveHelper() {}

	public static SaveHelper getInstance(){
		return SaveHelper.instance;
	}

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

    private static DetailleSavePartie buildDetail() {
        return DetailleSavePartie.create(
            new ArrayList<PositionTrait>(),
            0,
            0
        );
    }
}