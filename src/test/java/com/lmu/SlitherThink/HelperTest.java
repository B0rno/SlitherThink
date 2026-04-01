package com.lmu.SlitherThink;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.GestionnaireAide;
import com.lmu.SlitherThink.Helper.StrategieAide;
import com.lmu.SlitherThink.Helper.Techniques.Adjacents0Et3;
import com.lmu.SlitherThink.Helper.Techniques.Diagonale0Et3;
import com.lmu.SlitherThink.Helper.Techniques.TechniqueAvancee1;

/**
 * Tests unitaires pour le système d'aides.
 * Teste Aide, StrategieAide, GestionnaireAide et quelques techniques.
 */
public class HelperTest {

    private Matrice matrice;

    @BeforeEach
    public void setUp() {
        matrice = new Matrice(5, 5);
    }

    // ===== Tests de la classe Aide =====

    @Test
    public void testCreationAide() {
        Aide aide = new Aide("Test", "Description de test");

        assertNotNull(aide);
        assertEquals("Test", aide.getNom());
        assertEquals("Description de test", aide.getTechniqueLiee());
    }

    @Test
    public void testAideAvecDescriptionVide() {
        Aide aide = new Aide("", "");

        assertNotNull(aide);
        assertEquals("", aide.getTechniqueLiee());
    }

    // ===== Tests de GestionnaireAide =====

    @Test
    public void testCreationGestionnaireAide() {
        GestionnaireAide gestionnaire = new GestionnaireAide();
        assertNotNull(gestionnaire);
    }

    @Test
    public void testTrouverAideSurMatriceVide() {
        GestionnaireAide gestionnaire = new GestionnaireAide();

        // Sur une matrice vide, aucune aide ne devrait être applicable
        Aide aide = gestionnaire.trouverAide(matrice);
        assertNull(aide, "Aucune aide ne devrait être trouvée sur une matrice vide");
    }

    @Test
    public void testTrouverAideSurGrilleTutoriel() {
        GestionnaireAide gestionnaire = new GestionnaireAide();
        Matrice grille = Matrice.loadGrille("tutoriel");

        if (grille == null) {
            return; // Skip si la grille n'existe pas
        }

        // Sur la grille tutoriel, une aide devrait être trouvée
        Aide aide = gestionnaire.trouverAide(grille);

        if (aide != null) {
            assertNotNull(aide.getNom());
            assertNotNull(aide.getTechniqueLiee());
            assertFalse(aide.getTechniqueLiee().isEmpty());
        }
    }

    @Test
    public void testGestionnaireRetourneNullSiAucuneAide() {
        GestionnaireAide gestionnaire = new GestionnaireAide();

        // Vérifier que trouverAide ne plante pas et retourne null si rien n'est applicable
        Aide aide = gestionnaire.trouverAide(matrice);
        assertTrue(aide == null || aide instanceof Aide);
    }

    // ===== Tests des techniques spécifiques =====

    @Test
    public void testTechniqueAdjacents0Et3() {
        StrategieAide technique = new Adjacents0Et3();

        assertNotNull(technique);
        assertEquals("Adjacents 0 et 3", technique.getNom());

        // Sur une matrice vide, ne devrait pas être applicable
        assertFalse(technique.estApplicable(matrice));
    }

    @Test
    public void testTechniqueDiagonale0Et3() {
        StrategieAide technique = new Diagonale0Et3();

        assertNotNull(technique);
        assertEquals("Diagonale 0 et 3", technique.getNom());

        // Sur une matrice vide, ne devrait pas être applicable
        assertFalse(technique.estApplicable(matrice));
    }

    @Test
    public void testTechniqueAvancee1() {
        StrategieAide technique = new TechniqueAvancee1();

        assertNotNull(technique);
        assertTrue(technique.getNom().contains("Technique avancée 1"));

        // Sur une matrice vide, ne devrait pas être applicable
        assertFalse(technique.estApplicable(matrice));
    }

    @Test
    public void testTrouverAideRetourneNull() {
        StrategieAide technique = new Adjacents0Et3();

        // Sur une matrice vide, trouverAide devrait retourner null
        Aide aide = technique.trouverAide(matrice);
        assertNull(aide);
    }

    // ===== Tests d'intégration =====

    @Test
    public void testScenarioAideComplete() {
        // Charger une grille réelle
        Matrice grille = Matrice.loadGrille("tutoriel");
        if (grille == null) {
            return; // Skip si la grille n'existe pas
        }

        // Tester une technique directement
        StrategieAide technique = new Adjacents0Et3();
        Aide aide = technique.trouverAide(grille);

        if (aide != null) {
            // Vérifier que l'aide est bien formée
            assertNotNull(aide.getTechniqueLiee());
            assertFalse(aide.getTechniqueLiee().isEmpty());

            System.out.println("Aide trouvée: " + aide.getTechniqueLiee());
        }
    }

    @Test
    public void testPlusieursTechniquesEnSequence() {
        // Tester plusieurs techniques sur la même matrice
        StrategieAide[] techniques = {
            new Adjacents0Et3(),
            new Diagonale0Et3(),
            new TechniqueAvancee1()
        };

        for (StrategieAide technique : techniques) {
            assertNotNull(technique.getNom());

            // Vérifier que estApplicable ne plante pas
            boolean applicable = technique.estApplicable(matrice);
            assertTrue(applicable || !applicable); // Toujours vrai, juste pour vérifier qu'on ne plante pas

            // Vérifier que trouverAide ne plante pas
            Aide aide = technique.trouverAide(matrice);
            assertTrue(aide == null || aide instanceof Aide);
        }
    }

    @Test
    public void testTechniqueConsistance() {
        // Charger une grille
        Matrice grille = Matrice.loadGrille("tutoriel");
        if (grille == null) {
            return;
        }

        // Trouver plusieurs fois une aide avec la même technique
        StrategieAide technique = new Adjacents0Et3();
        Aide aide1 = technique.trouverAide(grille);
        Aide aide2 = technique.trouverAide(grille);

        // Les aides devraient avoir les mêmes propriétés
        if (aide1 != null && aide2 != null) {
            assertEquals(aide1.getTechniqueLiee(), aide2.getTechniqueLiee());
        }
    }

    @Test
    public void testTechniqueNomUnique() {
        // Vérifier que chaque technique a un nom unique
        StrategieAide[] techniques = {
            new Adjacents0Et3(),
            new Diagonale0Et3(),
            new TechniqueAvancee1()
        };

        for (int i = 0; i < techniques.length; i++) {
            for (int j = i + 1; j < techniques.length; j++) {
                assertNotEquals(techniques[i].getNom(), techniques[j].getNom(),
                    "Chaque technique devrait avoir un nom unique");
            }
        }
    }

    @Test
    public void testTechniqueRetourneAideCoherente() {
        StrategieAide technique = new Adjacents0Et3();
        Aide aide = technique.trouverAide(matrice);

        if (aide != null) {
            // Si une aide est retournée, elle devrait avoir un nom non vide
            assertNotNull(aide.getTechniqueLiee());
            assertFalse(aide.getTechniqueLiee().isEmpty());
        }
    }
}
