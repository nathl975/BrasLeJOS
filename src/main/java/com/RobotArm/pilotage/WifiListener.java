 package com.RobotArm.pilotage;
 
 import com.RobotArm.interfaces.IPilotage;
 import com.RobotArm.interfaces.IPilote;

import lejos.utility.Delay;

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
	private int port = 80;
	private IPilote pilote;
	private Future execThread;
	private ExecutorService execService = Executors.newSingleThreadExecutor();

	
	public void ecouter() {
		Future<?> execThread = this.execService.submit(new Runnable()
		{			
			public void run()
			{
				PrintWriter out;
				BufferedReader in;
				
				try
				{
					socket = new ServerSocket(port);
					System.out.println(String.format("Ecoute sur le port : %d", port));							
					client = socket.accept();
				} catch (IOException e)
				{
					e.printStackTrace();
					return;
				}			
				
				while(wifiOn)
				{	
					try {
						Delay.msDelay(500); // Ajout d'un petit délai pour ne pas surcharger le thread
						System.out.println("Attente d'un message...");								

						out = new PrintWriter(client.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						
						String s ="";
			        	if((s = in.readLine()) != null)
						{
			        		System.out.println(String.format("Message reçu : %s", s));
							//message = message.concat(s);
							notifierPilote(s);
						}
						/*if(!client.isClosed())
							client.close();
						System.out.println("Fin de la transmission.");*/
					} catch (Exception e)
					{							
						e.printStackTrace();
					}
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
