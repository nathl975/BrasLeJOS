 package com.RobotArm.pilotage;
 
 import com.RobotArm.interfaces.IPilotage;
 import com.RobotArm.interfaces.IPilote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lejos.utility.Delay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
 import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
				try
				{
					BufferedReader in;
					
					socket = new ServerSocket(port);
					System.out.println(String.format("Ecoute sur le port : %d", port));							
					
					while(wifiOn && !socket.isClosed())
					{
						client = socket.accept();
						in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						while(!client.isClosed())
						{
							String s ="";
							System.out.println("Attente d'un message...");								
				        	if((s = in.readLine()) != null)
							{
				        		System.out.println(String.format("Message reçu : %s", s));
								//message = message.concat(s);
								notifierPilote(s);
							}
				        	else
				        	{
				        		client.close();
				        	}
				        	Delay.msDelay(500); // Ajout d'un petit délai pour ne pas surcharger le thread
						}
						notifierDeconnexion();						
					}		
					System.out.println("Wifi terminée");
				} catch (Exception e)
				{							
					e.printStackTrace();
					return;
				}
			}
		});		
	}
		
	
	private void notifierPilote(String message) {
		this.pilote.notifierMessage(message);
	}
	
	
	private void notifierDeconnexion()
	{
		JsonObject root = new JsonObject();
		root.addProperty("action", "deco");
		notifierPilote(new Gson().toJson(root));
	}
 
 
 
 
	
	public void afficherEtatSysteme() {}
 
 
 
 
	
	public void afficherHistorique(ArrayList<String> rapports) {}
 
 
 
 
	
	public void envoyerMessage(String message)
	{
		try
		{			
			if(client.isConnected())
			{
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				out.write(String.format("%s\n", message));
				out.flush();				
			}
		} catch (Exception e) {
			System.out.println(String.format("Impossible d'envoyer un message : %s", e.getMessage()));
		}
	}
 
 
 
 
	
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
