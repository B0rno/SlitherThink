package com.lmu.SlitherThink.save.structure;

import java.util.List;

public class PositionTrait {
    private List<Integer> position;
    private List<Integer> etat;

    public PositionTrait() {}

    private PositionTrait(List<Integer> position, List<Integer> etat) {
        this.position = position;
        this.etat = etat;
    }

    public static PositionTrait create(List<Integer> position, List<Integer> etat) {
        return new PositionTrait(position, etat);
    }

    public List<Integer> getPositionTrait() {
        return position;
    }

    public List<Integer> getEtatTrait() {
        return etat;
    }
}
