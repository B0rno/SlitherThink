package com.lmu.SlitherThink.Grille;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import com.lmu.SlitherThink.save.gestionDonnee.rechercheSave;
import com.lmu.SlitherThink.save.gestionDonnee.savePartieLienJoueur;
import com.lmu.SlitherThink.save.structure.DetailleSavePartie;
import com.lmu.SlitherThink.save.structure.PositionTrait;
import com.lmu.SlitherThink.save.structure.SaveGrille;
import com.lmu.SlitherThink.save.structure.positionGrille;

/**
 * Représente une matrice de cases pour le jeu SlitherLink.
 * 
 * La matrice gère un plateau rectangulaire de cases où les traits sont partagés
 * entre les cases adjacentes. Par exemple, le trait droit d'une case est le même
 * objet que le trait gauche de sa voisine à droite.
 * 
 * Structure des indices :
 * - 0 : haut (top)
 * - 1 : gauche (left)
 * - 2 : droite (right)
 * - 3 : bas (bottom)
 * 
 * @author Natp24109
 * @version 1.0
 */

public class Matrice {
    /** Nombre de lignes de la matrice */
    private final int hauteur;
    
    /** Nombre de colonnes de la matrice */
    private final int largeur;
    
    /** Matrice bidimensionnelle des cases */
    private Case[][] grille;
    
    /** Matrice des traits horizontaux (partagés entre haut/bas des cases) */
    private Trait[][] traitsHorizontaux;
    
    /** Matrice des traits verticaux (partagés entre gauche/droite des cases) */
    private Trait[][] traitsVerticaux;

    /** Matrice des traits horizontaux solution */
    private Trait[][] traitsHorizontauxSol;

    /** Matrice des traits verticaux solution */
    private Trait[][] traitsVerticauxSol;

    /** Compteur de traits dans le bon état */
    private int cpt;

    /** Booleen donnant la complétion de la grille */
    private boolean completed;

    /**
     * Constructeur de la matrice.
     * Initialise une matrice de cases de dimensions hauteur x largeur.
     * Les traits sont automatiquement créés et partagés entre les cases adjacentes.
     * 
     * @param hauteur le nombre de lignes de cases
     * @param largeur le nombre de colonnes de cases
     */
    public Matrice(int hauteur, int largeur) {
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.grille = new Case[hauteur][largeur];

        this.cpt = 0;
        this.completed = false;
        
        // Créer les matrices de traits
        // traitsHorizontaux : (hauteur+1) x largeur
        this.traitsHorizontaux = new Trait[hauteur + 1][largeur];
        // traitsVerticaux : hauteur x (largeur+1)
        this.traitsVerticaux = new Trait[hauteur][largeur + 1];

        this.traitsHorizontauxSol = new Trait[hauteur + 1][largeur];
        this.traitsVerticauxSol = new Trait[hauteur][largeur + 1];
        
        // Initialiser tous les traits
        for (int i = 0; i <= hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                traitsHorizontaux[i][j] = new Trait();
                traitsHorizontauxSol[i][j] = new Trait();
            }
        }
        
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j <= largeur; j++) {
                traitsVerticaux[i][j] = new Trait();
                traitsVerticauxSol[i][j] = new Trait();
            }
        }
        
        // Créer les cases et assigner les traits partagés
        int numero = -1;
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                grille[i][j] = new Case(numero);
                
                // Assigner les traits à la case
                Trait[] traits = new Trait[4];
                traits[0] = traitsHorizontaux[i][j];            // haut
                traits[1] = traitsVerticaux[i][j];              // gauche
                traits[2] = traitsVerticaux[i][j + 1];          // droite
                traits[3] = traitsHorizontaux[i + 1][j];        // bas
                
                grille[i][j].setTraits(traits);
            }
        }
    }

    /**
     * Retourne la case à la position spécifiée.
     * 
     * @param ligne l'indice de ligne (0-indexed)
     * @param colonne l'indice de colonne (0-indexed)
     * @return la case à cette position, ou null si les indices sont invalides
     */
    public Case getCase(int ligne, int colonne) {
        if (ligne < 0 || ligne >= hauteur || colonne < 0 || colonne >= largeur) {
            return null;
        }
        return grille[ligne][colonne];
    }

    public Trait getTraitHorizSolution(int ligne, int colonne) {
        return this.traitsHorizontauxSol[ligne][colonne];
    }

    public Trait getTraitVertiSolution(int ligne, int colonne) {
        return this.traitsVerticauxSol[ligne][colonne];
    }

    /**
     * Retourne la hauteur de la matrice.
     * 
     * @return le nombre de lignes
     */
    public int getHauteur() {
        return hauteur;
    }

    /**
     * Retourne la largeur de la matrice.
     * 
     * @return le nombre de colonnes
     */
    public int getLargeur() {
        return largeur;
    }

    /**
     * Charge la solution dans la matrice.
     * Définit les états des traits solution pour tous les traits.
     */
    public static Matrice loadGrille(String key){
        LoadSave save = LoadSave.getInstance(null);


        Map<String, SaveGrille> toutesLesGrilles = save.getGrilles();
    
        if (toutesLesGrilles == null || !toutesLesGrilles.containsKey(key)) {
            System.err.println("ERREUR : La grille '" + key + "' est introuvable.");
            // Petit debug pour voir ce qui est chargé
            if(toutesLesGrilles != null) System.out.println("Grilles dispos : " + toutesLesGrilles.keySet());
            return null; 
        }

        SaveGrille grid = save.getGrilles().get(key);
        if (grid == null) {
            System.err.println("La grille '" + key + "' n'a pas été trouvée dans le dossier GrilleJson.");
            return null; 
        }
        int hauteur = grid.getTailleGrille();

        Matrice loadedMatrice = new Matrice(hauteur, hauteur);

        for(positionGrille pos : grid.getNumeroCases()){
            List<Integer> coord = pos.getPositionGrille();
            loadedMatrice.getCase(coord.get(0), coord.get(1)).setNumero(pos.getValeurGrille());
        }

        for(PositionTrait pos : grid.getListePositionTrait()){
            List<Integer> coord = pos.getPositionTrait();
            List<Integer> etat = pos.getEtatTrait();
            
            int ligne = coord.get(0);
            int colonne = coord.get(1);
            
            // Pour chaque direction qui fait partie de la solution
            for(Integer direction : etat) {
                switch(direction) {
                    case 0: // Trait haut
                        loadedMatrice.getTraitHorizSolution(ligne, colonne).setTrait(ValeurTrait.PLEIN);
                        break;
                    case 1: // Trait gauche
                        loadedMatrice.getTraitVertiSolution(ligne, colonne).setTrait(ValeurTrait.PLEIN);
                        break;
                    case 2: // Trait droite
                        loadedMatrice.getTraitVertiSolution(ligne, colonne + 1).setTrait(ValeurTrait.PLEIN);
                        break;
                    case 3: // Trait bas
                        loadedMatrice.getTraitHorizSolution(ligne + 1, colonne).setTrait(ValeurTrait.PLEIN);
                        break;
                }
            }
        }

        loadedMatrice.compterTraitsValides();
        return loadedMatrice;
    }

    public boolean loadSave(String pseudo, String path, boolean saveAventure){
        LoadSave save = LoadSave.getInstance("");

        // Recharger le SaveGlobal depuis la mémoire pour récupérer les dernières modifications
        save.rechargerSaveGlobal();

        savePartieLienJoueur saveJoueur = rechercheSave.trouverSauvegardeParPseudoEtPath(
            save.getSaveGlobal(),
            pseudo,
            path
        );

        List<PositionTrait> detail = null;
        if (saveJoueur != null) {
            // Forcer le rechargement du détail depuis la mémoire
            saveJoueur.reloadDetailleSave();

            if (saveJoueur.getDetailleSave() != null) {
                detail = saveJoueur.getDetailleSave().getEtatGrille();
            }
        }

        if(detail != null){
            for(PositionTrait pos : detail){
                List<Integer> coord = pos.getPositionTrait();
                List<Integer> etat = pos.getEtatTrait();
                
                int ligne = coord.get(0);
                int colonne = coord.get(1);
                System.out.println("l:" + ligne + "c:" + colonne + "e:" + etat);
                
                for(Integer direction : etat){
                    if(this.getCase(ligne,colonne).getTrait(direction).getEtat() == ValeurTrait.VIDE){
                        this.cliquer(ligne, colonne, direction);
                        System.out.println("ca marche !");
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void saveGrille(String pseudo, String path, boolean saveAventure, int l, int c, int direction){
        if(this.getCase(l,c).getTrait(direction).getEtat() == ValeurTrait.CROIX)
            return; //La sauvegarde des croix n'est pas implémenté dans les json

        LoadSave save = LoadSave.getInstance("");

        // Recharger SaveGlobal pour avoir les données à jour
        save.rechargerSaveGlobal();

        savePartieLienJoueur saveJoueur = rechercheSave.trouverSauvegardeParPseudoEtPath(
            save.getSaveGlobal(),
            pseudo,
            path
        );
        if (saveJoueur == null) {
            //System.err.println("Aucune sauvegarde trouvée pour pseudo/path: " + pseudo + " / " + path);
            return;
        }

        Integer id = saveJoueur.getId();
        if (id == null) {
            //System.err.println("Sauvegarde trouvée sans id: " + pseudo + " / " + path);
            return;
        }

        DetailleSavePartie detailSave = saveJoueur.getDetailleSave();
        if (detailSave == null) {
            detailSave = DetailleSavePartie.create(new ArrayList<>(), 0, 0);
            saveJoueur.setDetailleSave(detailSave);
        }

        List<PositionTrait> detail = detailSave.getEtatGrille();
        if (detail == null) {
            detail = new ArrayList<>();
            DetailleSavePartie nouveauDetail = DetailleSavePartie.create(
                detail,
                detailSave.getChronometre(),
                detailSave.getNbAides()
            );
            nouveauDetail.setNameClass(detailSave.getNameClass());
            saveJoueur.setDetailleSave(nouveauDetail);
        }

        if(detail != null){
            boolean caseExistante = false;
            for(PositionTrait pos : detail){
                List<Integer> coord = pos.getPositionTrait();
                List<Integer> etat = pos.getEtatTrait();
                
                int ligne = coord.get(0);
                int colonne = coord.get(1);
                
                if(ligne == l && colonne == c){
                    if(etat.contains(direction))
                        etat.remove(Integer.valueOf(direction));
                    else
                        etat.add(direction);
                    caseExistante = true;
                    break;
                }
            }

            if(!caseExistante) {
                List<Integer> positions = new ArrayList<>(2);
                positions.add(l);
                positions.add(c);
                List<Integer> etats = new ArrayList<>(4);
                etats.add(direction);
                detail.add(PositionTrait.create(positions, etats));
            }
        }
        SaveManager saveManager = new SaveManager(save);
        saveManager.updateSaveFichierId(id); 
    }

    /**
     * Compte le nombre total de traits valides dans la matrice.
     * Un trait est valide si son état actuel correspond à la solution.
     * 
     * @return le nombre de traits valides
     */
    private int compterTraitsValides() {
        int total = 0;
        
        // Vérifier les traits horizontaux
        for (int i = 0; i <= hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (traitsHorizontaux[i][j].getEtat() == traitsHorizontauxSol[i][j].getEtat() ||
                    (traitsHorizontaux[i][j].getEtat() == ValeurTrait.CROIX && 
                     traitsHorizontauxSol[i][j].getEtat() != ValeurTrait.PLEIN)) {
                    total++;
                }
            }
        }
        
        // Vérifier les traits verticaux
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j <= largeur; j++) {
                if (traitsVerticaux[i][j].getEtat() == traitsVerticauxSol[i][j].getEtat() ||
                    (traitsVerticaux[i][j].getEtat() == ValeurTrait.CROIX && 
                     traitsVerticauxSol[i][j].getEtat() != ValeurTrait.PLEIN)) {
                    total++;
                }
            }
        }
        
        this.cpt = total;
        return total;
    }

    /**
     * Modifie l'état d'un trait d'une case.
     * 
     * @param ligne l'indice de ligne de la case (0-indexed)
     * @param colonne l'indice de colonne de la case (0-indexed)
     * @param direction l'index du trait (0=haut, 1=gauche, 2=droite, 3=bas)
     */
    public void cliquer(int ligne, int colonne, int direction){
        this.grille[ligne][colonne].updateTrait(direction);
        this.cpt = this.compterTraitsValides();
        if(this.cpt == (hauteur + 1) * largeur + hauteur * (largeur + 1)) {
            System.out.println("GAGNE !!!!!!!!!!!!!!!!!!!");
            this.completed = true;
        }
    }

    /**
     * Vérifie la complétion de la grille.
     * 
     * @return un booleen donnant l'information de complétion
     */
    public boolean verifierVictoire(){
        return this.completed;
    }

    /**
     * Retourne une représentation textuelle de la matrice.
     * Affiche l'état actuel de toutes les cases avec les traits correctement positionnés.
     * 
     * @return une chaîne représentant visuellement la matrice
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < hauteur; i++) {
            // Ligne des traits horizontaux du haut
            for (int j = 0; j < largeur; j++) {
                sb.append(" ");
                sb.append(getStringOfTraitHorizontal(i, j, 0)); // trait haut
            }
            sb.append("\n");
            
            // Ligne des cases avec traits verticaux
            for (int j = 0; j < largeur; j++) {
                sb.append(getStringOfTraitVertical(i, j, 1));   // trait gauche
                if(grille[i][j].getNumero() != -1)
                    sb.append(String.format("[%d]", grille[i][j].getNumero()));
                else sb.append("[ ]");
            }
            sb.append(getStringOfTraitVertical(i,largeur-1,2)); // trait droit de la dernière case
            sb.append("\n");
        }
        
        // Dernière ligne des traits horizontaux du bas
        for (int j = 0; j < largeur; j++) {
            sb.append(" ");
            sb.append(getStringOfTraitHorizontal(hauteur - 1, j, 3)); // trait bas
        }
        
        int totalTraits = (hauteur + 1) * largeur + hauteur * (largeur + 1);
        sb.append("\nSolution : ").append(this.cpt).append("/").append(totalTraits);
        
        return sb.toString();
    }

    /**
     * Récupère la représentation textuelle d'un trait horizontal.
     * 
     * @param ligne l'indice de ligne de la case
     * @param colonne l'indice de colonne de la case
     * @param direction 0 pour haut, 3 pour bas
     * @return une chaîne représentant le trait horizontal
     */
    private String getStringOfTraitHorizontal(int ligne, int colonne, int direction) {
        Trait trait;
        if (direction == 0) {
            trait = traitsHorizontaux[ligne][colonne];
        } else {
            trait = traitsHorizontaux[ligne + 1][colonne];
        }
        
        String representation = trait.toString();
        String[] parts = representation.split("\\?");
        return parts[1] + parts[1] + parts[1];
    }

    /**
     * Récupère la représentation textuelle d'un trait vertical.
     * 
     * @param ligne l'indice de ligne de la case
     * @param colonne l'indice de colonne de la case
     * @param direction 1 pour gauche, 2 pour droite
     * @return une chaîne représentant le trait vertical
     */
    private String getStringOfTraitVertical(int ligne, int colonne, int direction) {
        Trait trait;
        if (direction == 1) {
            trait = traitsVerticaux[ligne][colonne];
        } else {
            trait = traitsVerticaux[ligne][colonne + 1];
        }
        
        String representation = trait.toString();
        String[] parts = representation.split("\\?");
        return parts[0];
    }
}
