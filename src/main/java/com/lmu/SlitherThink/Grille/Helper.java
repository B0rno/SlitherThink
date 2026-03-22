package com.lmu.SlitherThink.Grille;

public class Helper {
    
    /**
     * Parcourt la matrice pour vérifier s'il y a des cases '0'.
     * L'aide est applicable s'il manque encore des croix sur les traits autour du '0' 
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    public boolean CroixSurZeros(Matrice m) {

        int i,j,x;

        for (i=0; i < m.getHauteur(); i++) {
            for (j = 0; j < m.getLargeur(); j++) {

                Case c = m.getCase(i,j);
                
                if (c.getNumero() == 0) {
                    
                    // On test sur les 4 directions
                    for (x = 0; x < 4; x++) {
                        Trait t = c.getTrait(x);
                        
                        if (t.getEtat() == ValeurTrait.VIDE) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }



    /**
     * Parcourt la matrice pour vérifier s'il y a un '3' adjacent à un '0'.
     * L'aide est applicable s'il manque encore des traits sur les trois autres côtés de la case '3' qui ne touchent pas le '0'.
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    public boolean CroixSurTrois(Matrice m) {

        int i,j;

        for (i=0; i < m.getHauteur(); i++) {
            for (j = 0; j < m.getLargeur(); j++) {

                Case c = m.getCase(i,j);
                
                if (c.getNumero() == 3) {

                    Case c1 = m.getCase(i+1, j);
                    Case c2 = m.getCase(i-1, j);
                    Case c3 = m.getCase(i, j+1);
                    Case c4 = m.getCase(i, j-1);

                    // la case du bas est un '0' 
                   if (c1 != null && c1.getNumero() == 0){
                        if (c.getTrait(0).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(1).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(2).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }

                   // la case du haut est un '0' 
                   if (c2 != null && c2.getNumero() == 0){
                        if (c.getTrait(3).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(1).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(2).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }

                    // la case de droite est un '0' 
                   if (c3 != null && c3.getNumero() == 0){
                        if (c.getTrait(0).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(1).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(3).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }

                   // la case de gauche est un '0' 
                   if (c4 != null && c4.getNumero() == 0){
                        if (c.getTrait(0).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(2).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(3).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }

                   
                }
            }
        }
        return false;
    }



    /**
     * Parcourt la matrice pour vérifier s'il y a un '3' en diagonale d'un '0'.
     * L'aide est applicable s'il manque encore des traits sur les côtés opposés au '0'.
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    public boolean TroisDiagonaleZero(Matrice m) {

        int i,j;

        for (i=0; i < m.getHauteur(); i++) {
            for (j = 0; j < m.getLargeur(); j++) {

                Case c = m.getCase(i,j);
                
                if (c.getNumero() == 3) {

                    Case cDiagHG = m.getCase(i-1, j-1); 
                    Case cDiagHD = m.getCase(i-1, j+1); 
                    Case cDiagBG = m.getCase(i+1, j-1);
                    Case cDiagBD = m.getCase(i+1, j+1); 

                    // Le '0' est en Haut-Gauche, les traits opposés au '0' sur la case '3' sont Droite (2) et Bas (3)
                   if (cDiagHG != null && cDiagHG.getNumero() == 0){
                        if (c.getTrait(2).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(3).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }

                   // Le '0' est en Haut-Droite, les traits opposés au '0' sur la case '3' sont Gauche (1) et Bas (3)
                   if (cDiagHD != null && cDiagHD.getNumero() == 0){
                        if (c.getTrait(1).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(3).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }

                   // Le '0' est en Bas-Gauche, les traits opposés au '0' sur la case '3' sont Droite (2) et Haut (0)
                   if (cDiagBG != null && cDiagBG.getNumero() == 0){
                        if (c.getTrait(2).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(0).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }

                   // Le '0' est en Bas-Droite, les traits opposés au '0' sur la case '3' sont Gauche (1) et Haut (0)
                   if (cDiagBD != null && cDiagBD.getNumero() == 0){
                        if (c.getTrait(1).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(0).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }
                   
                }
            }
        }
        return false;
    }



    /**
     * Parcourt la matrice pour vérifier s'il y a deux '3' adjacents.
     * L'aide est applicable s'il manque encore des traits parallèles sur et à l'opposé de leur côté partagé.
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    public boolean DeuxTroisAdjacents(Matrice m) {

        int i,j;

        for (i=0; i < m.getHauteur(); i++) {
            for (j = 0; j < m.getLargeur(); j++) {

                Case c = m.getCase(i,j);
                
                if (c.getNumero() == 3) {

                    Case cHaut = m.getCase(i-1, j);
                    Case cBas = m.getCase(i+1, j);
                    Case cGauche = m.getCase(i, j-1);
                    Case cDroite = m.getCase(i, j+1);

                    // Si le '3' a un voisin '3' à gauche ou à droite les lignes verticaux doivent être pleines
                   if ((cGauche != null && cGauche.getNumero() == 3) || 
                       (cDroite != null && cDroite.getNumero() == 3)){
                        // Traits verticaux : Gauche (1) et Droite (2)
                        if (c.getTrait(1).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(2).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }

                   // Si le '3' a un voisin '3' en haut ou en bas les lignes horizontales doivent être pleines
                   if ((cHaut != null && cHaut.getNumero() == 3) || 
                       (cBas != null && cBas.getNumero() == 3)){
                        // Traits horizontaux : Haut (0) et Bas (3)
                        if (c.getTrait(0).getEtat() == ValeurTrait.VIDE || 
                            c.getTrait(3).getEtat() == ValeurTrait.VIDE) {
                                return true;
                        }
                   }
                   
                }
            }
        }
        return false;
    }

    /**
     * Vérifie s'il y a un chiffre 1, 2 ou 3 dans un des 4 coins de la grille.
     * L'aide est applicable s'il manque des traits ou croix sur les bords du coin
     * @param m La matrice du jeu
     * @return true si l'aide est applicable
     */
    public boolean ChiffreDansCoin(Matrice m) {
        
        int h = m.getHauteur();
        int w = m.getLargeur();
        
        //Coin Haut-Gauche
        Case c1 = m.getCase(0, 0);
        if (c1 != null) {
            int num = c1.getNumero();
            // Pour 1 et 3
            if (num == 1 || num == 3) {
                if (c1.getTrait(0).getEtat() == ValeurTrait.VIDE || 
                    c1.getTrait(1).getEtat() == ValeurTrait.VIDE) {
                    return true;
                }
            }
            // Pour 2
            else if (num == 2) {
                Case cDroite = m.getCase(0, 1);
                Case cBas = m.getCase(1, 0);
                if ((cDroite != null && cDroite.getTrait(0).getEtat() == ValeurTrait.VIDE) ||
                    (cBas != null && cBas.getTrait(1).getEtat() == ValeurTrait.VIDE)) {
                    return true;
                }
            }
        }

        // Coin Haut-Droite
        Case c2 = m.getCase(0, w - 1);
        if (c2 != null) {
            int num = c2.getNumero();
            // Pour 1 et 3 
            if (num == 1 || num == 3) {
                if (c2.getTrait(0).getEtat() == ValeurTrait.VIDE || 
                    c2.getTrait(2).getEtat() == ValeurTrait.VIDE) {
                    return true;
                }
            }
            // Pour 2 
            else if (num == 2) {
                Case cGauche = m.getCase(0, w - 2);
                Case cBas = m.getCase(1, w - 1);
                if ((cGauche != null && cGauche.getTrait(0).getEtat() == ValeurTrait.VIDE) ||
                    (cBas != null && cBas.getTrait(2).getEtat() == ValeurTrait.VIDE)) {
                    return true;
                }
            }
        }

        // Coin Bas-Gauche
        Case c3 = m.getCase(h - 1, 0);
        if (c3 != null) {
            int num = c3.getNumero();
            // Pour 1 et 3
            if (num == 1 || num == 3) {
                if (c3.getTrait(3).getEtat() == ValeurTrait.VIDE || 
                    c3.getTrait(1).getEtat() == ValeurTrait.VIDE) {
                    return true;
                }
            }
            // Pour 2
            else if (num == 2) {
                Case cDroite = m.getCase(h - 1, 1);
                Case cHaut = m.getCase(h - 2, 0);
                if ((cDroite != null && cDroite.getTrait(3).getEtat() == ValeurTrait.VIDE) ||
                    (cHaut != null && cHaut.getTrait(1).getEtat() == ValeurTrait.VIDE)) {
                    return true;
                }
            }
        }

        // Coin Bas-Droite
        Case c4 = m.getCase(h - 1, w - 1);
        if (c4 != null) {
            int num = c4.getNumero();
            // Pour 1 et 3 
            if (num == 1 || num == 3) {
                if (c4.getTrait(3).getEtat() == ValeurTrait.VIDE || 
                    c4.getTrait(2).getEtat() == ValeurTrait.VIDE) {
                    return true;
                }
            }
            // Pour 2 
            else if (num == 2) {
                Case cGauche = m.getCase(h - 1, w - 2);
                Case cHaut = m.getCase(h - 2, w - 1);
                if ((cGauche != null && cGauche.getTrait(3).getEtat() == ValeurTrait.VIDE) ||
                    (cHaut != null && cHaut.getTrait(2).getEtat() == ValeurTrait.VIDE)) {
                    return true;
                }
            }
        }

        return false;
    }

}   