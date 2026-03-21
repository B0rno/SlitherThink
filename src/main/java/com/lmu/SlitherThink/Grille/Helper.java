package com.lmu.SlitherThink.Grille;

public class Helper {
    
    /**
     * Parcourt la matrice pour trouver les cases '0' et met leurs traits sur croix.
     * * @param m La matrice du jeu
     * @return
     */
    public void CroixSurZeros(Matrice m) {

        int i,j,x;

        for (i=0; i < m.getHauteur(); i++) {
            for (j = 0; j < m.getLargeur(); j++) {

                Case c = m.getCase(i,j);
                
                if (c.getNumero() == 0) {
                    
                    // On test sur les 4 directions
                    for (x = 0; x < 4; x++) {
                        Trait t = c.getTrait(x);
                        
                        if (t.getEtat() == ValeurTrait.VIDE) {
                            t.setTrait(ValeurTrait.CROIX);
                        }
                    }
                }
            }
        }
    }



    /**
     * Parcourt la matrice pour trouver les cases '3' adjacente avec un zero et mettre des croix sur les trois directions possibles
     * * @param m La matrice du jeu
     * @return
     */

    public void CroixSurTrois(Matrice m) {

        int i,j;
        Trait t;

        for (i=0; i < m.getHauteur(); i++) {
            for (j = 0; j < m.getLargeur(); j++) {

                Case c = m.getCase(i,j);
                
                if (c.getNumero() == 3) {

                    Case c1 = m.getCase(i+1, j);
                    Case c2 = m.getCase(i-1, j);
                    Case c3 = m.getCase(i, j+1);
                    Case c4 = m.getCase(i, j-1);

                    // la case de droite est un '0' donc on doit mettre des traits sur toutes les autres direction
                   if (c1.getNumero() == 0){
                        t = c.getTrait(0);
                        t.setTrait(ValeurTrait.PLEIN);

                        t = c.getTrait(0);
                        t.setTrait(ValeurTrait.PLEIN);
                   }

                   // la case de gauche est un '0' donc on doit mettre des traits sur toutes les autres direction
                   if (c2.getNumero() == 0){
                        t = c.getTrait(0);
                        t.setTrait(ValeurTrait.PLEIN);

                        t = c.getTrait(2);
                        t.setTrait(ValeurTrait.PLEIN);

                        t = c.getTrait(3);
                        t.setTrait(ValeurTrait.PLEIN);
                   }

                    // la case du bas est un '0' donc on doit mettre des traits sur toutes les autres direction
                   if (c3.getNumero() == 0){
                        t = c.getTrait(0);
                        t.setTrait(ValeurTrait.PLEIN);

                        t = c.getTrait(1);
                        t.setTrait(ValeurTrait.PLEIN);

                        t = c.getTrait(2);
                        t.setTrait(ValeurTrait.PLEIN);
                   }

                   // la case du haut est un '0' donc on doit mettre des traits sur toutes les autres direction
                   if (c4.getNumero() == 0){
                        t = c.getTrait(4);
                        t.setTrait(ValeurTrait.PLEIN);

                        t = c.getTrait(1);
                        t.setTrait(ValeurTrait.PLEIN);

                        t = c.getTrait(2);
                        t.setTrait(ValeurTrait.PLEIN);
                   }

                   
                }
            }
        }
    }

}