 package com.RobotArm.persistance;

 import com.RobotArm.business.Gamme;
 import com.RobotArm.business.Operation;
 import com.RobotArm.business.Tache;
 import com.RobotArm.business.Utilisateur;
 import com.RobotArm.exception.GammeNotFoundException;
 import com.RobotArm.exception.UnableToReadUsersException;
 import com.RobotArm.interfaces.IPersistance;

 import java.util.ArrayList;


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
	
	public ArrayList<Gamme> getGammes()
	{
		ArrayList<Gamme> liste = new ArrayList<>();
		Gamme g;
		try {
			liste = getGammes();
		} catch (Exception ignored)
		{
			
		}
		return liste;
	}

	@Override
	public Gamme findGamme(String id) throws GammeNotFoundException {
		return null;
	}

	public Gamme recupererGammeDefaut() {
		Gamme g = new Gamme("1", "Gamme defaut");
		
		Operation ope = new Operation("1", "Gamme défaut");		
		
		Tache t1 = new Tache("1", "Tourner à gauche", -135, 'C');
		Tache t2 = new Tache("2", "Ouvre la pince", -45, 'D');
		Tache t3 = new Tache("3", "Baisse le bras", -120, 'B');
		Tache t4 = new Tache("4", "Ferme la pince", 45, 'D');		
		Tache t5 = new Tache("5", "Monter le bras", 120, 'B');
		Tache t6 = new Tache("6", "Tourner à droite", 135, 'C');
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
	
	public void createUser(String login, String pwd) {}
	
	
	public Utilisateur findUser(String l, String p)
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

	@Override
	public ArrayList<Utilisateur> getUsers() throws UnableToReadUsersException {
		return null;
	}
}
