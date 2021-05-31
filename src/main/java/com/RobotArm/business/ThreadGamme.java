 package com.RobotArm.business;

 import com.RobotArm.interfaces.IExecuteur;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.Future;

 public class ThreadGamme
 {
	public IExecuteur executeur;
	private ExecutorService execService;
	private Future execThread;
	
	public ThreadGamme(IExecuteur executeur) {
		this.executeur = executeur;
		this.execService = Executors.newSingleThreadExecutor();
	}

	
	public void executer(final Gamme g) {
		System.out.println(String.format("Démarrage de la gamme %s", g.id));
		this.execThread = this.execService.submit(new Runnable()
		{
			public void run()
			{
				try {
					g.executer();
					ThreadGamme.this.notifierObservateur();
				}
				catch (Exception e) {
					
					ThreadGamme.this.notifierObservateur();
				}
			}
		});
	}
	
	private void notifierObservateur() {
		this.executeur.notifierFinGamme();
	}

	
	public Boolean gammeEnCours() {
		return execThread == null ? true : this.execThread.isDone();
	}
	
	public void stop() {}
 }
