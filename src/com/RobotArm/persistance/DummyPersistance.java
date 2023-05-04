 package com.RobotArm.persistance;

 import com.RobotArm.business.Gamme;
 import com.RobotArm.business.Operation;
 import com.RobotArm.business.Tache;
 import com.RobotArm.business.Utilisateur;
 import com.RobotArm.interfaces.IPersistance;

 import java.util.ArrayList;
 import java.util.HashMap;


/**
 * Classe de persistance factice, qui permet de simuler une persistance.
 * @author Alvin
 *
 */
//TODO : implémenter une persistance, sous forme de fichier XML, par exemple
public class DummyPersistance implements IPersistance
{
	public void creerGamme(Gamme g) {}
	
	public void modifierGamme(Gamme g) {}
	
	public void supprimerGamme(String id) {}
	
	public HashMap<String, Gamme> recupererGammes()
	{
		HashMap<String, Gamme> liste = new HashMap<String, Gamme>();
		Gamme g;
		try {
			liste = new HashMap<String, Gamme>();
			g = recupererGammeDefaut();
			liste.put(g.getId(), g);
		} catch (Exception e)
		{
			
		}
		return liste;
	}

	
	public Gamme recupererGammeDefaut() {
		Gamme g = new Gamme("1", "Gamme defaut");
		
		Operation ope = new Operation("1", "Gamme défaut");		
		
		Tache t1 = new Tache("1", "Tourner é gauche", -135, 'C');
		Tache t2 = new Tache("2", "Ouvre la pince", -45, 'D');
		Tache t3 = new Tache("3", "Baisse le bras", -120, 'B');
		Tache t4 = new Tache("4", "Ferme la pince", 45, 'D');		
		Tache t5 = new Tache("5", "Monter le bras", 120, 'B');
		Tache t6 = new Tache("6", "Tourner é droite", 135, 'C');
		Tache t7 = new Tache("7", "Attendre 2 secondes", 2000);
		
		// Saisir un objet
		ope.AjouterTache(t1);
		ope.AjouterTache(t2);
		ope.AjouterTache(t3);
		ope.AjouterTache(t4);
		ope.AjouterTache(t5);
		ope.AjouterTache(t6);
		
		// Déposer un objet
		ope.AjouterTache(t3);
		ope.AjouterTache(t2);
		ope.AjouterTache(t5);
		ope.AjouterTache(t4);		
		
		// Attendre
		ope.AjouterTache(t7);
		
		g.AjouterOperation(ope);		
		return g;
	}

	
	public void sauverLog(String log) {}

	
	public ArrayList<String> recupererLogs() {
		return null;
	}

	
	public String getConfig(String nomConfig) {
		switch (nomConfig) {
		
		case "vitesseMoteur":
			return String.valueOf(120);
		case "accelerationMoteur":
			return String.valueOf(360);
		case "rendementMotA":
			return String.valueOf(2);
		case "rendementMotB":
			return String.valueOf(5);
		case "rendementMotC":
			return String.valueOf(3);
		}
		return "";
	}
	
	public void creerCompte(String login, String pwd) {}
	
	
	public Utilisateur trouverCompte(String l, String p)
	{
		Utilisateur[] users = new Utilisateur[]
		{
			new Utilisateur("admin", "admin", true),
			new Utilisateur("robert", "passe", false)
		};
		
		for(Utilisateur user : users)
		{
			if(user.getLogin().equals(l) && user.getPwd().equals(p))
				return user;
		}
		return null;
	}
	
	
	public void supprimerCompte(String login) {}
}
