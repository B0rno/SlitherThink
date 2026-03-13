package com.lmu.SlitherThink;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lmu.SlitherThink.Partie.*;

/**
 * Tests unitaires pour le package Partie.
 */
public class PartieTest {

    private Profil profil;
    private Partie partie;

    @BeforeEach
    public void setUp() {
        profil = new Profil("TestUser");
        partie = new Partie(profil, 5, 5);
    }

    @Test
    public void testCreationProfil() {
        assertEquals("TestUser", profil.getPseudo());
        assertNotNull(profil);
    }

    @Test
    public void testCreationPartie() {
        assertNotNull(partie);
        assertEquals(3, partie.getNbAides());
        assertNotNull(partie.getMatrice());
        assertNotNull(partie.getScore());
    }

    @Test
    public void testCycleDeVie() {
        // Démarrer depuis INIT
        partie.demarrer();

        // Mettre en pause
        partie.mettreEnPause();

        // Reprendre depuis PAUSE
        partie.demarrer();

        assertTrue(true); // Si pas d'exception, le test passe
    }

    @Test
    public void testObserver() {
        final boolean[] notifie = {false};

        PartieObserver observer = new PartieObserver() {
            @Override
            public void onVictoire(Score score) {
                notifie[0] = true;
            }

            @Override
            public void onEtatChange(EtatPartie etat) {
                assertNotNull(etat);
            }

            @Override
            public void onAideUtilisee(int restantes) {
                assertTrue(restantes >= 0 && restantes <= 3);
            }
        };

        partie.ajouterObserver(observer);
        partie.demarrer();

        assertNotNull(observer);
    }

    @Test
    public void testUtilisationAide() {
        partie.demarrer();

        assertTrue(partie.peutUtiliserAide());
        assertEquals(3, partie.getNbAides());

        partie.utiliserAide();
        assertEquals(2, partie.getNbAides());
        assertEquals(1, partie.getScore().getNbAidesUtilisees());
    }

    @Test
    public void testJouerCoup() {
        partie.demarrer();

        boolean coupJoue = partie.jouerCoup(0, 0, 1);
        assertTrue(coupJoue);
        assertEquals(1, partie.getScore().getNbCoups());
    }

    @Test
    public void testScore() {
        Score score = partie.getScore();

        assertNotNull(score);
        assertEquals(0, score.getNbCoups());
        assertEquals(0, score.getNbAidesUtilisees());
        assertEquals(0, score.getEtoiles());
    }

    @Test
    public void testProfilHistorique() {
        Score s1 = new Score();
        Score s2 = new Score();

        profil.ajouterScore(s1);
        profil.ajouterScore(s2);

        assertNotNull(profil.getMeilleurScore());
    }
}
