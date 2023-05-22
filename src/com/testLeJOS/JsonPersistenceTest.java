package com.testLeJOS;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Operation;
import com.RobotArm.business.Tache;
import com.RobotArm.exception.GammeNotFoundException;
import com.RobotArm.interfaces.IPersistance;
import com.RobotArm.persistance.JsonPersistance;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JsonPersistenceTest {	private IPersistance persistance;
    private Gamme defaultGamme;

    String fichierGammesTest = "test_gammes.json";
    String fichierUsersTest = "test_users.json";

    private static final Logger LOGGER = Logger.getLogger(JsonPersistance.class.getName());

    public void setUp() throws IOException {
        resetTestGammesFile(fichierGammesTest);
        persistance = null;
        defaultGamme = null;
        this.persistance = new JsonPersistance(fichierGammesTest, fichierUsersTest);
        for (int i = 0; i < 5; i++) {
            Gamme gamme = creerGammeDefaut(i);
            this.persistance.creerGamme(gamme);
        }
        this.defaultGamme = this.persistance.getGammes().get(0);
    }

    private void resetTestGammesFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete(); // Supprimez le fichier existant s'il existe
        }
        file.createNewFile(); // Créez un nouveau fichier

    }

    private Gamme creerGammeDefaut(int id) {
        Gamme g = new Gamme(Integer.toString(id), "Gamme "+ id);


        Operation ope = new Operation(Integer.toString(id), "Opération " + id);

        Tache t1 = new Tache("1", "Tourner à gauche", -90);
        Tache t2 = new Tache("2", "Tourner à droite", 90);
        Tache t3 = new Tache("3", "Ouvre la pince", -180);
        Tache t4 = new Tache("4", "Ferme la pince", 180);
        Tache t5 = new Tache("5", "Baisse le bras", -120);
        Tache t6 = new Tache("6", "Monter le bras", 120);
        Tache t7 = new Tache("7", "Attendre 1 secondes", 1000);

        // Saisir un objet
        ope.AjouterTache(t3);
        ope.AjouterTache(t5);
        ope.AjouterTache(t4);
        ope.AjouterTache(t6);
        ope.AjouterTache(t1);
        ope.AjouterTache(t5);
        ope.AjouterTache(t3);
        ope.AjouterTache(t6);
        ope.AjouterTache(t4);
        ope.AjouterTache(t7);
        ope.AjouterTache(t2);

        g.AjouterOperation(ope);
        return g;
    }

    @Test
    public void isGammeCreee() throws IOException {
        resetTestGammesFile(fichierGammesTest);
        persistance = new JsonPersistance(fichierGammesTest, fichierUsersTest);

        Gamme nouvelleGamme = creerGammeDefaut(0); // ou utilisez 1 si vous préférez commencer à partir de 1
        persistance.creerGamme(nouvelleGamme);

        try {
            Gamme gamme = persistance.findGamme(nouvelleGamme.getId());
            assertNotNull(gamme);
            assertEquals(nouvelleGamme.getId(), gamme.getId());
        } catch (GammeNotFoundException e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void isGammeSupprimee() throws IOException {
        setUp();
        persistance.supprimerGamme(defaultGamme.getId());
        try {
            persistance.findGamme(defaultGamme.getId());
            fail("La suppression de la gamme a échoué");
        } catch (GammeNotFoundException e) {
        }
    }

    @Test
    public void isGammeTrouvee() throws IOException {
        setUp();
        try {
            Gamme gamme = persistance.findGamme(defaultGamme.getId());
            assertNotNull(gamme);
            assertEquals(defaultGamme.getId(), gamme.getId());
        } catch (GammeNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void isGammesRecuperees() throws IOException {
        setUp();
        ArrayList<Gamme> result = persistance.getGammes();
        assertEquals(5, result.size());
        for(int i = 0; i < 5; i++) {
            assertEquals("Expected ID of " + i + ", but got " + result.get(i).getId(), Integer.toString(i), result.get(i).getId());
        }
    }

    @Test
    public void isGammeModifiee() throws IOException {
        setUp();
        Gamme gamme = persistance.getGammes().get(0);

        String nouvelleDescription = "Nouvelle description";
        gamme.setDescription(nouvelleDescription);

        Tache nouvelleTache = new Tache("6", "Nouvelle Tâche", 500);

        Operation operation = gamme.getListeOperations().get(0);
        operation.AjouterTache(nouvelleTache);

        Operation nouvelleOperation = new Operation("6", "Nouvelle Opération");
        nouvelleOperation.AjouterTache(new Tache("1", "Tâche 1 de la nouvelle opération", 200));

        gamme.AjouterOperation(nouvelleOperation);

        persistance.modifierGamme(gamme);

        try {
            Gamme gammeModifiee = persistance.findGamme(gamme.getId());

            assertEquals(nouvelleDescription, gammeModifiee.getDescription());
            assertTrue(gammeModifiee.getListeOperations().get(0).getListeTaches().contains(nouvelleTache));
            assertTrue(gammeModifiee.getListeOperations().contains(nouvelleOperation));

        } catch (GammeNotFoundException e) {
            fail(e.getMessage());
        }
    }



}