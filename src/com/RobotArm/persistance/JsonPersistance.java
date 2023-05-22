package com.RobotArm.persistance;

import com.RobotArm.adapter.JsonAdapter;
import com.RobotArm.business.Gamme;
import com.RobotArm.business.Operation;
import com.RobotArm.business.Tache;
import com.RobotArm.business.Utilisateur;
import com.RobotArm.enumeration.TypeAction;
import com.RobotArm.exception.GammeNotFoundException;
import com.RobotArm.exception.UnableToReadGammesException;
import com.RobotArm.exception.UnableToReadUsersException;
import com.RobotArm.exception.UserNotFoundException;
import com.RobotArm.interfaces.IPersistance;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JsonPersistance implements IPersistance {
    private final JsonAdapter<Gamme> gammeAdapter;
    private final String fichierGammes;
    private final JsonAdapter<Utilisateur> userAdapter;
    private ArrayList<Gamme> gammes;
    private static final Logger LOGGER = Logger.getLogger(JsonPersistance.class.getName());


    public JsonPersistance(String fichierGammes, String fichierUsers) throws IOException {
        this.fichierGammes = fichierGammes;
        this.gammeAdapter = new JsonAdapter<>();

        this.userAdapter = new JsonAdapter<>();
    }

    @Override
    public void creerGamme(Gamme gamme) {
        try {
            this.gammes = getGammes();
            if (gammes == null) {
                gammes = new ArrayList<>();
            }
            gammes.add(gamme);
            try (FileWriter gammeWriter = new FileWriter(this.fichierGammes)) {
                this.gammeAdapter.serializeAll(gammes, gammeWriter);
            }
            //LOGGER.info("Gamme créée avec succès : " + gamme.getId());
        } catch (IOException e) {
            System.out.println("Impossible de stocker la gamme");
        }
    }

    @Override
    public void modifierGamme(Gamme gammeAModifier)  {
        try {
            // Récupérer toutes les gammes
            ArrayList<Gamme> gammes = getGammes();
            // Parcourir la liste des gammes
            for (int i = 0; i < gammes.size(); i++) {
                Gamme gamme = gammes.get(i);
                if (gamme.getId().equals(gammeAModifier.getId())) {
                    gammes.set(i, gammeAModifier);
                    break;
                }
            }
            // Écrire les modifications dans le fichier
            try (FileWriter gammeWriter = new FileWriter(this.fichierGammes)) {
                this.gammeAdapter.serializeAll(gammes, gammeWriter);
            }
        } catch (IOException e) {
            LOGGER.severe("Impossible de modifier la gamme : " + e.getMessage());
        }
    }

    @Override
    public void supprimerGamme(String paramString) {
        Gamme gamme;
        try {
            gamme = this.findGamme(paramString);

            this.gammes = getGammes();
            if (gammes == null) {
                gammes = new ArrayList<>();
            }
            gammes.remove(gamme);

            try (FileWriter gammeWriter = new FileWriter(this.fichierGammes)) {
                this.gammeAdapter.serializeAll(gammes, gammeWriter);
            }
        } catch (IOException e) {
            LOGGER.severe("Impossible de stocker la gamme : " + e.getMessage());
        } catch (GammeNotFoundException e) {
            e.getMessage();
        }
    }


    @Override
    public ArrayList<Gamme> getGammes() throws UnableToReadGammesException {
        ArrayList<Gamme> gammes = null;
        try (FileReader gammeReader = new FileReader(this.fichierGammes)) {
            gammes = this.gammeAdapter.deserialize(gammeReader, new TypeToken<List<Gamme>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gammes == null) {
            gammes = new ArrayList<>();
        }
        return gammes;

    }

    @Override
    public Gamme findGamme(String id) throws GammeNotFoundException {
        try {
            ArrayList<Gamme> gammes = getGammes();

            for (Gamme gamme : gammes) {
                if (gamme.getId().equals(id)) {
                    return gamme;
                }
            }
        } catch (UnableToReadGammesException e) {
            LOGGER.severe("Impossible de lire les gammes: " + e.getMessage());
            e.printStackTrace();
        }
        throw new GammeNotFoundException();
    }

    @Override
    public Gamme recupererGammeDefaut() {
        Gamme g = new Gamme("1", "Gamme defaut");

        Operation ope = new Operation("1", "Gamme défaut");

        Tache t1 = new Tache("1", "Tourner à gauche", TypeAction.TournerGauche);
        Tache t2 = new Tache("2", "Tourner à droite", TypeAction.TournerDroite);
        Tache t3 = new Tache("3", "Ouvre la pince", TypeAction.Attraper);
        Tache t4 = new Tache("4", "Ferme la pince", TypeAction.Poser);
        Tache t5 = new Tache("7", "Attendre 2 secondes", TypeAction.Attendre);


        // Saisir un objet
        ope.AjouterTache(t3);
        ope.AjouterTache(t1);
        ope.AjouterTache(t5);
        ope.AjouterTache(t4);
        ope.AjouterTache(t2);

        g.AjouterOperation(ope);
        return g;
    }


    @Override
    public void sauverLog(String paramString) {

    }

    @Override
    public ArrayList<String> recupererLogs() {
        return null;
    }

    @Override
    public String getConfig(String paramString) {
        return null;
    }

    @Override
    public void createUser(String login, String pwd) {
        String jsonContent = userAdapter.serialize(new Utilisateur(login, pwd, false));
    }

    @Override
    public Utilisateur findUser(String login, String pwd) throws UserNotFoundException {
        try {
            ArrayList<Utilisateur> users = getUsers();

            for (Utilisateur user : users) {
                if (user.getLogin().equals(login) && user.getPwd().equals(pwd))
                    return user;
            }
        } catch (UnableToReadUsersException e) {
            e.printStackTrace();
        }
        throw new UserNotFoundException();
    }

    @Override
    public ArrayList<Utilisateur> getUsers() throws UnableToReadUsersException {
        return new ArrayList<>();
    }

    @Override
    public void supprimerCompte(String login) {

    }
}
