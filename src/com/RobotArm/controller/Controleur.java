package com.RobotArm.controller;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Moteur;
import com.RobotArm.business.ThreadGamme;
import com.RobotArm.business.Utilisateur;
import com.RobotArm.interfaces.*;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Classe pilote centrale de l'application. Tous les messages entre les classes
 * transitent par cette classe, qui posséde la plupart des éléments logiques de contréle et de décision.
 *
 * @author Alvin
 */
public class Controleur implements IExecuteur, IPilote {
    HashMap<String, Gamme> listeGammes;
    Gamme gammeDefaut;
    Utilisateur utilisateurConnecte;
    IPersistance persistance;
    IEtatMode modeFonctionnement;
    IPilotage pilotage;
    ThreadGamme execGammeService;
    Gson gson;
    boolean stop;

    /**
     * Constructeur de la classe
     *
     * @param pe
     * @param pi
     * @throws Exception
     */
    public Controleur(IPersistance pe, IPilotage pi) throws Exception {
        this.persistance = pe;
        this.pilotage = pi;
        this.listeGammes = new HashMap<>();
        this.gammeDefaut = new Gamme();
        this.modeFonctionnement = new ModeManuel();
        this.execGammeService = new ThreadGamme(this);
        this.gson = (new GsonBuilder()).create();
        this.stop = false;

        initRobot();
        initGamme();
    }

    /**
     * Démarre le contréleur et le module de pilotage, et bloque le thread principal
     */
    public void demarrer() {
        System.out.println("Demarrage du controleur");
        this.pilotage.ecouter();
        while (!stop) // Thread principal
        {
            while (execGammeService.gammeEnCours()) ; // Attendre que l'exécution soit possible

            if (this.modeFonctionnement.estAutonome()) {
                executerGamme(this.gammeDefaut.getId());
            }
        }
    }

    /**
     * Initialise la position des moteurs
     *
     * @throws Exception
     */
    private void initRobot() throws Exception {
        initPositionMoteurs();
    }


    /**
     * Positionne tous les moteurs en butée.
     */
    private void initPositionMoteurs() {
        Moteur.getInstance('D').tourner(360);
        Moteur.getInstance('D').stop();
        System.out.println("Moteur D fini");

        Moteur.getInstance('B').tourner(360);
        Moteur.getInstance('B').stop();
        System.out.println("Moteur B fini");

        Moteur.getInstance('C').tourner(360);
        Moteur.getInstance('C').stop();
        System.out.println("Moteur C fini");
    }


    /**
     * Récupére la liste des gammes et la gamme par défaut
     *
     * @throws Exception
     */
    private void initGamme() throws Exception {
        this.listeGammes = this.persistance.recupererGammes();
        this.gammeDefaut = this.persistance.recupererGammeDefaut();
    }


    /**
     * Sauvegarde un log
     *
     * @param r
     */
    public void sauverRapport(String r) {
        this.persistance.sauverLog(r);
    }

    /**
     * Demadne l'exécution d'une gamme. S'il y a une gamme en cours, la gamme est refusée.
     *
     * @param id ID de la gamme à exécuter
     */
    public void executerGamme(String id) {
        if (this.modeFonctionnement.peutExecuter()) {
            if (!gammeEnCours()) {
                Gamme gamme = this.listeGammes.get(id);
                if (gamme != null) {
                    this.execGammeService.executer(gamme);
                } else {
                    pilotage.envoyerMessage("La gamme demandee n'existe pas.");
                }
            } else {
                pilotage.envoyerMessage("Gamme deja en cours.");
                return;
            }
        }
    }

    /**
     * Implémentation de la classe IExecuteur, notifie le contréleur de la fin d'une gamme
     */
    public void notifierFinGamme() {
        pilotage.envoyerMessage("Gamme terminee !");

        this.initPositionMoteurs();
    }

    /**
     * Filtre les logs par date de sauvegarde
     *
     * @param date Date de filtrage
     * @return Liste des logs d'une date donnée
     */
    public ArrayList<String> filtrerLog(Date date) {
        ArrayList<String> rapports = this.persistance.recupererLogs();
        return rapports;
    }

    /**
     * Appelée lorsque le pilotage reéoit un message de l'opérateur. Détermine le type
     * du message et l'action à mener en conséquence.
     *
     * @param msg Message reéu
     */
    public void notifierMessage(String msg) {
        JsonObject root = new JsonObject();

        try {
            root = JsonParser.parseString(msg).getAsJsonObject();
            if (root == null)
                return;
        } catch (Exception e) {
            System.out.println("Message recu invalide ! Le format JSON n'est pas respecte");
            e.printStackTrace();
            return;
        }

        String action;
        JsonElement actionJson = root.get("action");
        if (actionJson == null) return;
        action = actionJson.getAsString();

        String login, pwd;
        if (utilisateurConnecte == null) {
            if (action.equals("co")) {
                login = root.get("login").getAsString();
                pwd = root.get("pwd").getAsString();
                if (login != null && pwd != null) {
                    utilisateurConnecte = persistance.trouverCompte(login, pwd);
                    if (utilisateurConnecte == null)
                        pilotage.envoyerMessage("Cet utilisateur n'existe pas !");
                    else
                        pilotage.envoyerMessage(String.format("Vous etes connecte sous %s.", utilisateurConnecte.getLogin()));
                }
                return;
            } else {
                pilotage.envoyerMessage("Vous n'étes pas connecté !");
                System.out.println("non connecté");
                return;
            }
        }

        // On fait un Switch-Case sur la valeur du champ "Action" du message reéu.
        // Les messages et leurs arguments sont décrits dans la documentation du logiciel.
        switch (action) {
            case "co":
                pilotage.envoyerMessage(String.format("Vous étes déjé connecté sous %s", utilisateurConnecte.getLogin()));
                break;
            case "deco":
                utilisateurConnecte = null;
                pilotage.envoyerMessage("Vous etes bien deconnecte.");
                break;
            case "newG":
                try {
                    Gamme g = interpreterGamme(root);
                    if (g != null) {
                        System.out.println(String.format("Nouvelle gamme créée : %s", g.getId()));
                        listeGammes.put(g.getId(), g);
                    } else throw new Exception();
                } catch (Exception e) {

                    this.pilotage.envoyerMessage("La gamme n'a pas pu etre creee.");
                    sauverRapport(e.getMessage());
                }
                break;
            case "modG":
                try {
                    Gamme g = (Gamme) gson.fromJson((JsonElement) root.get("gamme").getAsJsonObject(), Gamme.class);
                    if (g != null) {

                        this.persistance.modifierGamme(g);
                    }
                    throw new Exception("Informations invalides");
                } catch (Exception e) {
                    this.pilotage.envoyerMessage("La gamme n'a pas pu etre modifiee.");
                    sauverRapport(e.getMessage());
                }
                break;
            case "delG":
                try {
                    String g = root.get("idGamme").getAsString();
                    if (g != null) {

                        this.persistance.supprimerGamme(g);
                        this.listeGammes.remove(g);
                    }
                    throw new Exception("Informations invalides");
                } catch (Exception e) {

                    this.pilotage.envoyerMessage("La gamme n'a pas pu etre supprimee.");
                    sauverRapport(e.getMessage());
                }
                break;
            case "newU":
                try {
                    login = root.get("login").getAsString();
                    pwd = root.get("pwd").getAsString();
                    if (login != null && pwd != null) {

                        this.persistance.creerCompte(login, pwd);
                    }
                    throw new Exception("Informations invalides");
                } catch (Exception e) {

                    this.pilotage.envoyerMessage("Le compte n'a pas pu etre cree.");
                    sauverRapport(e.getMessage());
                }
                break;
            case "delU":
                this.persistance.supprimerCompte(root.get("login").getAsString());
                break;
            case "auto":
                if (this.modeFonctionnement.getClass().equals(ModePanne.class))
                    this.execGammeService = new ThreadGamme(this);
                this.modeFonctionnement = new ModeAutonome();
                break;
            case "manu":
                if (this.modeFonctionnement.getClass().equals(ModePanne.class))
                    this.execGammeService = new ThreadGamme(this);
                this.modeFonctionnement = new ModeManuel();
                break;
            case "panne":
                declencherPanne();
                break;
            case "execG":
                JsonElement idGamme = root.get("idGamme");
                if (idGamme == null)
                    throw new NullPointerException("L'id de la gamme à exécuter est nul");
                executerGamme(idGamme.getAsString());
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
                System.out.println("Ping reéu");
                this.pilotage.envoyerMessage("Pong !");
                break;
            case "stop":
                stopRobot();
                break;
            default:
                System.out.println(String.format("Commande inconnue : %s", action));
                break;
        }
        System.out.println("Message traite");
    }


    public void declencherPanne() {
        this.modeFonctionnement = new ModePanne();
        this.execGammeService.panne();
        System.out.println("Mode panne actif.");
    }


    public boolean gammeEnCours() {
        return this.execGammeService.gammeEnCours();
    }

    /**
     * Transforme un String JSON en objet Gamme
     * La désérialisation en objet est un peu spéciale pour la classe Téche, et nécessite
     * de le faire manuellement.
     *
     * @param json Objet JSON à convertir
     * @return Gamme convertie
     */
    private Gamme interpreterGamme(JsonObject json) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Gamme g = gson.fromJson(json.get("gamme"), Gamme.class);
            return g;
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Stoppe les services d'exécution et de pilotage, puis le contréleur,
     * en vue d'arréter le robot.
     */
    private void stopRobot() {
        this.pilotage.stop();
        this.execGammeService.stop();
        this.stop = true;
    }
}
