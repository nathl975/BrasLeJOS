 package com.RobotArm.controller;

 import com.RobotArm.business.Gamme;
import com.RobotArm.business.Moteur;
import com.RobotArm.business.ThreadGamme;
import com.RobotArm.business.Utilisateur;
import com.RobotArm.interfaces.IEtatMode;
 import com.RobotArm.interfaces.IExecuteur;
 import com.RobotArm.interfaces.IPersistance;
 import com.RobotArm.interfaces.IPilotage;
 import com.RobotArm.interfaces.IPilote;
 import com.google.gson.Gson;
 import com.google.gson.GsonBuilder;
 import com.google.gson.JsonElement;
 import com.google.gson.JsonObject;
 import com.google.gson.JsonParser;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.HashMap;

 public class Controleur implements IExecuteur, IPilote
 {
	HashMap<String, Gamme> listeGammes;
	Gamme gammeDefaut;
	Utilisateur utilisateurConnecte;
	IPersistance persistance;
	IEtatMode modeFonctionnement;
	IPilotage pilotage;
	ThreadGamme execGammeService;
	Gson gson;
	
	public Controleur(IPersistance pe, IPilotage pi) throws Exception {
		this.persistance = pe;
		this.pilotage = pi;
		this.listeGammes = new HashMap<>();
		this.gammeDefaut = new Gamme();
		this.modeFonctionnement = new ModeManuel();
		this.execGammeService = new ThreadGamme(this);
		this.gson = (new GsonBuilder()).create();
		
		initRobot();
		initGamme();
	}

	
	public void demarrer() {
		System.out.println("Demarrage du controleur");
		this.pilotage.ecouter();
		while (true) // Thread principal
		{
			while(execGammeService.gammeEnCours()); // Attendre que l'exécution soit possible
			
			if (this.modeFonctionnement.estAutonome())
			{					
				executerGamme(this.gammeDefaut.getId());
			}
		}
	}


	private void initRobot() throws Exception {
		initPositionMoteurs();
	}


	/**
	 * Positionne tous les moteurs en butée. 
	 */
	private void initPositionMoteurs()
	{
		Moteur.getInstance('A').tourner(360);
		Moteur.getInstance('A').stop();
		System.out.println("Moteur A fini");

		Moteur.getInstance('B').tourner(360);
		Moteur.getInstance('B').stop();		
		System.out.println("Moteur B fini");		

		Moteur.getInstance('C').tourner(360);
		Moteur.getInstance('C').stop();
		System.out.println("Moteur C fini");
	}


	
	private void initGamme() throws Exception {
		this.listeGammes = this.persistance.recupererGammes();
		this.gammeDefaut = this.persistance.recupererGammeDefaut();
	}


	
	public void sauverRapport(String r) {
		try {
			this.persistance.sauverLog(r);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	
	public void executerGamme(String id) {
		if (this.modeFonctionnement.peutExecuter())
		{
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

	
	public void notifierFinGamme() {
		pilotage.envoyerMessage("Gamme terminee !");
		
		this.initPositionMoteurs();
	}


	public ArrayList<String> filtrerLog(Date date) {
		try {
			ArrayList<String> rapports = this.persistance.recupererLogs();
			return rapports;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
	}

	
	public void notifierMessage(String msg) {
		JsonObject root = new JsonObject();

		try {
			root = JsonParser.parseString(msg).getAsJsonObject();
			if(root == null)
				return;
		}
		catch (Exception e)
		{		
			System.out.println("Message recu invalide ! Le format JSON n'est pas respecte");
			e.printStackTrace();
			return;
		}		
		
		String action;
		JsonElement actionJson = root.get("action");
		if(actionJson == null) return;
		action = actionJson.getAsString();
		
		String login, pwd;
		if(utilisateurConnecte == null)
		{
			if(action.equals("co"))
			{				
				login = root.get("login").getAsString();
				pwd = root.get("pwd").getAsString();
				if (login != null && pwd != null)
				{
					utilisateurConnecte = persistance.trouverCompte(login, pwd);
					if(utilisateurConnecte == null)
						pilotage.envoyerMessage("Cet utilisateur n'existe pas !");
					else
						pilotage.envoyerMessage(String.format("Vous etes connecte sous %s.", utilisateurConnecte.getLogin()));
				}
				return;
			}
			else
			{
				pilotage.envoyerMessage("Vous n'êtes pas connecté !");
				return;
			}
		}
		
		switch (action) {
			case "co":
				pilotage.envoyerMessage(String.format("Vous êtes déjà connecté sous %s", utilisateurConnecte.getLogin()));
				break;
			case "deco":
				utilisateurConnecte = null;
				pilotage.envoyerMessage("Vous etes bien deconnecte.");
			case "newG":				
				try {
					Gamme g = (Gamme)gson.fromJson((JsonElement)root.get("gamme").getAsJsonObject(), Gamme.class);
					
					if (g != null) {
						
						this.persistance.creerGamme(g);
						break;
					}
					throw new Exception("Informations invalides");
				}
				catch (Exception e) {
					
					this.pilotage.envoyerMessage("La gamme n'a pas pu etre creee.");
					sauverRapport(e.getMessage());
					break;
				}
			
			case "modG":
				try {
					Gamme g = (Gamme)gson.fromJson((JsonElement)root.get("gamme").getAsJsonObject(), Gamme.class);
					if (g != null) {
						
						this.persistance.modifierGamme(g);
						break;
					}
					throw new Exception("Informations invalides");
				}
				catch (Exception e) {
					
					this.pilotage.envoyerMessage("La gamme n'a pas pu etre modifiee.");
					sauverRapport(e.getMessage());
					break;
				}
			
			case "delG":
				try {
					String g = root.get("idGamme").getAsString();
					if (g != null) {
						
						this.persistance.supprimerGamme(g);
						this.listeGammes.remove(g);
						break;
					}
					throw new Exception("Informations invalides");
				}
				catch (Exception e) {
					
					this.pilotage.envoyerMessage("La gamme n'a pas pu etre supprimee.");
					sauverRapport(e.getMessage());
					break;
				}
			
			case "newU":
				try {
					login = root.get("login").getAsString();
					pwd = root.get("pwd").getAsString();
					if (login != null && pwd != null) {
						
						this.persistance.creerCompte(login, pwd);
						break;
					}
					throw new Exception("Informations invalides");
				}
				catch (Exception e) {
					
					this.pilotage.envoyerMessage("Le compte n'a pas pu etre cree.");
					sauverRapport(e.getMessage());
					break;
				}
			case "delU":
				try {
					this.persistance.supprimerCompte(root.get("login").getAsString());
				} catch (SQLException e) {
					System.out.println("Erreur lors de la suppression du compte");
				}
				break;
			case "auto":
				if(this.modeFonctionnement.getClass().equals(ModePanne.class))
					this.execGammeService = new ThreadGamme(this);
				this.modeFonctionnement = new ModeAutonome();
				break;
			case "manu":
				if(this.modeFonctionnement.getClass().equals(ModePanne.class))
					this.execGammeService = new ThreadGamme(this);				
				this.modeFonctionnement = new ModeManuel();
				break;
			case "panne":
				declencherPanne();
				break;
			case "execG":
				JsonElement idGamme = root.get("idGamme");
				if(idGamme == null)
					throw new NullPointerException("L'id de la gamme à exécuter est nul");
				executerGamme(idGamme.getAsString());
				break;
			
			case "logs":
				if (root.get("date").getAsString() != null) {
					ArrayList<String> rapports = filtrerLog(new Date(root.get("d").getAsString())); break;
				}
				try {
					ArrayList<String> rapports = this.persistance.recupererLogs();
					this.pilotage.afficherHistorique(rapports);
				} catch (SQLException e) {
					System.out.println("Erreur lors de la recuperation des logs");
				}
				break;
			case "ping":
				System.out.println("Ping reçu");
				this.pilotage.envoyerMessage("Pong !");
				break;
			default:
				System.out.println(String.format("Commande inconnue : %s", action));
				break;
		}
		System.out.println("Message traite");
	}

	
	public void declencherPanne() {
		this.modeFonctionnement = new ModePanne();
		this.execGammeService.stop();
		System.out.println("Mode panne actif.");
	}
	
	
	public void reprisePanne()
	{
		
	}


	
	public boolean gammeEnCours() {
		return this.execGammeService.gammeEnCours();
	}
 }
