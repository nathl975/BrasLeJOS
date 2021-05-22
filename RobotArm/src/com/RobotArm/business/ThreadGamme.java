package com.RobotArm.business;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.RobotArm.interfaces.IExecuteur;

public class ThreadGamme {
	public IExecuteur executeur;
	private ExecutorService execService;
	private Future execThread; // Ajouté

	public ThreadGamme(IExecuteur executeur) {
		this.executeur = executeur;
		execService = ExecutorService.newSingleThreadExecutor();
	}

	public void executer(Gamme g)
	{
	// Utilisation de l'ExecutorService pour déléguer l'exécution à un thread géré par Java.
		Future execThread = execService.submit(new Runnable()
			{
				try
				{
					g.executer();
					notifierObservateur();
				}
				catch(Exception e)
				{
					notifierObservateur();
				}
			});
	}

	private void notifierObservateur() {
		executeur.notifierFinGamme();
	}

	public Boolean gammeEnCours() // Ajouté
	{
		return execThread.isDone();
	}
}