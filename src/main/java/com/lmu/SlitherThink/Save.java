package com.lmu.SlitherThink;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Save {
	public static void main(String[] args){
        loadSave save = new loadSave("");
        System.out.println(save.toString());
    }
}

class loadSave{
    private saveGrille grille;
    private saveTechnique technique;
    loadSave(String pathBeforeSave){
        Gson gson = new Gson();
        try (Reader reader = new InputStreamReader(Save.class.getResourceAsStream(pathBeforeSave + "/save/saveGrille/grilleJeu1.json"),StandardCharsets.UTF_8)) {
            grille = gson.fromJson(reader, saveGrille.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Reader reader = new InputStreamReader(Save.class.getResourceAsStream(pathBeforeSave +"/save/technique.json"),StandardCharsets.UTF_8)) {
            technique = gson.fromJson(reader, saveTechnique.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void afficherTechn(){
        if (technique != null && technique.getStockageLangague() != null) {
            for (languageContenue langcont : technique.getStockageLangague()) {
                if (langcont == null) continue;
                System.out.println("Langage: " + langcont.getLangage());
                List<contenuTechnique> contenus = langcont.getContenu();
                if (contenus == null) continue;
                for (contenuTechnique contenu : contenus) {
                    if (contenu == null) continue;
                    System.out.println("----------------\nNiveau: " + contenu.getNv());
                    List<stockageTechnique> techs = contenu.getTechniqueParsNv();
                    if (techs == null) {
                        System.out.println("Aucune technique disponible pour ce niveau.");
                        continue;
                    }
                    for (stockageTechnique tech : techs) {
                        if (tech == null) continue;
                        System.out.println("----------------\nNom de la technique: " + tech.getName());
                        System.out.println("----------------Description----------------\n" + tech.getDescription());
                    }
                }
            }
        } else {
            System.out.println("Aucune technique disponible.");
        }
    }

    public void afficherGrille(){
        if (grille != null) {
            System.out.println("Taille de la grille: " + grille.getTailleGrille());
            List<positionGrille> cases = grille.getNumeroCases();
            if (cases != null) {
                for (positionGrille posgrille : cases) {
                    if (posgrille == null) continue;
                    System.out.println("----------------\nPosition: " + posgrille.getPositionGrille());
                    System.out.println("Valeur: " + posgrille.getValeurGrille());
                }
            } else {
                System.out.println("Aucune case disponible dans la grille.");
            }
        } else {
            System.out.println("Aucune grille disponible.");
        }
    }

    public void affichertoJson(){
        Gson gson = new Gson();
        String jsonGrille = gson.toJson(grille);
        String jsonTechnique = gson.toJson(technique);
        System.out.println("Grille en JSON:\n" + jsonGrille);
        System.out.println("Technique en JSON:\n" + jsonTechnique);
    }

    public saveGrille getGrille(){
        return grille;
    }
    public saveTechnique getTechnique(){
        return technique;
    }

    public String toString(){
        this.afficherGrille();
        this.afficherTechn();
        System.out.println("\n\nAffichage en JSON:");
        this.affichertoJson();
        return "Affichage de la grille et des techniques terminé.";
    }



}

class saveGlobal{
    private List<savePartie> SauvegardeLibre;
    private List<savePartie> SauvegardeAventure;

    public List<savePartie> getSauvegardeLibre(){
        return SauvegardeLibre;
    }
    public List<savePartie> getSauvegardeAventure(){
        return SauvegardeAventure;
    }
}

class savePartie{
    private String pseudo;
    private Integer id;
    private String pathGrille;

    savePartie(String pseudo, Integer id, String pathGrille){
        this.pseudo = pseudo;
        this.id = id;
        this.pathGrille = pathGrille;
    }

    public String getPseudo(){
        return pseudo;
    }
    public Integer getId(){
        return id;
    }
    public String getPathGrille(){
        return pathGrille;
    }
    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public void setPathGrille(String pathGrille){
        this.pathGrille = pathGrille;
    }
}

class saveTechnique{
    private List<languageContenue> stockage_langague;

    public List<languageContenue> getStockageLangague(){
        return stockage_langague;
    }
}

class languageContenue{
    private String langage;
    private List<contenuTechnique> contenu;

    public String getLangage(){
        return langage;
    }
    public List<contenuTechnique> getContenu(){
        return contenu;
    }
}

class contenuTechnique{
    private String nv;    
    private List<stockageTechnique> techniqueParsNv;

    public String getNv(){
        return nv;
    }
    public List<stockageTechnique> getTechniqueParsNv(){
        return techniqueParsNv;
    }
}

class stockageTechnique{
    private String name;
    private String description;

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
}

class saveGrille{
    private int taille;
    private List<positionGrille> numero_cases;
    private List<positionTrait> liste_position_trait;

    public int getTailleGrille(){
            return taille;
    }
    public List<positionGrille> getNumeroCases(){
        return numero_cases;
    }
    public List<positionTrait> getListePositionTrait(){
        return liste_position_trait;
    }
}
 class positionTrait{
    private List<Integer> position;
    private List<Integer> etat;

    public List<Integer> getPositionTrait(){
        return position;
    }
    public List<Integer> getEtatTrait(){
        return etat;
    }
}
 class positionGrille{
    private List<Integer> position;
    private int valeur;

    public List<Integer> getPositionGrille(){
            return position;
    }
    public int getValeurGrille(){
        return valeur;
    }
}
 class detailleSavePartie{
    private List<positionTrait> etatgrille;
    private Integer Chronometre;
    private Integer nbAides;

    public List<positionTrait> getEtatGrille(){
        return etatgrille;
    }
    public Integer getChronometre(){
        return Chronometre;
    }
    public Integer getNbAides(){
        return nbAides;
    }

}