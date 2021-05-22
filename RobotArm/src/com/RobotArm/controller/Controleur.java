package com.RobotArm.controller;

import lejos.hardware.motor.Motor;
import lejos.hardware.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.RobotArm.persistance.*;
import com.RobotArm.business.*;
import com.RobotArm.interfaces.*;

public class Controleur implements IExecuteur, IPilote
{
	HashMap<String, Gamme> listeGammes;
	Gamme gammeDefaut;
	IPersistance persistance;
	IEtatMode modeFonctionnement;
	IPilotage pilotage;
	ThreadGamme execGammeService;
	
	
	public Controleur(IPersistance pe, IPilotage pi)
	{
		this.persistance = pe;
		this.pilotage = pi;
		this.listeGammes = new HashMap<String, Gamme>();
		this.gammeDefaut = new Gamme();
		this.modeFonctionnement = new ModeManuel();
		
		initRobot();
		initGamme();
	}
	
	
	private void initRobot()
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
		
		initPositionMoteurs(); // Initialise les moteurs pour les tourner en butée.
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
					execGammeService.executer(gamme); // Exécute une gamme dans un thread
				else
					pilotage.envoyerMessage("La gamme demandée n'existe pas.");
			}
			else
			{
				pilotage.envoyerMessage("Gamme déjà en cours.");
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
		//Filtrage du tableau et expulsion des éléments dont la date est antérieure au paramètre transmis.
		return rapports;
	}


	public void notifierMessage(String msg)
	{
		JSONObject json = new JSONObject(msg);
		switch(json.getString("cmd"))
		{
			case "creerGamme":
				try
				{
					Gamme g = Gamme.ConvertirJsonEnGamme(json.getString("gamme"));
					if(g != null)
					{
						persistance.creerGamme(g);
					}
					else
						throw new Exception("Informations invalides");
				}
				catch(Exception e)
				{
					pilotage.envoyerMessage("La gamme n'a pas pu être créée.");
					sauverRapport(e.getMessage());
				}
				break;
			case "modifierGamme":
				try
				{
					Gamme g = Gamme.ConvertirJsonEnGamme(json.getString("gamme"));
					if(g != null)
					{
						persistance.modifierGamme(g);
					}
					else
						throw new Exception("Informations invalides");
				}
				catch(Exception e)
				{
					pilotage.envoyerMessage("La gamme n'a pas pu être modifiée.");
					sauverRapport(e.getMessage());
				}
				break;
			case "supprimerGamme":
				try
				{
					Gamme g = Gamme.ConvertirJsonEnGamme(json.getString("gamme"));
					if(g != null)
					{
						persistance.creerGamme(g);
					}
					else
						throw new Exception("Informations invalides");
				}
				catch(Exception e)
				{
					pilotage.envoyerMessage("La gamme n'a pas pu être supprimée.");
					sauverRapport(e.getMessage());
				}			
				break;
			case "creerCompte":
				try
				{
					String login = json.getString("login");
					String pwd = json.getString("pwd");
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
				persistance.supprimerCompte(json.getString("login"));
				break;
			case "modeAutonome":
				modeFonctionnement = new ModeAutonome();
				break;
			case "declencherPanne":
				declencherPanne();
				break;
			case "executerGamme":
				executerGamme(json.getString("gamme"));
				break;
			case "recupererLogs":
				ArrayList<String> rapports;
				if(json.getString("date") != null)
					rapports = filtrerLog(new Date(json.getString("date")));
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