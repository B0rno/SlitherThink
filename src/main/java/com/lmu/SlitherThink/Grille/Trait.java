public class Trait {
    private ValeurTrait etat;

    public Trait(){
        this.etat = ValeurTrait.VIDE;
    }

    public Trait(ValeurTrait etat){
        this.etat = etat;
    }

    public void setTrait(ValeurTrait etat){
        this.etat = etat;
    }

    public ValeurTrait getEtat(){
        return this.etat;
    }

    public void etatSuivant(){
        this.etat = this.etat.etatSuivant();
    }

    public String toString(){
        return this.etat.toString();
    }
}
