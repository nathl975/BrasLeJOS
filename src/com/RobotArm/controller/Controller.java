package com.RobotArm.controller;

import com.RobotArm.business.BrasArticule;
import com.RobotArm.business.Gamme;
import com.RobotArm.business.ThreadGamme;
import com.RobotArm.business.Utilisateur;
import com.RobotArm.exception.GammeNotFoundException;
import com.RobotArm.exception.UnableToReadGammesException;
import com.RobotArm.interfaces.*;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Classe pilote centrale de l'application. Tous les messages entre les classes
 * transitent par cette classe, qui possède la plupart des éléments logiques de contrôle et de décision.
 *
 * @author Alvin
 */
public class Controller implements IExecuteur, IPilote {
    private final IPersistance persistance;

    private IEtatMode modeFonctionnement;

    private final IPilotage pilotage;

    private final IAdapter<Gamme> adapter;

    private final BrasArticule brasArticule;

    private final Gamme gammeDefaut;

    private Utilisateur utilisateurConnecte;

    private ThreadGamme execGammeService;

    private final Gson gson;

    boolean stop = false;

    /**
     * Constructeur de la classe
     */
    public Controller(IPersistance persistance, IPilotage pilotage, IAdapter<Gamme> adapter, BrasArticule brasArticule) throws UnableToReadGammesException {
        this.persistance = persistance;
        this.modeFonctionnement = new ModeManuel();
        this.pilotage = pilotage;
        this.adapter = adapter;
        this.brasArticule = brasArticule;
        this.gammeDefaut = this.persistance.recupererGammeDefaut();
        this.execGammeService = new ThreadGamme(this, this.brasArticule);
        this.gson = (new GsonBuilder()).create();

        // on initialise la position du robot au début du programme
        this.brasArticule.resetPositionMoteurs();
    }

    /**
     * Démarre le contrôleur et le module de pilotage et bloque le thread principal
     */
    public void start() {
        this.pilotage.ecouter();
        while (!stop) // Thread principal
        {
            while (execGammeService.gammeEnCours()) ; // Attendre que l'exécution soit possible

            if (this.modeFonctionnement.estAutonome()) {
                executeGamme(this.gammeDefaut.getId());
            }
        }
    }

    /**
     * Sauvegarde un log
     *
     * @param r le rapport à sauvegarder
     */
    public void sauverRapport(String r) {
        this.persistance.sauverLog(r);
    }

    /**
     * Demande l'exécution d'une gamme. S'il y a une gamme en cours, la gamme est refusée.
     *
     * @param id ID de la gamme à exécuter
     */
    public void executeGamme(String id) {
        System.out.println(this.modeFonctionnement.peutExecuter());
        if (this.modeFonctionnement.peutExecuter()) {
            if (!gammeEnCours()) {
                Gamme gamme = null;
                try {
                    gamme = this.persistance.findGamme(id);
                    System.out.println(gamme.getId());
                } catch (GammeNotFoundException e) {
                    e.getMessage();
                }

                this.execGammeService.executer(gamme);
            } else {
                pilotage.envoyerMessage("Gamme deja en cours.");
            }
        }
    }

    /**
     * Implémentation de la classe IExecuteur, notifie le contrôleur de la fin d'une gamme
     */
    public void notifierFinGamme() {
        pilotage.envoyerMessage("Gamme terminée !");

        this.brasArticule.resetPositionMoteurs();
    }

    /**
     * Filtre les logs par date de sauvegarde
     *
     * @param date Date de filtrage
     * @return Liste des logs d'une date donnée
     */
    public ArrayList<String> filtrerLog(Date date) {
        return this.persistance.recupererLogs();
    }

    /**
     * Appelée lorsque le pilotage reçoit un message de l'opérateur. Détermine le type
     * du message et l'action à mener en conséquence.
     *
     * @param msg Message reçu
     */
    public void notifierMessage(String msg) {
        JsonObject root;
        try {
            root = JsonParser.parseString(msg).getAsJsonObject();
            if (root == null)
                return;
        } catch (JsonSyntaxException e) {
            System.out.println("Message reçu invalide ! Le format JSON n'est pas respecte");
            e.printStackTrace();
            return;
        }

        String action;
        JsonElement actionJson = root.get("action");
        if (actionJson == null) {
            System.out.println("Erreur action");
        }
        action = Objects.requireNonNull(actionJson).getAsString();
        System.out.println(action);

        String login, pwd;
        // Switch-Case sur la valeur du champ "Action" du message reçu.
        // Les messages et leurs arguments sont décrits dans la documentation du logiciel.
        switch (action) {
            case "co":
                if (utilisateurConnecte == null) {
                    login = root.get("login").getAsString();
                    pwd = root.get("pwd").getAsString();
                    System.out.println(login);
                    System.out.println(pwd);
                    if (login != null && pwd != null) {
                        // TODO: implémenter le stockage d'utilisateurs
//                        try {
//                            utilisateurConnecte = persistance.findUser(login, pwd);
//                        } catch (UserNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }
//                        if (utilisateurConnecte == null)
//                            pilotage.envoyerMessage("Cet utilisateur n'existe pas !");
//                        else
//                            pilotage.envoyerMessage(String.format("Vous êtes connecte sous %s.", utilisateurConnecte.getLogin()));
                        this.utilisateurConnecte = new Utilisateur(login, pwd, true);
                    }

                } else {
                    pilotage.envoyerMessage(String.format("Vous êtes déjà connecté sous %s", utilisateurConnecte.getLogin()));
                }
                break;
            case "deco":
                utilisateurConnecte = null;
                pilotage.envoyerMessage("Vous êtes bien déconnecté.");
                break;
            case "newG":
                try {
                    if (utilisateurConnecte == null) {
                        System.out.println("Non connecté");
                        break;
                    }

                    Gamme g = this.adapter.deserialize(root.get("gamme").toString(), Gamme.class);

                    this.persistance.creerGamme(g);
                    System.out.println("Je passe");
                    System.out.printf("Nouvelle gamme créée : %s%n", g.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    this.pilotage.envoyerMessage("La gamme n'a pas pu être créée.");
                    sauverRapport(e.getMessage());
                }
                break;
            case "modG":
                try {
                    if (utilisateurConnecte == null) {
                        System.out.println("Non connecté");
                        break;
                    }

                    Gamme g = gson.fromJson(root.get("gamme").getAsJsonObject(), Gamme.class);
                    if (g != null) {

                        this.persistance.modifierGamme(g);
                    }
                    throw new Exception("Informations invalides");
                } catch (Exception e) {
                    this.pilotage.envoyerMessage("La gamme n'a pas pu être modifiée.");
                    sauverRapport(e.getMessage());
                }
                break;
            case "delG":
                try {
                    if (utilisateurConnecte == null) {
                        System.out.println("Non connecté");
                        break;
                    }

                    String g = root.get("idGamme").getAsString();
                    if (g != null) {
                        this.persistance.supprimerGamme(g);
                    }
                    throw new Exception("Informations invalides");
                } catch (Exception e) {

                    this.pilotage.envoyerMessage("La gamme n'a pas pu être supprimée.");
                    sauverRapport(e.getMessage());
                }
                break;
            case "newU":
                try {
                    if (utilisateurConnecte == null) {
                        System.out.println("Non connecté");
                        break;
                    }
                    login = root.get("login").getAsString();
                    pwd = root.get("pwd").getAsString();
                    if (login != null && pwd != null) {
                        this.persistance.createUser(login, pwd);
                    }
                    throw new Exception("Informations invalides");
                } catch (Exception e) {
                    this.pilotage.envoyerMessage("Le compte n'a pas pu être créé.");
                    sauverRapport(e.getMessage());
                }
                break;
            case "delU":
                if (utilisateurConnecte == null) {
                    System.out.println("Non connecté");
                    break;
                }
                this.persistance.supprimerCompte(root.get("login").getAsString());
                break;
            case "auto":
                if (utilisateurConnecte == null) {
                    System.out.println("Non connecté");
                    break;
                }
                if (this.modeFonctionnement.getClass().equals(ModePanne.class))
                    this.execGammeService = new ThreadGamme(this, this.brasArticule);
                this.modeFonctionnement = new ModeAutonome();
                // this.executeGamme(this.gammeDefaut.getId());
                this.execGammeService.executer(gammeDefaut);
                break;
            case "manu":
                if (utilisateurConnecte == null) {
                    System.out.println("Non connecté");
                    break;
                }
                if (this.modeFonctionnement.getClass().equals(ModePanne.class))
                    this.execGammeService = new ThreadGamme(this, this.brasArticule);
                this.modeFonctionnement = new ModeManuel();
                break;
            case "panne":
                if (utilisateurConnecte == null) {
                    System.out.println("Non connecté");
                    break;
                }
                triggerPanne();
                break;
            case "execG":
                if (utilisateurConnecte == null) {
                    System.out.println("Non connecté");
                    break;
                }
                JsonElement idGamme = root.get("idGamme");
                executeGamme(idGamme.getAsString());
                break;
            case "logs":
                if (root.get("date").getAsString() != null) {
                    ArrayList<String> rapports = filtrerLog(new Date(root.get("d").getAsString()));
                    break;
                }
                ArrayList<String> rapports = this.persistance.recupererLogs();
                this.pilotage.afficherHistorique(rapports);
                break;
            case "ping":
                System.out.println("Ping reçu");
                this.pilotage.envoyerMessage("Pong !");
                break;
            case "stop":
                stopRobot();
                break;
            default:
                System.out.printf("Commande inconnue : %s%n", action);
                break;
        }
        System.out.println("Message traite");
    }


    public void triggerPanne() {
        this.modeFonctionnement = new ModePanne();
        this.execGammeService.panne();
        System.out.println("Mode panne actif.");
    }


    public boolean gammeEnCours() {
        return this.execGammeService.gammeEnCours();
    }

    /**
     * Stoppe les services d'exécution et de pilotage, puis le contrôleur,
     * en vue d'arrêter le robot.
     */
    private void stopRobot() {
        this.pilotage.stop();
        this.execGammeService.stop();
        this.stop = true;
    }
}
