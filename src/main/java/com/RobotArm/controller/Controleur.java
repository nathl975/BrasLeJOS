package com.RobotArm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.ThreadGamme;
import com.RobotArm.interfaces.IEtatMode;
import com.RobotArm.interfaces.IExecuteur;
import com.RobotArm.interfaces.IPersistance;
import com.RobotArm.interfaces.IPilotage;
import com.RobotArm.interfaces.IPilote;
import com.google.gson.*;

import lejos.hardware.motor.Motor;

public class Controleur implements IExecuteur, IPilote
{
	HashMap<String, Gamme> listeGammes;
	Gamme gammeDefaut;
	IPersistance persistance;
	IEtatMode modeFonctionnement;
	IPilotage pilotage;
	ThreadGamme execGammeService;
	
	
	public Controleur(IPersistance pe, IPilotage pi) throws SQLException
	{
		this.persistance = pe;
		this.pilotage = pi;
		this.listeGammes = new HashMap<String, Gamme>();
		this.gammeDefaut = new Gamme();
		this.modeFonctionnement = new ModeManuel();
		
		initRobot();
		initGamme();
	}
	
	
	private void initRobot() throws NumberFormatException, SQLException
	{
		int vitesseMoteur = Integer.parseInt(persistance.getConfig("vitesseMoteur").get("vitMot"));
		int accelerationMoteur = Integer.parseInt(persistance.getConfig("accelerationMoteur").get("accMot"));
		int rendementMotA = Integer.parseInt(persistance.getConfig("rendementMotA").get("motA"));
		int rendementMotB = Integer.parseInt(persistance.getConfig("rendementMotB").get("motB"));
		int rendementMotC = Integer.parseInt(persistance.getConfig("rendementMotC").get("motC"));
		int val;

		Motor.A.setSpeed(vitesseMoteur);
		Motor.B.setSpeed(vitesseMoteur);
		Motor.C.setSpeed(vitesseMoteur);
		
		Motor.A.setAcceleration(rendementMotA);
		Motor.B.setAcceleration(rendementMotB);
		Motor.C.setAcceleration(rendementMotC);
		
		initPositionMoteurs(); // Initialise les moteurs pour les tourner en butÃ©e.
	}
	
	private void initPositionMoteurs()
	{
		//TODO : Ajouter l'initialisation des moteurs pour les mettre en butée
	}
	
	private void initGamme() throws SQLException
	{
		this.listeGammes = persistance.recupererGammes();
		this.gammeDefaut = persistance.recupererGammeDefaut();
	}
	
	
	public void sauverRapport(String r) throws SQLException
	{
		persistance.sauverLog(r);
	}
	

	public void executerGamme(String id)
	{
		if(modeFonctionnement.peutExecuter())
		{
			if(!gammeEnCours())
			{
				Gamme gamme = listeGammes.get(id);
				if(gamme != null)
					execGammeService.executer(gamme); // ExÃ©cute une gamme dans un thread
				else
					pilotage.envoyerMessage("La gamme demandée n'existe pas.");
			}
			else
			{
				pilotage.envoyerMessage("Gamme déjà  en cours.");
				return;
			}
		}
		
	}
	

	public void notifierFinGamme()
	{
		if(modeFonctionnement.estAutonome())
		{
			executerGamme(gammeDefaut.getId());
		}
	}


	public ArrayList<String> filtrerLog(Date date) throws SQLException
	{
		ArrayList<String> rapports = persistance.recupererLogs();
		//Filtrage du tableau et expulsion des Ã©lÃ©ments dont la date est antÃ©rieure au paramÃ¨tre transmis.
		return rapports;
	}


	public void notifierMessage(String msg) throws SQLException
	{		
		Gson gson = new GsonBuilder().create();

		//TODO : conversion string to json
		
		JsonObject root = JsonParser.parseString(msg).getAsJsonObject();
		
		switch(root.get("action").getAsString())
		{
			case "creerGamme":
				try
				{
					Gamme g = gson.fromJson(root.get("gamme").getAsJsonObject(), Gamme.class);
					
					if(g != null)
					{
						persistance.creerGamme(g);
					}
					else
						throw new Exception("Informations invalides");
				}
				catch(Exception e)
				{
					pilotage.envoyerMessage("La gamme n'a pas pu Ãªtre crÃ©Ã©e.");
					sauverRapport(e.getMessage());
				}
				break;
			case "modifierGamme":
				try
				{
					Gamme g = gson.fromJson(root.get("gamme").getAsJsonObject(), Gamme.class);
					if(g != null)
					{
						persistance.modifierGamme(g);
					}
					else
						throw new Exception("Informations invalides");
				}
				catch(Exception e)
				{
					pilotage.envoyerMessage("La gamme n'a pas pu Ãªtre modifiÃ©e.");
					sauverRapport(e.getMessage());
				}
				break;
			case "supprimerGamme":
				try
				{
					String g = root.get("idGamme").getAsString();
					if(g != null)
					{
						persistance.supprimerGamme(g);
					}
					else
						throw new Exception("Informations invalides");
				}
				catch(Exception e)
				{
					pilotage.envoyerMessage("La gamme n'a pas pu Ãªtre supprimÃ©e.");
					sauverRapport(e.getMessage());
				}			
				break;
			case "creerCompte":
				try
				{
					String login = root.get("login").getAsString();
					String pwd = root.get("pwd").getAsString();
					if(login != null && pwd != null)
					{
						persistance.creerCompte(login, pwd);
					}
					else
						throw new Exception("Informations invalides");
				}
				catch(Exception e)
				{
					pilotage.envoyerMessage("Le compte n'a pas pu être créé.");
					sauverRapport(e.getMessage());
				}			
				break;
			case "supprimerCompte":
				persistance.supprimerCompte(root.get("login").getAsString());
				break;
			case "modeAutonome":
				modeFonctionnement = new ModeAutonome();
				break;
			case "declencherPanne":
				declencherPanne();
				break;
			case "executerGamme":
				executerGamme(root.get("idGamme").getAsString());
				break;
			case "recupererLogs":
				ArrayList<String> rapports;
				if(root.get("date").getAsString() != null)
					rapports = filtrerLog(new Date(root.get("d").getAsString()));
				else
					rapports = persistance.recupererLogs();		
				pilotage.afficherHistorique(rapports);
				break;
		}
	}


	public void declencherPanne()
	{
		modeFonctionnement = new ModePanne();
		execGammeService.stop();
		pilotage.envoyerMessage("Mode panne activé.");
	}


	public boolean gammeEnCours()
	{
		return execGammeService.gammeEnCours();
	}
}