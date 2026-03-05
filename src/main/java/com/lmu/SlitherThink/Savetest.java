package com.lmu.SlitherThink;

public class Savetest {
    public static void main(String[] args) {
        loadSave save = new loadSave("");
        saveManger saveManager = new saveManger(save);

        saveManager.separerLesSauvegardes();
        saveManager.getDossiersJson().forEach((key, value) -> {
            System.out.println("Clé: " + key);
            System.out.println("Valeur JSON: " + value);
            System.out.println("-----------------------------");
        });

        saveManager.sauvegarderJsonDansArborescence(save.getBasePath());
    }
}
