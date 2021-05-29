 package com.RobotArm.pilotage;
 
 import com.RobotArm.interfaces.IPilotage;
 import com.RobotArm.interfaces.IPilote;
 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.PrintWriter;
 import java.net.ServerSocket;
 import java.net.Socket;
 import java.util.ArrayList;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.Future;
 
 
 public class WifiListener
	implements IPilotage
 {
	private ServerSocket socket;
	private Socket client;
	private boolean wifiOn = true;
	private IPilote pilote;
	private Future execThread;
	private ExecutorService execService = Executors.newSingleThreadExecutor();

	
	public void ecouter() {
		Future<?> execThread = this.execService.submit(new Runnable()
				{
					
					public void run()
					{
						try {
							int port = 80;
							socket = new ServerSocket(port);
							System.out.println(String.format("Ecoute sur le port : %d", new Object[] { Integer.valueOf(port) }));							
							while(true)
							{	
								System.out.println("Attente d'un message...");								
								client = socket.accept();
								
								PrintWriter out = new PrintWriter(client.getOutputStream(), true);
								BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
								
								String s ="";
								String message = "";
						        while ((s = in.readLine()) != null)
						        {
									System.out.println(String.format("Message reçu : %s", s));
									if(s != null)
									{
										message = message.concat(s);
									}
								}
						        notifierPilote(message);
								System.out.println("Fin de la transmission.");
								if(!client.isClosed())
									client.close();
							}
						}
						catch (Exception e) {							
							e.printStackTrace();
						} 
					}
				});
	}
	
	private void notifierPilote(String message) {
		this.pilote.notifierMessage(message);
	}
 
 
 
 
	
	public void afficherEtatSysteme() {}
 
 
 
 
	
	public void afficherHistorique(ArrayList<String> rapports) {}
 
 
 
 
	
	public void envoyerMessage(String message) {}
 
 
 
 
	
	public void stop() {
		this.wifiOn = false;
	}
 
	
	public void fermerConnexion() {
		try {
			this.client.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
 
 
	
	public void ajoutListener(IPilote listener) {
		this.pilote = listener;
	}
 }
