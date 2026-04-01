package com.lmu.SlitherThink.boutonsAction;

import javafx.event.ActionEvent;
import com.lmu.SlitherThink.App;
import com.lmu.SlitherThink.GestionnaireVues;
import java.io.File;
import java.net.URL;
import java.util.Random;

public abstract class ChangementFenetre {

    protected void changerFenetre(ActionEvent event, String viewName) {
        App.changerVue(viewName);
    }

    protected void changerVueFinPartie(int aides, int aidesMax, String temps, String tempsMax, boolean succes) {
        FinPartieAventure controller = (FinPartieAventure) GestionnaireVues.getController("finPartieAventure");
        if (controller != null) {
            controller.mettreDonnees(aides, aidesMax, temps, tempsMax, succes);
            App.changerVue("finPartieAventure");
        } else {
            System.err.println("Erreur : Le contrôleur finPartieAventure est null !");
        }
    }

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

    protected void choixPartieLibre(ActionEvent event, String difficulte) {
        // 1. Récupérer un nom de fichier valide (ex: "GrilleFacile7X7_2")
        String nomGrille = recupererGrilleAleatoire(difficulte); 

        if (nomGrille == null) {
            System.err.println("Erreur : Aucune grille trouvée pour la difficulté " + difficulte);
            return;
        }

        // 2. IMPORTANT : On change d'abord la vue pour que l'injection FXML se fasse
        App.changerVue("partie");

        // 3. On récupère le contrôleur de l'instance qui vient d'être affichée
        Partie controller = (Partie) GestionnaireVues.getController("partie");  
        
        if (controller != null) {
            // 4. On demande à JavaFX d'exécuter l'initialisation juste après l'affichage
            // pour garantir que zoneJeu ne soit pas null
            javafx.application.Platform.runLater(() -> {
                controller.initialiserPartie(nomGrille);
            });
        } else {
            System.err.println("Erreur : Le contrôleur de la partie est introuvable !");
        }
    }

    public String recupererGrilleAleatoire(String difficulte) {
        try {
            URL resource = getClass().getResource("/GrilleJson");
            if (resource == null) return null;

            File dossier = new File(resource.toURI());
            
            // Filtre : commence par "Grille" + difficulté ET est un fichier (pas le dossier Exemple)
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