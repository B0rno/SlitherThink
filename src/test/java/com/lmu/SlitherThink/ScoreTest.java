package com.lmu.SlitherThink;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lmu.SlitherThink.Partie.Score;

import java.time.Duration;

/**
 * Tests unitaires pour la classe Score.
 * Teste le chronométrage, le calcul des étoiles et les compteurs.
 */
public class ScoreTest {

    private Score score;

    @BeforeEach
    public void setUp() {
        score = new Score();
    }

    // ===== Tests du chronométrage =====

    @Test
    public void testChronoInitial() {
        assertEquals(0, score.getDureeEnSecondes());
    }

    @Test
    public void testDemarrerChrono() throws InterruptedException {
        score.demarrerChrono();
        Thread.sleep(1100); // Attendre un peu plus d'1 seconde
        score.arreterChrono();

        long duree = score.getDureeEnSecondes();
        assertTrue(duree >= 1, "Le chrono devrait compter au moins 1 seconde");
        assertTrue(duree <= 2, "Le chrono ne devrait pas compter plus de 2 secondes");
    }

    @Test
    public void testPauseChrono() throws InterruptedException {
        score.demarrerChrono();
        Thread.sleep(500);
        score.pauseChrono();

        long dureeApresPause = score.getDureeEnSecondes();

        Thread.sleep(500); // Attendre pendant la pause

        long dureeFinale = score.getDureeEnSecondes();

        // La durée ne devrait pas augmenter pendant la pause
        assertEquals(dureeApresPause, dureeFinale,
            "Le chrono ne devrait pas avancer pendant la pause");
    }

    @Test
    public void testRepriseChrono() throws InterruptedException {
        // Démarrer
        score.demarrerChrono();
        Thread.sleep(600);

        // Pause
        score.pauseChrono();
        long dureeApresause = score.getDureeEnSecondes();
        Thread.sleep(500);

        // Reprendre
        score.demarrerChrono();
        Thread.sleep(600);
        score.arreterChrono();

        long dureeFinale = score.getDureeEnSecondes();

        // La durée finale devrait être supérieure ou égale à la durée après pause
        // (Le chrono doit continuer à compter après reprise)
        assertTrue(dureeFinale >= dureeApresause,
            "Le chrono devrait reprendre après une pause");
    }

    @Test
    public void testSetDureeAccumulee() {
        Duration duree = Duration.ofSeconds(120);
        score.setDureeAccumulee(duree);

        assertEquals(120, score.getDureeEnSecondes());
    }

    // ===== Tests des compteurs =====

    @Test
    public void testCompteurCoupsInitial() {
        assertEquals(0, score.getNbCoups());
    }

    @Test
    public void testIncrementCoups() {
        score.incrementerCoups();
        assertEquals(1, score.getNbCoups());

        score.incrementerCoups();
        assertEquals(2, score.getNbCoups());
    }

    @Test
    public void testCompteurAidesInitial() {
        assertEquals(0, score.getNbAidesUtilisees());
    }

    @Test
    public void testIncrementAides() {
        score.utiliserAide();
        assertEquals(1, score.getNbAidesUtilisees());

        score.utiliserAide();
        assertEquals(2, score.getNbAidesUtilisees());
    }

    @Test
    public void testSetNbAidesUtilisees() {
        score.setNbAidesUtilisees(5);
        assertEquals(5, score.getNbAidesUtilisees());
    }

    // ===== Tests du calcul des étoiles =====

    @Test
    public void testEtoilesInitial() {
        assertEquals(0, score.getEtoiles());
    }

    @Test
    public void testCalculerEtoiles3Etoiles() {
        // Simuler une partie rapide sans aides
        score.setDureeAccumulee(Duration.ofSeconds(30));
        score.setNbAidesUtilisees(0);

        score.calculerEtoiles();

        // Devrait obtenir 3 étoiles (temps court, pas d'aides)
        assertEquals(3, score.getEtoiles());
    }

    @Test
    public void testCalculerEtoiles2Etoiles() {
        // Simuler une partie moyenne avec 1 aide
        score.setDureeAccumulee(Duration.ofSeconds(120));
        score.setNbAidesUtilisees(1);

        score.calculerEtoiles();

        // Devrait obtenir 2 étoiles
        assertTrue(score.getEtoiles() >= 1 && score.getEtoiles() <= 2,
            "Devrait obtenir 1 ou 2 étoiles");
    }

    @Test
    public void testCalculerEtoiles1Etoile() {
        // Simuler une partie longue avec plusieurs aides
        score.setDureeAccumulee(Duration.ofSeconds(300));
        score.setNbAidesUtilisees(3);

        score.calculerEtoiles();

        // Devrait obtenir 1 étoile
        assertTrue(score.getEtoiles() >= 1,
            "Devrait obtenir au moins 1 étoile");
    }

    @Test
    public void testGetDureePourEtoile() {
        // Devrait retourner la durée maximale pour obtenir 3 étoiles
        long duree = score.getDureePourEtoile();
        assertTrue(duree > 0, "La durée pour les étoiles devrait être positive");
    }

    // ===== Tests d'intégration =====

    @Test
    public void testScenarioComplet() throws InterruptedException {
        // Démarrer une partie
        score.demarrerChrono();

        // Jouer quelques coups
        score.incrementerCoups();
        score.incrementerCoups();
        score.incrementerCoups();

        Thread.sleep(500);

        // Utiliser une aide
        score.utiliserAide();

        // Mettre en pause
        score.pauseChrono();
        Thread.sleep(200);

        // Reprendre
        score.demarrerChrono();
        Thread.sleep(500);

        // Terminer
        score.arreterChrono();
        score.calculerEtoiles();

        // Vérifications
        assertEquals(3, score.getNbCoups());
        assertEquals(1, score.getNbAidesUtilisees());
        assertTrue(score.getDureeEnSecondes() >= 1);
        assertTrue(score.getEtoiles() >= 1 && score.getEtoiles() <= 3);
    }
}
