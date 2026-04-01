package com.lmu.SlitherThink;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lmu.SlitherThink.Grille.Case;
import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Grille.Trait;
import com.lmu.SlitherThink.Grille.ValeurTrait;

/**
 * Tests unitaires pour le package Grille.
 * Teste Matrice, Case, Trait et ValeurTrait.
 */
public class GrilleTest {

    private Matrice matrice;
    private Case caseTest;

    @BeforeEach
    public void setUp() {
        // Créer une matrice 5x5
        matrice = new Matrice(5, 5);
        caseTest = matrice.getCase(2, 2);
    }

    // ===== Tests Matrice =====

    @Test
    public void testCreationMatrice() {
        assertNotNull(matrice);
        assertEquals(5, matrice.getHauteur());
        assertEquals(5, matrice.getLargeur());
    }

    @Test
    public void testGetCaseValide() {
        Case c = matrice.getCase(0, 0);
        assertNotNull(c);
    }

    @Test
    public void testGetCaseInvalide() {
        // Hors limites
        Case c1 = matrice.getCase(-1, 0);
        Case c2 = matrice.getCase(0, -1);
        Case c3 = matrice.getCase(10, 0);
        Case c4 = matrice.getCase(0, 10);

        assertNull(c1);
        assertNull(c2);
        assertNull(c3);
        assertNull(c4);
    }

    @Test
    public void testCliquer() {
        // Cliquer sur un trait devrait changer son état
        Case c = matrice.getCase(0, 0);
        assertNotNull(c);

        // État initial devrait être VIDE
        assertEquals(ValeurTrait.VIDE, c.getTrait(0).getEtat());

        // Premier clic : VIDE → PLEIN
        matrice.cliquer(0, 0, 0);
        assertEquals(ValeurTrait.PLEIN, c.getTrait(0).getEtat());

        // Deuxième clic : PLEIN → CROIX
        matrice.cliquer(0, 0, 0);
        assertEquals(ValeurTrait.CROIX, c.getTrait(0).getEtat());

        // Troisième clic : CROIX → VIDE
        matrice.cliquer(0, 0, 0);
        assertEquals(ValeurTrait.VIDE, c.getTrait(0).getEtat());
    }

    @Test
    public void testLoadGrille() {
        // Charger la grille tutoriel
        Matrice grilleTuto = Matrice.loadGrille("tutoriel");
        assertNotNull(grilleTuto, "La grille tutoriel devrait être chargeable");
        assertTrue(grilleTuto.getHauteur() > 0);
        assertTrue(grilleTuto.getLargeur() > 0);
    }

    // ===== Tests Case =====

    @Test
    public void testCaseNumero() {
        assertNotNull(caseTest);

        // Par défaut, les cases n'ont pas de numéro (-1)
        assertEquals(-1, caseTest.getNumero());
    }

    @Test
    public void testCaseTraits() {
        assertNotNull(caseTest);

        // Vérifier que les 4 traits existent
        for (int direction = 0; direction < 4; direction++) {
            Trait t = caseTest.getTrait(direction);
            assertNotNull(t, "Le trait direction " + direction + " devrait exister");
            assertEquals(ValeurTrait.VIDE, t.getEtat(), "Le trait devrait être VIDE initialement");
        }
    }

    @Test
    public void testCaseTraitPartage() {
        // Deux cases adjacentes partagent un trait
        Case c1 = matrice.getCase(1, 1);
        Case c2 = matrice.getCase(1, 2); // À droite de c1

        assertNotNull(c1);
        assertNotNull(c2);

        // Le trait EST (2) de c1 devrait être le même que le trait OUEST (1) de c2
        Trait traitC1Est = c1.getTrait(2);
        Trait traitC2Ouest = c2.getTrait(1);

        assertNotNull(traitC1Est);
        assertNotNull(traitC2Ouest);
        assertSame(traitC1Est, traitC2Ouest, "Les cases adjacentes devraient partager le même trait");

        // Modifier un trait devrait affecter les deux cases
        traitC1Est.etatSuivant();
        assertEquals(ValeurTrait.PLEIN, traitC2Ouest.getEtat());
    }

    // ===== Tests Trait =====

    @Test
    public void testTraitEtatInitial() {
        Trait trait = new Trait();
        assertEquals(ValeurTrait.VIDE, trait.getEtat());
    }

    @Test
    public void testTraitCycleEtats() {
        Trait trait = new Trait();

        // VIDE → PLEIN
        trait.etatSuivant();
        assertEquals(ValeurTrait.PLEIN, trait.getEtat());

        // PLEIN → CROIX
        trait.etatSuivant();
        assertEquals(ValeurTrait.CROIX, trait.getEtat());

        // CROIX → VIDE
        trait.etatSuivant();
        assertEquals(ValeurTrait.VIDE, trait.getEtat());
    }

    @Test
    public void testTraitSetEtat() {
        Trait trait = new Trait();

        trait.setTrait(ValeurTrait.PLEIN);
        assertEquals(ValeurTrait.PLEIN, trait.getEtat());

        trait.setTrait(ValeurTrait.CROIX);
        assertEquals(ValeurTrait.CROIX, trait.getEtat());

        trait.setTrait(ValeurTrait.VIDE);
        assertEquals(ValeurTrait.VIDE, trait.getEtat());
    }

    // ===== Tests ValeurTrait =====

    @Test
    public void testValeurTraitEnum() {
        // Vérifier que l'enum a bien 3 valeurs
        ValeurTrait[] valeurs = ValeurTrait.values();
        assertEquals(3, valeurs.length);

        // Vérifier les noms
        assertEquals(ValeurTrait.VIDE, ValeurTrait.valueOf("VIDE"));
        assertEquals(ValeurTrait.PLEIN, ValeurTrait.valueOf("PLEIN"));
        assertEquals(ValeurTrait.CROIX, ValeurTrait.valueOf("CROIX"));
    }

    // ===== Tests d'intégration =====

    @Test
    public void testJouerPartieSimple() {
        // Créer une petite grille et jouer quelques coups
        Matrice m = new Matrice(3, 3);

        // Tracer un carré autour de la case (1,1)
        m.cliquer(1, 1, 0); // Haut
        m.cliquer(1, 1, 1); // Gauche
        m.cliquer(1, 1, 2); // Droite
        m.cliquer(1, 1, 3); // Bas

        // Vérifier que les 4 traits sont PLEINS
        Case centre = m.getCase(1, 1);
        assertEquals(ValeurTrait.PLEIN, centre.getTrait(0).getEtat());
        assertEquals(ValeurTrait.PLEIN, centre.getTrait(1).getEtat());
        assertEquals(ValeurTrait.PLEIN, centre.getTrait(2).getEtat());
        assertEquals(ValeurTrait.PLEIN, centre.getTrait(3).getEtat());
    }

    @Test
    public void testMatriceToString() {
        // Vérifier que toString ne plante pas
        String representation = matrice.toString();
        assertNotNull(representation);
        assertFalse(representation.isEmpty());
    }
}
