public class Case {
    private final Int numero_case;
    private final Trait solution;
    private Trait[] etatJeu;

    private static Trait[] getEtatInitialCase(){
        this.etatJeu = new Trait[]{Trait.VIDE,Trait.VIDE,Trait.VIDE,Trait.VIDE}
    }

    public Case(Int num){
        this.numero_case = num;
        this.etatJeu = Case.getEtatInitialCase();
        this.solution = Case.getEtatInitialCase();
    }

    public void setSolutionCase(){
        
    }

    public String toString(){
        return 
    }
}
