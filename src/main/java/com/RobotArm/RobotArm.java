 package com.RobotArm;
 
 import com.RobotArm.business.*;
import com.RobotArm.business.Moteur;
import com.RobotArm.controller.Controleur;
 import com.RobotArm.interfaces.IPersistance;
 import com.RobotArm.interfaces.IPilotage;
 import com.RobotArm.interfaces.IPilote;
 import com.RobotArm.persistance.DummyPersistance;
 import com.RobotArm.pilotage.WifiListener;
 import java.io.IOException;
 import java.sql.SQLException;

import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

 
 public class RobotArm
{
	private static IPersistance persistance;
	private static IPilotage pilotage;
	private static Controleur controleur;
	
	public static void main(String[] args){
		System.out.println("Debut du programme");
 		
		try
		{
			InitHardware();
			persistance = (IPersistance)new DummyPersistance();
			pilotage = (IPilotage)new WifiListener();
			controleur = new Controleur(persistance, pilotage);
			pilotage.ajoutListener((IPilote)controleur);
			
			controleur.demarrer();
		}
		catch (IOException e){
			System.out.println("Une erreur bloquane est survenue, impossible de demarrer !");
			e.printStackTrace();
		} catch (SQLException e){
			System.out.println("Une erreur SQL bloquante est survenue, impossible de demarrer !");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Une erreur inconnue est survenue, impossible de demarrer !");
			e.printStackTrace();
		} 
		pilotage.fermerConnexion();
		
		pilotage = null;
		persistance = null;
		controleur = null;
		System.gc();
		System.out.println("Fin du programme");
	}
	
	
	private static void InitHardware()
	{
		float vitesse = 120;
		int acceleration = 360;
		CapteurContact.initCapteur('1');
		CapteurCouleur.initCapteur('2');
		CapteurPince.initCapteur();
		
		Moteur.initMoteur('B', 3, CapteurCouleur.getInstance(), vitesse, acceleration, Moteur.SENS_NEGATIF); // Levée
		Moteur.initMoteur('C', 5, CapteurContact.getInstance(), vitesse, acceleration, Moteur.SENS_POSITIF); // Rotation
		Moteur.initMoteur('D', 2, CapteurPince.getInstance(), vitesse, acceleration, Moteur.SENS_POSITIF); // Pince
	}
}
