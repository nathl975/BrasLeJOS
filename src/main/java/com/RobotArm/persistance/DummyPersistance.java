 package com.RobotArm.persistance;

import com.RobotArm.business.*;
import com.RobotArm.interfaces.IPersistance;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DummyPersistance implements IPersistance
{
	public void creerGamme(Gamme g) throws SQLException {}
	
	public void modifierGamme(Gamme g) throws SQLException {}
	
	public void supprimerGamme(String id) throws SQLException {}
	
	public HashMap<String, Gamme> recupererGammes() throws SQLException
	{
		return null;
	}


	
	public Gamme recupererGammeDefaut() throws SQLException {
		Gamme g = new Gamme();
		
		Operation ope = new Operation();
		Tache t1 = new Tache(Tache.TypeAction.Tourner, 30);
		
		return g;
	}



	
	public void sauverLog(String log) {}



	
	public ArrayList<String> recupererLogs() throws SQLException {
		return null;
	}

	
	public String getConfig(String nomConfig) throws SQLException {
		switch (nomConfig) {
		 
		 case "vitesseMoteur":
			return String.valueOf(100);
		 case "accelerationMoteur":
			return String.valueOf(100);
		 case "rendementMotA":
			return String.valueOf(2);
		 case "rendementMotB":
			return String.valueOf(5);
		 case "rendementMotC":
			return String.valueOf(3);
		} 
		return "";
	}
	
	public void creerCompte(String login, String pwd) throws SQLException {}
	
	public void supprimerCompte(String login) throws SQLException {}
}
