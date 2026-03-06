package com.lmu.SlitherThink.save.structure;

import java.util.List;

public class DetailleSavePartie {
    private List<PositionTrait> etatgrille;
    private Integer Chronometre;
    private Integer nbAides;
    private transient String nameClass = null;

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
