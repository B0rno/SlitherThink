package com.lmu.SlitherThink.boutonsAction;

import java.io.File;
import java.net.URL;
import java.util.Random;

import com.lmu.SlitherThink.App;
import com.lmu.SlitherThink.GestionnaireVues;
import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.gestionDonnee.rechercheSave;

import javafx.event.ActionEvent;

/**
 * Classe abstraite de base gérant la navigation entre les différentes vues de l'application.
 * * @author Ilann
 */
public abstract class ChangementFenetre {

    /**
     * Change la vue actuelle de l'application vers une vue spécifiée.
     * * @param event L'événement déclencheur (clic de bouton, etc.).
     * @param viewName Le nom de la vue FXML à charger (sans l'extension .fxml).
     */
    protected void changerFenetre(ActionEvent event, String viewName) {
        App.changerVue(viewName);
    }

    /**
     * Prépare et affiche l'écran de fin de partie pour le mode Aventure.
     * Transmet les statistiques de la partie (aides utilisées, temps) au contrôleur dédié.
     * * @param aides Nombre d'aides utilisées durant la partie.
     * @param aidesMax Nombre maximum d'aides autorisées.
     * @param temps Temps réalisé par le joueur (format String).
     * @param tempsMax Temps limite ou record à battre (format String).
     * @param succes Booléen indiquant si la partie est gagnée ou perdue.
     */
    protected void changerVueFinPartie(int aides, int aidesMax, String temps, String tempsMax, boolean succes) {
        FinPartieAventure controller = (FinPartieAventure) GestionnaireVues.getController("finPartieAventure");
        if (controller != null) {
            controller.mettreDonnees(aides, aidesMax, temps, tempsMax, succes);
            App.changerVue("finPartieAventure");
        } else {
            System.err.println("Erreur : Le contrôleur finPartieAventure est null !");
        }
    }

    /**
     * Initialise et lance une partie spécifique du mode Aventure.
     * * @param event L'événement déclencheur.
     * @param numPartie Le numéro de la grille ou du niveau à charger (sous forme de chaîne de caractères).
     */
    protected void choixPartieAventure(ActionEvent event, String numPartie) {
        PartieTimer controller = (PartieTimer) GestionnaireVues.getController("partieTimer");
        if (controller != null) {
            int numero = Integer.parseInt(numPartie);
            controller.initialiserPartie(numero, false);
            App.changerVue("partieTimer");
        } else {
            System.err.println("Erreur : Le contrôleur de la partie est introuvable !");
        }
    }

    /**
     * Sélectionne une grille aléatoire selon la difficulté choisie et lance une partie libre.
     * * @param event L'événement déclencheur.
     * @param difficulte Le niveau de difficulté souhaité.
     */
    protected void choixPartieLibre(ActionEvent event, String difficulte) {
        LoadSave save = LoadSave.getInstance("");


        String nomGrille = null;
        for (var sauvegarde : rechercheSave.getSauvegardesJoueurLibre(save.getSaveGlobal(), Pseudo.nomJoueur)) {
            if (difficulte.equals(sauvegarde.getDifficulte())) {
                nomGrille = extraireNomGrilleDepuisPath(sauvegarde.getPath());
                if (nomGrille != null) {
                    System.out.println("Grille trouvée dans les sauvegardes : " + nomGrille);
                    break;
                }
            }
        }




        if (nomGrille == null) {
            nomGrille = recupererGrilleAleatoire(difficulte); 
        }
        // 1. Récupérer un nom de fichier valide (ex: "GrilleFacile7X7_2")
        

        if (nomGrille == null) {
            System.err.println("Erreur : Aucune grille trouvée pour la difficulté " + difficulte);
            return;
        }

        App.changerVue("partie");

        Partie controller = (Partie) GestionnaireVues.getController("partie");  
        
        if (controller != null) {
            final String nomGrilleFinal = nomGrille;
            // 4. On demande à JavaFX d'exécuter l'initialisation juste après l'affichage
            // pour garantir que zoneJeu ne soit pas null
            javafx.application.Platform.runLater(() -> {
                controller.initialiserPartie(nomGrilleFinal);
            });
        } else {
            System.err.println("Erreur : Le contrôleur de la partie est introuvable !");
        }
    }

    private String extraireNomGrilleDepuisPath(String pathGrille) {
        if (pathGrille == null || pathGrille.isBlank()) {
            return null;
        }
        String nomFichier = new File(pathGrille).getName();
        if (nomFichier.endsWith(".json")) {
            return nomFichier.substring(0, nomFichier.length() - 5);
        }
        return nomFichier;
    }

    /**
     * Parcourt le dossier des ressources pour trouver une grille JSON correspondant 
     * à la difficulté demandée.
     * * @param difficulte Le niveau de difficulté à rechercher (ex: "Facile").
     * @return Le nom du fichier trouvé (sans l'extension .json), ou null si aucune grille n'est disponible.
     */
    public String recupererGrilleAleatoire(String difficulte) {
        try {
            URL resource = getClass().getResource("/GrilleJson");
            if (resource == null) return null;

            File dossier = new File(resource.toURI());
            
            File[] fichiersTrouves = dossier.listFiles((dir, name) -> 
                name.startsWith("Grille" + difficulte) && name.endsWith(".json"));
        
            if (fichiersTrouves != null && fichiersTrouves.length > 0) {
                Random rand = new Random();
                File choix = fichiersTrouves[rand.nextInt(fichiersTrouves.length)];
                return choix.getName().replace(".json", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }
}