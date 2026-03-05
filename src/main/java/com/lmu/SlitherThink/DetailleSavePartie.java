package com.lmu.SlitherThink;

import java.util.List;

class DetailleSavePartie {
    private List<positionTrait> etatgrille;
    private Integer Chronometre;
    private Integer nbAides;
    private transient String nameClass = null;

    public List<positionTrait> getEtatGrille() {
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
