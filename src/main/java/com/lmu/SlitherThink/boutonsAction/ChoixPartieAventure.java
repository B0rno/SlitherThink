package com.lmu.SlitherThink.boutonsAction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

public class ChoixPartieAventure extends ChangementFenetre {
    
    // Déclaration de toutes les étoiles
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

    private final Image IMAGE_PLEINE = new Image(getClass().getResourceAsStream("/images/etoilePleine.png"));
    private final Image IMAGE_VIDE = new Image(getClass().getResourceAsStream("/images/etoileVide.png"));

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

    private void reinitialiserEtoilesVides() {
        for (ImageView[] ligneEtoiles : getToutesLesEtoiles()) {
            for (ImageView etoile : ligneEtoiles) {
                if (etoile != null) {
                    etoile.setImage(IMAGE_VIDE);
                }
            }
        }
    }

    private int extraireNumeroNiveau(String nomFichier) {
        if (nomFichier == null) {
            return -1;
        }
        String chiffres = nomFichier.replaceAll("[^0-9]", "");
        if (chiffres.isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(chiffres);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    @FXML
    public void initialize() {
        rafraichirEtoiles();
    }

    public void onViewShown() {
        rafraichirEtoiles();
    }

    private void rafraichirEtoiles() {
        reinitialiserEtoilesVides();

        String pseudoJoueurActuel = Pseudo.nomJoueur == null ? "" : Pseudo.nomJoueur.trim();
        if (pseudoJoueurActuel.isEmpty()) {
            return;
        }

        int[] meilleuresEtoiles = new int[13]; // Indices de 1 à 12 (0 ignoré)

        try {
            com.lmu.SlitherThink.save.LoadSave save = com.lmu.SlitherThink.save.LoadSave.getInstance("");
            java.util.List<com.lmu.SlitherThink.save.csvScore.structure.StructureCSV> scores = save.getScores();

            for (com.lmu.SlitherThink.save.csvScore.structure.StructureCSV scoreData : scores) {
                // On vérifie que le score appartient bien au joueur actuel
                if (scoreData.getPseudo() != null && scoreData.getPseudo().trim().equalsIgnoreCase(pseudoJoueurActuel)) {
                    String nomFichier = scoreData.getNiveau(); 
                    
                    if (nomFichier != null) {
                        // Extrait uniquement le numéro contenu dans le nom du fichier
                        int niveau = extraireNumeroNiveau(nomFichier);
                            
                        if (niveau >= 1 && niveau <= 12) {
                            int chrono = scoreData.getChrono();
                            int nbAides = scoreData.getNbAide();
                                
                            // Utilisation de Score.java pour le calcul
                            com.lmu.SlitherThink.Partie.Score calculScore = new com.lmu.SlitherThink.Partie.Score();
                            calculScore.setReconstructionSave(chrono, nbAides);
                            calculScore.calculerEtoiles();
                            int etoilesPossibles = calculScore.getEtoiles();
                                
                            // On garde le meilleur score par niveau
                            if (etoilesPossibles > meilleuresEtoiles[niveau]) {
                                meilleuresEtoiles[niveau] = etoilesPossibles;
                            }
                        }
                    }
                }
            }

            // Met à jour l'affichage avec les meilleures étoiles stockées
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
     * Remplace l'image des étoiles vides par des étoiles pleines selon le nombre d'étoiles obtenues.
     * @param niveau Niveau de 1 à 12
     * @param nbEtoiles Nombre d'étoiles gagnées (0 à 3)
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

    @FXML
    private void retour(ActionEvent event) {
        changerFenetre(event, "choixMode");
    }

    //load partie en fonction du bouton cliqué (load la vue "partie" avec le numero de la partie cliquée)
    @FXML
    public void partie(ActionEvent event) {
        Button b = (Button) event.getSource();
        String niveau = b.getId().replace("btn", "");   
        choixPartieAventure(event, niveau);
        //méthode dans changementfenetre.java
    }
}
