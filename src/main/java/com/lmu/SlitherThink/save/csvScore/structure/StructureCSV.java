package com.lmu.SlitherThink.save.csvScore.structure;

import com.opencsv.bean.CsvBindByName;

public class StructureCSV {
    @CsvBindByName(column = "pseudo")
    private String pseudo;

    @CsvBindByName(column = "niveau")
    private String cheminGrille;

    @CsvBindByName(column = "nbAide")
    private int nbAide;

    @CsvBindByName(column = "chrono")
    private int chrono;

    public StructureCSV() {}


    public StructureCSV(String pseudo, String cheminGrille, int nbAide, int chrono) {
        this.pseudo = pseudo;
        this.cheminGrille = cheminGrille;
        this.nbAide = nbAide;
        this.chrono = chrono;
    }

    public String getPseudo() {return pseudo;}
    public String getCheminGrille() {return cheminGrille;}
    public int getNbAide() {return nbAide;}
    public int getChrono() {return chrono;}

    public String getNom() {return getPseudo();}
    public String getNiveau() {return getCheminGrille();}
}
