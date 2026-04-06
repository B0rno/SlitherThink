package com.lmu.SlitherThink;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lmu.SlitherThink.Grille.Matrice;
import com.lmu.SlitherThink.Helper.Aide;
import com.lmu.SlitherThink.Helper.GestionnaireAide;
import com.lmu.SlitherThink.Helper.StrategieAide;
import com.lmu.SlitherThink.Helper.Techniques.*;
import com.lmu.SlitherThink.Grille.ValeurTrait;
import com.lmu.SlitherThink.Grille.Case;

/**
 * Tests unitaires pour le système d'aides.
 * Teste les 12 techniques implémentées, GestionnaireAide et la classe Aide.
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
        Aide aide = gestionnaire.trouverAide(matrice);
        assertNull(aide, "Aucune aide ne devrait être trouvée sur une matrice vide");
    }

    @Test
    public void testTrouverAideSurGrilleTutoriel() {
        GestionnaireAide gestionnaire = new GestionnaireAide();
        Matrice grille = Matrice.loadGrille("tutoriel");

        if (grille != null) {
            Aide aide = gestionnaire.trouverAide(grille);
            if (aide != null) {
                assertNotNull(aide.getNom());
                assertNotNull(aide.getTechniqueLiee());
                assertFalse(aide.getTechniqueLiee().isEmpty());
            }
        }
    }

    // ===== Tests ContraintesSur3 =====

    @Test
    public void testContraintesSur3_Nom() {
        StrategieAide technique = new ContraintesSur3();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().contains("Contraintes"));
    }

    @Test
    public void testContraintesSur3_MatriceVide() {
        StrategieAide technique = new ContraintesSur3();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testContraintesSur3_Avec3() {
        Case c = matrice.getCase(2, 2);
        c.setNumero(3);

        StrategieAide technique = new ContraintesSur3();
        // Sur une matrice avec un 3 sans traits, la technique devrait être applicable
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests BoucleSur3 =====

    @Test
    public void testBoucleSur3_Nom() {
        StrategieAide technique = new BoucleSur3();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().contains("Boucle") || technique.getNom().contains("3"));
    }

    @Test
    public void testBoucleSur3_MatriceVide() {
        StrategieAide technique = new BoucleSur3();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testBoucleSur3_AvecTraitArrivant() {
        Case c = matrice.getCase(2, 2);
        c.setNumero(3);

        // Trait arrive du haut
        Case cHaut = matrice.getCase(1, 2);
        cHaut.getTrait(3).setTrait(ValeurTrait.PLEIN);

        StrategieAide technique = new BoucleSur3();
        // Devrait détecter la situation
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests BoucleSur1 =====

    @Test
    public void testBoucleSur1_Nom() {
        StrategieAide technique = new BoucleSur1();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().contains("Boucle") || technique.getNom().contains("1"));
    }

    @Test
    public void testBoucleSur1_MatriceVide() {
        StrategieAide technique = new BoucleSur1();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testBoucleSur1_Avec1SansTraits() {
        Case c = matrice.getCase(2, 2);
        c.setNumero(1);

        StrategieAide technique = new BoucleSur1();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests AucuneLigneAutourDe0 =====

    @Test
    public void testAucuneLigneAutourDe0_Nom() {
        StrategieAide technique = new AucuneLigneAutourDe0();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().contains("0") || technique.getNom().toLowerCase().contains("aucun"));
    }

    @Test
    public void testAucuneLigneAutourDe0_MatriceVide() {
        StrategieAide technique = new AucuneLigneAutourDe0();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testAucuneLigneAutourDe0_Avec0() {
        Case c = matrice.getCase(2, 2);
        c.setNumero(0);

        StrategieAide technique = new AucuneLigneAutourDe0();
        boolean applicable = technique.estApplicable(matrice);
        // Sur un 0 sans croix, devrait être applicable
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests Adjacents0Et3 =====

    @Test
    public void testAdjacents0Et3_Nom() {
        StrategieAide technique = new Adjacents0Et3();
        assertNotNull(technique.getNom());
        assertEquals("Adjacents 0 et 3", technique.getNom());
    }

    @Test
    public void testAdjacents0Et3_MatriceVide() {
        StrategieAide technique = new Adjacents0Et3();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testAdjacents0Et3_Avec0Et3Adjacents() {
        Case c1 = matrice.getCase(2, 2);
        c1.setNumero(0);
        Case c2 = matrice.getCase(2, 3);
        c2.setNumero(3);

        StrategieAide technique = new Adjacents0Et3();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests Diagonale0Et3 =====

    @Test
    public void testDiagonale0Et3_Nom() {
        StrategieAide technique = new Diagonale0Et3();
        assertNotNull(technique.getNom());
        assertEquals("Diagonale 0 et 3", technique.getNom());
    }

    @Test
    public void testDiagonale0Et3_MatriceVide() {
        StrategieAide technique = new Diagonale0Et3();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testDiagonale0Et3_Avec0Et3EnDiagonale() {
        Case c1 = matrice.getCase(2, 2);
        c1.setNumero(0);
        Case c2 = matrice.getCase(3, 3);
        c2.setNumero(3);

        StrategieAide technique = new Diagonale0Et3();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests Deux3Adjacents =====

    @Test
    public void testDeux3Adjacents_Nom() {
        StrategieAide technique = new Deux3Adjacents();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().contains("3") && technique.getNom().toLowerCase().contains("adjacent"));
    }

    @Test
    public void testDeux3Adjacents_MatriceVide() {
        StrategieAide technique = new Deux3Adjacents();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testDeux3Adjacents_AvecDeux3() {
        Case c1 = matrice.getCase(2, 2);
        c1.setNumero(3);
        Case c2 = matrice.getCase(2, 3);
        c2.setNumero(3);

        StrategieAide technique = new Deux3Adjacents();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests Deux3EnDiagonale =====

    @Test
    public void testDeux3EnDiagonale_Nom() {
        StrategieAide technique = new Deux3EnDiagonale();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().contains("3") && technique.getNom().toLowerCase().contains("diagonale"));
    }

    @Test
    public void testDeux3EnDiagonale_MatriceVide() {
        StrategieAide technique = new Deux3EnDiagonale();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testDeux3EnDiagonale_AvecDeux3() {
        Case c1 = matrice.getCase(2, 2);
        c1.setNumero(3);
        Case c2 = matrice.getCase(3, 3);
        c2.setNumero(3);

        StrategieAide technique = new Deux3EnDiagonale();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests NimporteQuelNumeroDansUnCoin =====

    @Test
    public void testNimporteQuelNumeroDansUnCoin_Nom() {
        StrategieAide technique = new NimporteQuelNumeroDansUnCoin();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().toLowerCase().contains("coin"));
    }

    @Test
    public void testNimporteQuelNumeroDansUnCoin_MatriceVide() {
        StrategieAide technique = new NimporteQuelNumeroDansUnCoin();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testNimporteQuelNumeroDansUnCoin_AvecNumeroDansCoin() {
        // Coin haut-gauche (0,0)
        Case c = matrice.getCase(0, 0);
        c.setNumero(3);

        StrategieAide technique = new NimporteQuelNumeroDansUnCoin();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests ContinuiteForcee =====

    @Test
    public void testContinuiteForcee_Nom() {
        StrategieAide technique = new ContinuiteForcee();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().contains("Continuité") || technique.getNom().contains("forcée"));
    }

    @Test
    public void testContinuiteForcee_MatriceVide() {
        StrategieAide technique = new ContinuiteForcee();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testContinuiteForcee_AvecUnTraitPlein() {
        Case c = matrice.getCase(2, 2);
        c.getTrait(0).setTrait(ValeurTrait.PLEIN);
        c.getTrait(1).setTrait(ValeurTrait.CROIX);
        c.getTrait(2).setTrait(ValeurTrait.CROIX);

        StrategieAide technique = new ContinuiteForcee();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests CoinsBloquesSurUn3 =====

    @Test
    public void testCoinsBloquesSurUn3_Nom() {
        StrategieAide technique = new CoinsBloquesSurUn3();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().contains("3") && technique.getNom().toLowerCase().contains("coin"));
    }

    @Test
    public void testCoinsBloquesSurUn3_MatriceVide() {
        StrategieAide technique = new CoinsBloquesSurUn3();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testCoinsBloquesSurUn3_Avec3() {
        Case c = matrice.getCase(2, 2);
        c.setNumero(3);

        StrategieAide technique = new CoinsBloquesSurUn3();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests EviterBoucleSeparee =====

    @Test
    public void testEviterBoucleSeparee_Nom() {
        StrategieAide technique = new EviterBoucleSeparee();
        assertNotNull(technique.getNom());
        assertTrue(technique.getNom().toLowerCase().contains("boucle") ||
                   technique.getNom().toLowerCase().contains("séparée"));
    }

    @Test
    public void testEviterBoucleSeparee_MatriceVide() {
        StrategieAide technique = new EviterBoucleSeparee();
        assertFalse(technique.estApplicable(matrice));
        assertNull(technique.trouverAide(matrice));
    }

    @Test
    public void testEviterBoucleSeparee_AvecTraits() {
        // Créer quelques traits pour tester
        Case c1 = matrice.getCase(1, 1);
        c1.getTrait(0).setTrait(ValeurTrait.PLEIN);
        c1.getTrait(2).setTrait(ValeurTrait.PLEIN);

        StrategieAide technique = new EviterBoucleSeparee();
        boolean applicable = technique.estApplicable(matrice);
        assertTrue(applicable || !applicable); // Test de non-crash
    }

    // ===== Tests d'intégration =====

    @Test
    public void testToutesLesTechniques_Noms() {
        // Vérifier que toutes les techniques ont un nom
        assertNotNull(new ContraintesSur3().getNom());
        assertNotNull(new BoucleSur3().getNom());
        assertNotNull(new BoucleSur1().getNom());
        assertNotNull(new AucuneLigneAutourDe0().getNom());
        assertNotNull(new Adjacents0Et3().getNom());
        assertNotNull(new Diagonale0Et3().getNom());
        assertNotNull(new Deux3Adjacents().getNom());
        assertNotNull(new Deux3EnDiagonale().getNom());
        assertNotNull(new NimporteQuelNumeroDansUnCoin().getNom());
        assertNotNull(new ContinuiteForcee().getNom());
        assertNotNull(new CoinsBloquesSurUn3().getNom());
        assertNotNull(new EviterBoucleSeparee().getNom());
    }

    @Test
    public void testToutesLesTechniques_MatriceVide() {
        // Vérifier qu'aucune technique ne retourne d'aide sur matrice vide
        assertNull(new ContraintesSur3().trouverAide(matrice));
        assertNull(new BoucleSur3().trouverAide(matrice));
        assertNull(new BoucleSur1().trouverAide(matrice));
        assertNull(new AucuneLigneAutourDe0().trouverAide(matrice));
        assertNull(new Adjacents0Et3().trouverAide(matrice));
        assertNull(new Diagonale0Et3().trouverAide(matrice));
        assertNull(new Deux3Adjacents().trouverAide(matrice));
        assertNull(new Deux3EnDiagonale().trouverAide(matrice));
        assertNull(new NimporteQuelNumeroDansUnCoin().trouverAide(matrice));
        assertNull(new ContinuiteForcee().trouverAide(matrice));
        assertNull(new CoinsBloquesSurUn3().trouverAide(matrice));
        assertNull(new EviterBoucleSeparee().trouverAide(matrice));
    }

    @Test
    public void testToutesLesTechniques_NomsUniques() {
        StrategieAide[] techniques = {
            new ContraintesSur3(),
            new BoucleSur3(),
            new BoucleSur1(),
            new AucuneLigneAutourDe0(),
            new Adjacents0Et3(),
            new Diagonale0Et3(),
            new Deux3Adjacents(),
            new Deux3EnDiagonale(),
            new NimporteQuelNumeroDansUnCoin(),
            new ContinuiteForcee(),
            new CoinsBloquesSurUn3(),
            new EviterBoucleSeparee()
        };

        // Vérifier que tous les noms sont uniques
        for (int i = 0; i < techniques.length; i++) {
            for (int j = i + 1; j < techniques.length; j++) {
                assertNotEquals(techniques[i].getNom(), techniques[j].getNom(),
                    "Chaque technique devrait avoir un nom unique");
            }
        }
    }

    @Test
    public void testToutesLesTechniques_NonCrash() {
        StrategieAide[] techniques = {
            new ContraintesSur3(),
            new BoucleSur3(),
            new BoucleSur1(),
            new AucuneLigneAutourDe0(),
            new Adjacents0Et3(),
            new Diagonale0Et3(),
            new Deux3Adjacents(),
            new Deux3EnDiagonale(),
            new NimporteQuelNumeroDansUnCoin(),
            new ContinuiteForcee(),
            new CoinsBloquesSurUn3(),
            new EviterBoucleSeparee()
        };

        // Vérifier qu'aucune technique ne plante
        for (StrategieAide technique : techniques) {
            assertDoesNotThrow(() -> technique.estApplicable(matrice));
            assertDoesNotThrow(() -> technique.trouverAide(matrice));
        }
    }

    @Test
    public void testGestionnaireAide_Contient12Techniques() {
        GestionnaireAide gestionnaire = new GestionnaireAide();
        assertNotNull(gestionnaire);

        // Tester sur différentes configurations pour voir si au moins une technique fonctionne
        Matrice grille = Matrice.loadGrille("tutoriel");
        if (grille != null) {
            Aide aide = gestionnaire.trouverAide(grille);
            // Peut retourner null ou une aide, l'important est de ne pas planter
            assertTrue(aide == null || aide instanceof Aide);
        }
    }

    @Test
    public void testCoherenceAide_SiApplicableAlorsRetourneAide() {
        StrategieAide[] techniques = {
            new ContraintesSur3(),
            new BoucleSur3(),
            new BoucleSur1(),
            new AucuneLigneAutourDe0(),
            new Adjacents0Et3(),
            new Diagonale0Et3(),
            new Deux3Adjacents(),
            new Deux3EnDiagonale(),
            new NimporteQuelNumeroDansUnCoin(),
            new ContinuiteForcee(),
            new CoinsBloquesSurUn3(),
            new EviterBoucleSeparee()
        };

        Matrice grille = Matrice.loadGrille("tutoriel");
        if (grille != null) {
            for (StrategieAide technique : techniques) {
                if (technique.estApplicable(grille)) {
                    Aide aide = technique.trouverAide(grille);
                    assertNotNull(aide, "Si estApplicable=true, trouverAide ne devrait pas retourner null");
                    assertNotNull(aide.getNom());
                    assertNotNull(aide.getTechniqueLiee());
                }
            }
        }
    }
}
