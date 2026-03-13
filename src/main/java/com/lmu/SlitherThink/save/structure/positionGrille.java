package com.lmu.SlitherThink.save.structure;

import java.util.List;

public class positionGrille {
    private List<Integer> position;
    private int valeur;

    public List<Integer> getPositionGrille() {
        return position;
    }

    public int getValeurGrille() {
        return valeur;
    }

    @Override
    public String toString() {
        return 
        "positionGrille{" +
            "position=" + position +
            ", valeur=" + valeur +
        '}';
    }
}
