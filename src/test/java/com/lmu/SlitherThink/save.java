import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class save {
	public static void main(String[] args){
        Gson gson = new Gson();
        try (Reader reader = new FileReader("save/saveGrille/grilleJeu1.json")) {
            saveGrille grille = gson.fromJson(reader, saveGrille.class);
            System.out.println("Taille de la grille: " + grille.getTailleGrille());
            System.out.println("Numéro des cases:");
            for (positionGrille pg : grille.getNumeroCases()) {
                System.out.println("Position: " + pg.getPositionGrille() + ", Valeur: " + pg.getValeurGrille());
            }
            System.out.println("Positions des traits:");
            for (positionTrait pt : grille.getListePositionTrait()) {
                System.out.println("Position: " + pt.getPositionTrait() + ", État: " + pt.getEtatTrait());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}


 class saveGlobal{

}

 class saveTechnique{

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
 class saveJoeur{
    
}