import lejos.hardware.motor.Motor;
import lejos.hardware.sensor;

class Controleur implements IExecuteur, IPilote
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
		int vitesseMoteur = persistance.getConfig("vitesseMoteur");
		int accelerationMoteur = persistance.getConfig("accelerationMoteur");
		int rendementMotA = persistance.getConfig("rendementMotA");
		int rendementMotB = persistance.getConfig("rendementMotB");
		int rendementMotC = persistance.getConfig("rendementMotC");
		int val;

		lejos.Motor.A.setSpeed(vitesseMoteur);
		lejos.Motor.B.setSpeed(vitesseMoteur);
		lejos.Motor.C.setSpeed(vitesseMoteur);
		
		lejos.Motor.A.setAcceleration(rendementMotA);
		lejos.Motor.B.setAcceleration(rendementMotB);
		lejos.Motor.C.setAcceleration(rendementMotC);
		
		initPositionMoteurs(); // Initialise les moteurs pour les tourner en butée.
	}
	
	
	private void initGamme()
	{
		this.listeGammes = persistance.recupererGammes();
		this.gammeDefaut = persistance.recupererGammeDefaut();
	}
	
	
	public void sauverRapport(String r)
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


	public ArrayList<String> filtrerLog(Date date)
	{
		ArrayList<String> rapports = persistance.recupererLogs();
		//Filtrage du tableau et expulsion des éléments dont la date est antérieure au paramètre transmis.
		return rapports;
	}


	public notifierMessage(String msg)
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
					sauverRapport(e.message);
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
					sauverRapport(e.message);
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
					sauverRapport(e.message);
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
					sauverRapport(e.message);
				}			
				break;
			case "supprimerCompte":
				persistance.supprimerCompte(json.getString("login"));
				break;
			case "modeAutonome":
				modeFonctionnement = new modeAutonome();
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
					rapports = filtrerLog(new Date(json.getString("date"));
				else
					rapports = persistance.recupererLogs();		
				pilotage.afficherHistorique(rapports);
				break;
		}
	}


	public déclencherPanne()
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