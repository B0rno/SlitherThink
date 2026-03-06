package com.lmu.SlitherThink.save.structure;

import java.util.List;

public class DetailleSavePartie {
    private List<PositionTrait> etatgrille;
    private Integer Chronometre;
    private Integer nbAides;
    private transient String nameClass = null;

    public DetailleSavePartie() {}

    private DetailleSavePartie(List<PositionTrait> etatgrille, Integer chronometre, Integer nbAides) {
        this.etatgrille = etatgrille;
        this.Chronometre = chronometre;
        this.nbAides = nbAides;
    }

    public static DetailleSavePartie create(List<PositionTrait> etatgrille, Integer chronometre, Integer nbAides) {
        return new DetailleSavePartie(etatgrille, chronometre, nbAides);
    }

    public List<PositionTrait> getEtatGrille() {
        return etatgrille;
    }

    public Integer getChronometre() {
        return Chronometre;
    }

    public Integer getNbAides() {
        return nbAides;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }
}
