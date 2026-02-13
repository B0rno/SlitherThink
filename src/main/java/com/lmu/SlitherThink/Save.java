package com.lmu.SlitherThink;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;

public class Save {
	public static void main(String[] args){
        loadSave save = new loadSave("");

        saveManger saveManager = new saveManger(save);
        saveManager.separerLesSauvegardes();
        saveManager.getDossiersJson().forEach((key, value) -> {
            System.out.println("Clé: " + key);
            System.out.println("Valeur JSON: " + value);
            System.out.println("-----------------------------");
        });
    }
}

class saveManger{
    private loadSave save;
    private List<Class<?>> classesAExtraire = List.of(
        SaveGrille.class, DetailleSavePartie.class, SaveGlobal.class, SaveTechnique.class
    );
    private Map< String, String> dossiersJson ;
    saveManger(loadSave save){
        this.save = save;
        dossiersJson = new HashMap<>();
    }

    public Map<String, String> getDossiersJson() {
        return dossiersJson;
    }

    public void separerLesSauvegardes() {
        Gson gsonBasique = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        JsonSerializer<Object> separateur = new JsonSerializer<Object>() {
            @Override
            public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
                String nomClasse;
                if (src instanceof DetailleSavePartie) {
                    DetailleSavePartie d = (DetailleSavePartie) src;
                    String customName = d.getNameClass();
                    nomClasse = (customName != null && !customName.isEmpty())
                            ? customName
                            : d.getClass().getSimpleName();
                } else {
                    nomClasse = src.getClass().getSimpleName();
                }
                String jsonExtrait = gsonBasique.toJson(src);
                dossiersJson.put(nomClasse, jsonExtrait);

                return new JsonPrimitive("fichier:" + nomClasse);
            }
        };

        // Pré-popule les extraits pour les références
        dossiersJson.put("SaveGrille", gsonBasique.toJson(save.getGrille()));
        dossiersJson.put("SaveTechnique", gsonBasique.toJson(save.getTechnique()));
        dossiersJson.put("SaveGlobal", gsonBasique.toJson(save.getSaveGlobal()));

        // Ajoute les détails des parties (150, 151, etc.)
        if (save.getSaveGlobal() != null) {
            if (save.getSaveGlobal().getSauvegardeLibre() != null) {
                for (savePartieLienJoueur sp : save.getSaveGlobal().getSauvegardeLibre()) {
                    DetailleSavePartie det = sp.getDetailleSave();
                    if (det != null) {
                        dossiersJson.put(det.getNameClass() != null ? det.getNameClass() : "DetailleSavePartie", gsonBasique.toJson(det));
                    }
                }
            }
            if (save.getSaveGlobal().getSauvegardeAventure() != null) {
                for (savePartieLienJoueur sp : save.getSaveGlobal().getSauvegardeAventure()) {
                    DetailleSavePartie det = sp.getDetailleSave();
                    if (det != null) {
                        dossiersJson.put(det.getNameClass() != null ? det.getNameClass() : "DetailleSavePartie", gsonBasique.toJson(det));
                    }
                }
            }
        }

        GsonBuilder gsonbuilder = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(loadSave.class, new LoadSaveSerializer());
        
        for (Class<?> clazz : classesAExtraire) {
            gsonbuilder.registerTypeAdapter(clazz, separateur);
        }
        
        Gson gsonPrincipal = gsonbuilder.create();
        String jsonPrincipal = gsonPrincipal.toJson(save);
        dossiersJson.put("LoadSave", jsonPrincipal);
    }



}
// Sérialise loadSave avec références et liste des détails
class LoadSaveSerializer implements JsonSerializer<loadSave> {
    @Override
    public JsonElement serialize(loadSave src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("grille", "fichier:SaveGrille");
        obj.addProperty("technique", "fichier:SaveTechnique");
        obj.addProperty("saveGlobal", "fichier:SaveGlobal");

        // Ajoute un tableau des références de détails
        com.google.gson.JsonArray detailsRefs = new com.google.gson.JsonArray();
        SaveGlobal sg = src.getSaveGlobal();
        if (sg != null) {
            java.util.function.Consumer<java.util.List<savePartieLienJoueur>> addRefs = list -> {
                if (list == null) return;
                for (savePartieLienJoueur sp : list) {
                    DetailleSavePartie det = sp != null ? sp.getDetailleSave() : null;
                    if (det != null) {
                        String name = det.getNameClass();
                        detailsRefs.add("fichier:" + (name != null ? name : "DetailleSavePartie"));
                    }
                }
            };
            addRefs.accept(sg.getSauvegardeLibre());
            addRefs.accept(sg.getSauvegardeAventure());
        }
        if (detailsRefs.size() > 0) {
            obj.add("DetailleSavePartie", detailsRefs);
        }
        return obj;
    }
}

class loadSave{
    private SaveGrille grille;
    private SaveTechnique technique;
    private SaveGlobal saveGlobal;
    
    loadSave(String pathBeforeSave){

        Gson gson = new Gson();
        try (Reader reader = new InputStreamReader(Save.class.getResourceAsStream(pathBeforeSave + "/save/saveGrille/grilleJeu1.json"),StandardCharsets.UTF_8)) {
            grille = gson.fromJson(reader, SaveGrille.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Reader reader = new InputStreamReader(Save.class.getResourceAsStream(pathBeforeSave +"/save/technique.json"),StandardCharsets.UTF_8)) {
            technique = gson.fromJson(reader, SaveTechnique.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Reader reader = new InputStreamReader(Save.class.getResourceAsStream(pathBeforeSave +"/save/saveGlobal.json"),StandardCharsets.UTF_8)) {
            saveGlobal = gson.fromJson(reader, SaveGlobal.class);

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

    public SaveGrille getGrille(){
        return grille;
    }
    public SaveTechnique getTechnique(){
        return technique;
    }
    public SaveGlobal getSaveGlobal(){
        return saveGlobal;
    }

    @Override
    public String toString(){
        this.afficherGrille();
        this.afficherTechn();
        System.out.println("\n\nAffichage en JSON:");
        this.affichertoJson();
        return "Affichage de la grille et des techniques terminé.";
    }
}

class SaveGlobal{
    private List<savePartieLienJoueur> SauvegardeLibre;
    private List<savePartieLienJoueur> SauvegardeAventure;

    public List<savePartieLienJoueur> getSauvegardeLibre(){
        return SauvegardeLibre;
    }
    public List<savePartieLienJoueur> getSauvegardeAventure(){
        return SauvegardeAventure;
    }
}

class savePartieLienJoueur{
    private static final String pathLienJoueurSave = "/save/saveJoueur/";
    private String pseudo;
    private Integer id;
    private String path;
    private transient DetailleSavePartie detailleSave;

    // No-arg constructor for Gson
    savePartieLienJoueur() {}

    savePartieLienJoueur(String pseudo, Integer id, String path){
        this.pseudo = pseudo;
        this.id = id;
        this.path = path;
    }

    public void loadDetailleSave(){
        if (id == null) {
            return;
        }
        
        String pathSaveJoeur = pathLienJoueurSave + id + ".json";
        Gson gson = new Gson();
        java.io.InputStream is = Save.class.getResourceAsStream(pathSaveJoeur);
        if (is == null) {
            System.err.println("Ressource introuvable: " + pathSaveJoeur);
            return;
        }
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            detailleSave = gson.fromJson(reader, DetailleSavePartie.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.detailleSave != null) {
            this.detailleSave.setNameClass(this.id != null ? this.id.toString() : null);
        }
    }

    public DetailleSavePartie getDetailleSave(){
        if (detailleSave == null) {
            loadDetailleSave();
        }
        return detailleSave;
    }

    public String getPseudo(){
        return pseudo;
    }
    public Integer getId(){
        return id;
    }
    public String getPath(){
        return path;
    }
    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public void setPath(String path){
        this.path = path;
    }
}

class SaveTechnique{
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

class SaveGrille{
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
 class DetailleSavePartie{
    private List<positionTrait> etatgrille;
    private Integer Chronometre;
    private Integer nbAides;
    private String nameClass = null ;

    public List<positionTrait> getEtatGrille(){
        return etatgrille;
    }
    public Integer getChronometre(){
        return Chronometre;
    }
    public Integer getNbAides(){
        return nbAides;
    }
    public String getNameClass(){
        return nameClass;
    }
    public void setNameClass(String nameClass){
        this.nameClass = nameClass;
    }

}