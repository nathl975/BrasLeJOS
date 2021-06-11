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
	private boolean stopper;
	private Gamme gammeEnExec;
	
	public ThreadGamme(IExecuteur executeur) {
		this.executeur = executeur;
		this.execService = Executors.newSingleThreadExecutor();
		stopper = false;
	}

	
	public void executer(final Gamme g) {
		gammeEnExec = g;
		System.out.println(String.format("Démarrage de la gamme %s", gammeEnExec.id));
		stopper = false;
		this.execThread = this.execService.submit(new Runnable()
		{
			public void run()
			{
				try {
					for(Operation o:g.getListeOperations())
					{
						for(Tache t:o.getListeTaches())
						{
							if(stopper == true)
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
			}
		});
	}
	
	private void notifierObservateur() {
		System.out.println("Gamme terminee");
		this.executeur.notifierFinGamme();
	}

	
	public Boolean gammeEnCours() {
		return execThread == null ? false : ! this.execThread.isDone();
	}
	
	
	public Gamme getGammeEnExecution()
	{
		return gammeEnExec;
	}
	
	public void panne()
	{
		stopper = true;
		Moteur.stopAll();		
	}
	
	
	public void stop()
	{
		panne();
		if(this.execThread != null)
			this.execThread.cancel(true);
		this.execService.shutdownNow();
	}
}
