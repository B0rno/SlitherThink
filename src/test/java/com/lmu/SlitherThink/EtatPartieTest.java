package com.lmu.SlitherThink;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.lmu.SlitherThink.Partie.EtatPartie;

/**
 * Tests unitaires pour l'enum EtatPartie.
 * Teste les états et les méthodes de garde.
 */
public class EtatPartieTest {

    @Test
    public void testValeurs() {
        // Vérifier que l'enum a 4 valeurs
        EtatPartie[] etats = EtatPartie.values();
        assertEquals(4, etats.length);
    }

    @Test
    public void testValueOf() {
        assertEquals(EtatPartie.INIT, EtatPartie.valueOf("INIT"));
        assertEquals(EtatPartie.EN_COURS, EtatPartie.valueOf("EN_COURS"));
        assertEquals(EtatPartie.PAUSE, EtatPartie.valueOf("PAUSE"));
        assertEquals(EtatPartie.TERMINE, EtatPartie.valueOf("TERMINE"));
    }

    // ===== Tests des méthodes de garde =====

    @Test
    public void testPeutJouerENIT() {
        assertFalse(EtatPartie.INIT.peutJouer(),
            "Ne devrait pas pouvoir jouer en état INIT");
    }

    @Test
    public void testPeutJouerENCOURS() {
        assertTrue(EtatPartie.EN_COURS.peutJouer(),
            "Devrait pouvoir jouer en état EN_COURS");
    }

    @Test
    public void testPeutJouerPAUSE() {
        assertFalse(EtatPartie.PAUSE.peutJouer(),
            "Ne devrait pas pouvoir jouer en état PAUSE");
    }

    @Test
    public void testPeutJouerTERMINE() {
        assertFalse(EtatPartie.TERMINE.peutJouer(),
            "Ne devrait pas pouvoir jouer en état TERMINE");
    }

    @Test
    public void testPeutDemarrerINIT() {
        assertTrue(EtatPartie.INIT.peutDemarrer(),
            "Devrait pouvoir démarrer depuis INIT");
    }

    @Test
    public void testPeutDemarrerPAUSE() {
        assertTrue(EtatPartie.PAUSE.peutDemarrer(),
            "Devrait pouvoir démarrer depuis PAUSE");
    }

    @Test
    public void testPeutDemarrerENCOURS() {
        assertFalse(EtatPartie.EN_COURS.peutDemarrer(),
            "Ne devrait pas pouvoir démarrer en état EN_COURS");
    }

    @Test
    public void testPeutDemarrerTERMINE() {
        assertFalse(EtatPartie.TERMINE.peutDemarrer(),
            "Ne devrait pas pouvoir démarrer en état TERMINE");
    }

    @Test
    public void testPeutMettreEnPauseENCOURS() {
        assertTrue(EtatPartie.EN_COURS.peutMettreEnPause(),
            "Devrait pouvoir mettre en pause depuis EN_COURS");
    }

    @Test
    public void testPeutMettreEnPauseINIT() {
        assertFalse(EtatPartie.INIT.peutMettreEnPause(),
            "Ne devrait pas pouvoir mettre en pause en état INIT");
    }

    @Test
    public void testPeutMettreEnPausePAUSE() {
        assertFalse(EtatPartie.PAUSE.peutMettreEnPause(),
            "Ne devrait pas pouvoir mettre en pause en état PAUSE");
    }

    @Test
    public void testPeutMettreEnPauseTERMINE() {
        assertFalse(EtatPartie.TERMINE.peutMettreEnPause(),
            "Ne devrait pas pouvoir mettre en pause en état TERMINE");
    }

    // ===== Tests des transitions d'état =====

    @Test
    public void testTransitionINITversENCOURS() {
        // Cette transition devrait être autorisée
        assertTrue(EtatPartie.INIT.peutDemarrer());
    }

    @Test
    public void testTransitionENCOURSversPAUSE() {
        // Cette transition devrait être autorisée
        assertTrue(EtatPartie.EN_COURS.peutMettreEnPause());
    }

    @Test
    public void testTransitionPAUSEversENCOURS() {
        // Cette transition devrait être autorisée (reprise)
        assertTrue(EtatPartie.PAUSE.peutDemarrer());
    }

    @Test
    public void testTransitionINITversTERMINE() {
        // Cette transition ne devrait PAS être directement autorisée
        assertFalse(EtatPartie.INIT.peutJouer());
    }

    // ===== Tests de cohérence =====

    @Test
    public void testCoherenceJouer() {
        // Seul EN_COURS devrait permettre de jouer
        int compteur = 0;
        for (EtatPartie etat : EtatPartie.values()) {
            if (etat.peutJouer()) {
                compteur++;
            }
        }
        assertEquals(1, compteur, "Seul un état devrait permettre de jouer");
    }

    @Test
    public void testCoherenceDemarrer() {
        // INIT et PAUSE devraient permettre de démarrer
        int compteur = 0;
        for (EtatPartie etat : EtatPartie.values()) {
            if (etat.peutDemarrer()) {
                compteur++;
            }
        }
        assertEquals(2, compteur, "Deux états devraient permettre de démarrer");
    }

    @Test
    public void testCoherenceMettreEnPause() {
        // Seul EN_COURS devrait permettre de mettre en pause
        int compteur = 0;
        for (EtatPartie etat : EtatPartie.values()) {
            if (etat.peutMettreEnPause()) {
                compteur++;
            }
        }
        assertEquals(1, compteur, "Seul un état devrait permettre de mettre en pause");
    }
}
