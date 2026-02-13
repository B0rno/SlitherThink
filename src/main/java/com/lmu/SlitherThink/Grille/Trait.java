public class Trait {
    private ValeurTrait etat;

    public Trait(){
        this.etat = ValeurTrait.VIDE;
    }

    public void etatSuivant(){
        this.etat = this.etat.etatSuivant();
    }

    public String toString(){
        return this.etat.toString();
    }
}
