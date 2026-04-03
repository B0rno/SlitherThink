package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

/**
 * Contrôleur de la vue de sélection des niveaux du mode Aventure.
 * Cette classe gère l'affichage des niveaux disponibles, la récupération des meilleurs 
 * scores (étoiles) du joueur actuel et le lancement des parties.
 * * @author Ilann
 */
public class ChoixPartieAventure extends ChangementFenetre {
    
    // Déclarations FXML des ImageView pour les étoiles (12 niveaux, 3 étoiles par niveau)
    @FXML private ImageView etoile_1_1, etoile_1_2, etoile_1_3;
    @FXML private ImageView etoile_2_1, etoile_2_2, etoile_2_3;
    @FXML private ImageView etoile_3_1, etoile_3_2, etoile_3_3;
    @FXML private ImageView etoile_4_1, etoile_4_2, etoile_4_3;
    @FXML private ImageView etoile_5_1, etoile_5_2, etoile_5_3;
    @FXML private ImageView etoile_6_1, etoile_6_2, etoile_6_3;
    @FXML private ImageView etoile_7_1, etoile_7_2, etoile_7_3;
    @FXML private ImageView etoile_8_1, etoile_8_2, etoile_8_3;
    @FXML private ImageView etoile_9_1, etoile_9_2, etoile_9_3;
    @FXML private ImageView etoile_10_1, etoile_10_2, etoile_10_3;
    @FXML private ImageView etoile_11_1, etoile_11_2, etoile_11_3;
    @FXML private ImageView etoile_12_1, etoile_12_2, etoile_12_3;

    /** Image représentant une étoile obtenue. */
    private final Image IMAGE_PLEINE = new Image(getClass().getResourceAsStream("/images/etoilePleine.png"));
    
    /** Image représentant une étoile non obtenue. */
    private final Image IMAGE_VIDE = new Image(getClass().getResourceAsStream("/images/etoileVide.png"));

    /**
     * Regroupe toutes les ImageView d'étoiles dans une matrice pour faciliter le parcours.
     * @return Un tableau à deux dimensions [niveau-1][index_etoile].
     */
    private ImageView[][] getToutesLesEtoiles() {
        return new ImageView[][] {
            {etoile_1_1, etoile_1_2, etoile_1_3}, {etoile_2_1, etoile_2_2, etoile_2_3},
            {etoile_3_1, etoile_3_2, etoile_3_3}, {etoile_4_1, etoile_4_2, etoile_4_3},
            {etoile_5_1, etoile_5_2, etoile_5_3}, {etoile_6_1, etoile_6_2, etoile_6_3},
            {etoile_7_1, etoile_7_2, etoile_7_3}, {etoile_8_1, etoile_8_2, etoile_8_3},
            {etoile_9_1, etoile_9_2, etoile_9_3}, {etoile_10_1, etoile_10_2, etoile_10_3},
            {etoile_11_1, etoile_11_2, etoile_11_3}, {etoile_12_1, etoile_12_2, etoile_12_3}
        };
    }

    /**
     * Réinitialise graphiquement toutes les étoiles en les affichant comme "vides".
     */
    private void reinitialiserEtoilesVides() {
        for (ImageView[] ligneEtoiles : getToutesLesEtoiles()) {
            for (ImageView etoile : ligneEtoiles) {
                if (etoile != null) {
                    etoile.setImage(IMAGE_VIDE);
                }
            }
        }
    }

    /**
     * Extrait le numéro du niveau à partir du nom d'un fichier de sauvegarde ou d'une ressource.
     * @param nomFichier Le nom de la chaîne à analyser.
     * @return Le numéro du niveau extrait, ou -1 en cas d'erreur.
     */
    private int extraireNumeroNiveau(String nomFichier) {
        if (nomFichier == null) return -1;
        String chiffres = nomFichier.replaceAll("[^0-9]", "");
        if (chiffres.isEmpty()) return -1;
        try {
            return Integer.parseInt(chiffres);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Vérifie si le fichier de sauvegarde correspond bien à une partie du mode aventure.
     * @param nomFichier Le nom du fichier à tester.
     * @return true si le fichier commence par "partie" (insensible à la casse).
     */
    private boolean estNiveauPartie(String nomFichier) {
        if (nomFichier == null) return false;
        String nomNormalise = nomFichier.replace('\\', '/');
        String nomSimple = nomNormalise.substring(nomNormalise.lastIndexOf('/') + 1).toLowerCase();
        return nomSimple.startsWith("partie");
    }
    
    /**
     * Méthode d'initialisation de JavaFX. Appelle le rafraîchissement des étoiles au chargement.
     */
    @FXML
    public void initialize() {
        rafraichirEtoiles();
    }

    /**
     * Méthode à appeler lorsque la vue devient active pour mettre à jour l'affichage des scores.
     */
    public void onViewShown() {
        rafraichirEtoiles();
    }

    /**
     * Parcourt les scores sauvegardés pour le joueur actuel, calcule le nombre d'étoiles
     * obtenues pour chaque niveau aventure et met à jour l'interface graphique.
     */
    private void rafraichirEtoiles() {
        reinitialiserEtoilesVides();

        String pseudoJoueurActuel = Pseudo.nomJoueur == null ? "" : Pseudo.nomJoueur.trim();
        if (pseudoJoueurActuel.isEmpty()) return;

        int[] meilleuresEtoiles = new int[13];

        try {
            com.lmu.SlitherThink.save.LoadSave save = com.lmu.SlitherThink.save.LoadSave.getInstance("");
            java.util.List<com.lmu.SlitherThink.save.csvScore.structure.StructureCSV> scores = save.getScores();

            for (com.lmu.SlitherThink.save.csvScore.structure.StructureCSV scoreData : scores) {
                if (scoreData.getPseudo() != null && scoreData.getPseudo().trim().equalsIgnoreCase(pseudoJoueurActuel)) {
                    String nomFichier = scoreData.getNiveau(); 

                    if (!estNiveauPartie(nomFichier)) continue;
                    
                    int niveau = extraireNumeroNiveau(nomFichier);
                        
                    if (niveau >= 1 && niveau <= 12) {
                        int chrono = scoreData.getChrono();
                        int nbAides = scoreData.getNbAide();
                            
                        com.lmu.SlitherThink.Partie.Score calculScore = new com.lmu.SlitherThink.Partie.Score();
                        calculScore.setReconstructionSave(chrono, nbAides);
                        calculScore.calculerEtoiles();
                        int etoilesPossibles = calculScore.getEtoiles();
                            
                        if (etoilesPossibles > meilleuresEtoiles[niveau]) {
                            meilleuresEtoiles[niveau] = etoilesPossibles;
                        }
                    }
                }
            }

            for (int i = 1; i <= 12; i++) {
                if (meilleuresEtoiles[i] > 0) {
                    afficherEtoiles(i, meilleuresEtoiles[i]);
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture de Score.csv : " + e.getMessage());
        }
    }

    /**
     * Remplace les images des étoiles vides par des étoiles pleines pour un niveau donné.
     * @param niveau L'indice du niveau (1 à 12).
     * @param nbEtoiles Le nombre d'étoiles pleines à afficher (maximum 3).
     */
    public void afficherEtoiles(int niveau, int nbEtoiles) {
        ImageView[][] toutesLesEtoiles = getToutesLesEtoiles();

        if (niveau >= 1 && niveau <= 12 && nbEtoiles > 0) {
            for (int i = 0; i < Math.min(nbEtoiles, 3); i++) {
                if (toutesLesEtoiles[niveau - 1][i] != null) {
                    toutesLesEtoiles[niveau - 1][i].setImage(IMAGE_PLEINE);
                }
            }
        }
    }

    /**
     * Retourne à la vue de sélection du mode de jeu.
     * @param event L'événement de clic.
     */
    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }

    /**
     * Lance la partie correspondant au bouton cliqué.
     * Le numéro du niveau est extrait de l'identifiant du bouton.
     * @param event L'événement de clic sur un bouton de niveau.
     */
    @FXML
    public void partie(ActionEvent event) {
        Button b = (Button) event.getSource();
        String niveau = b.getId().replace("btn", "");   
        choixPartieAventure(event, niveau);
    }
}