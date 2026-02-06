public class Case {
    private final int numero_case;
    private Trait[] solution;
    private Trait[] etatJeu;

    private static Trait[] getEtatInitialCase(){
        return new Trait[]{Trait.VIDE,Trait.VIDE,Trait.VIDE,Trait.VIDE};
    }

    public Case(int num){
        this.numero_case = num;
        this.etatJeu = Case.getEtatInitialCase();
        this.solution = Case.getEtatInitialCase();
    }

    public void setSolutionCase(Trait[] solutionCase){
        this.solution = solutionCase;
    }

    public Trait getTrait(int direction){
        return etatJeu[direction];
    }

    private String getStringOfTrait(int direction) {
        String representation = this.getTrait(direction).toString();
        String[] parts = representation.split("\\?");

        boolean useSecondPart = (direction == 0 || direction == 3);

        if (useSecondPart && parts.length > 1) {
            return parts[1];
        }
        return parts[0];
    }

    public void updateTrait(int direction){
        this.etatJeu[direction] = this.etatJeu[direction].etatSuivant();
    }


    public String toString(){
        return " " + getStringOfTrait(0) + " \n"
            + getStringOfTrait(1) + this.numero_case + getStringOfTrait(2) + "\n"
            + " " + getStringOfTrait(3) + " ";
    }
}