 package com.RobotArm.business;

 import com.RobotArm.interfaces.IExecuteur;

 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.Future;

 /**
  * Service d'exécution des gammes
  * @author Alvin
  *
  */
 public class ThreadGamme
 {
	public IExecuteur executeur;
	private final ExecutorService execService;
	private Future execThread;
	private boolean stopper;
	private Gamme gammeEnExec;
	
	/**
	 * Constructeur de la classe
	 * @param executeur exécuteur à utiliser pout lancer la game
	 */
	public ThreadGamme(IExecuteur executeur) {
		this.executeur = executeur;
		this.execService = Executors.newSingleThreadExecutor();
		stopper = false;
	}

	/**
	 * Exécute une gamme en parallèle du reste du programme
	 * @param g la gamme à exécuter
	 */
	public void executer(Gamme g) {
		gammeEnExec = g;
		System.out.printf("Démarrage de la gamme %s%n", gammeEnExec.id);
		stopper = false;
		this.execThread = this.execService.submit(() -> {
			// Pour chaque tâche de la gamme, on lui demande d'exécuter son programme.
			try {
				for(Operation o : g.getListeOperations())
				{
					for(Tache t:o.getListeTaches())
					{
						// La variable booléenne stopper permet d'interrompre le thread
						// proprement et sans perte de mémoire ou de contrôleur du thread
						if(stopper)
							throw new InterruptedException();
						t.executer();
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			stopper = true;
			ThreadGamme.this.notifierObservateur();
		});
	}
	
	/**
	 * Appelée lors de la fin d'une gamme
	 */
	private void notifierObservateur() {
		System.out.println("Gamme terminee");
		this.executeur.notifierFinGamme();
	}

	/**
	 * @return True si une gamme est en cours, False sinon
	 */
	public Boolean gammeEnCours() {
		return execThread != null && !this.execThread.isDone();
	}
	
	/**
	 * @return la gamme actuellement exécutée
	 */
	public Gamme getGammeEnExecution()
	{
		return gammeEnExec;
	}
	
	/**
	 * Déclenche le mode panne, ce qui arrête le thread et l'exécution de la gamme
	 */
	public void panne()
	{
		stopper = true;
		Moteur.stopAll();		
	}
	
	/**
	 * Arrête le thread complet en vue d'arrêter le robot
	 */
	public void stop()
	{
		panne();
		if(this.execThread != null)
			this.execThread.cancel(true);
		this.execService.shutdownNow();
	}
}
