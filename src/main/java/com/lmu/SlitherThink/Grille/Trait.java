public enum Trait {
    VIDE,
    PLEIN,
    CROIX;

    public Trait etatSuivant(){
        return switch(this){
            case VIDE -> PLEIN;
            case PLEIN -> CROIX;
            case CROIX -> VIDE;
        }
    }

    public String toString(){
        return switch(this){
            case VIDE -> " ?  ";
            case PLEIN -> '|?--';
            case CROIX -> "*?**";
        }
    }
}
