package com.RobotArm.pilotage;

import com.RobotArm.interfaces.IPilotage;
import com.RobotArm.interfaces.IPilote;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lejos.utility.Delay;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class WifiListener
        implements IPilotage {
    private ServerSocket socket;
    private Socket client;
    private boolean wifiOn = true;
    private final int port = 2048;
    private IPilote pilote;
    private Future execThread;
    private final ExecutorService execService = Executors.newSingleThreadExecutor();

    private BufferedReader in;
    private BufferedWriter out;

    /**
     * Lance le thread permettant d'écouter sur la connexion Wifi
     */
    public void ecouter() {
        this.execThread = this.execService.submit(() -> {
            try {
                // En cas d'erreur, on interrompt le thread
                socket = new ServerSocket(port);

                NetworkInterface wlan = NetworkInterface.getByName("wlan0");
                Enumeration<InetAddress> adresses = wlan.getInetAddresses();
                while (adresses.hasMoreElements()) {
                    InetAddress address = adresses.nextElement();
                    System.out.println("Adresse pour la connexion :");
                    System.out.println(address.getHostName());
                }

                // Tant que le robot doit écouter, et que le socket n'est pas fermé
                while (wifiOn && !socket.isClosed()) {
                    // On accepte une connexion et on obtient un socket vers le client connecté
                    // On récupère les buffers d'entrée et de sortie
                    client = socket.accept();
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                    // Et on attend un message de sa part.
                    while (!client.isClosed()) {
                        System.out.println("Attente d'un message...");
                        String s = in.readLine();

                        if (s != null) {
                            System.out.printf("Message reçu : %s%n", s);

                            // Notification du contrôleur
                            notifierPilote(s);
                        } else {
                            client.close();
                        }
                        Delay.msDelay(500); // Ajout d'un petit délai pour ne pas surcharger le thread
                    }
                    // Notification au contrôleur de la déconnexion
                    notifierDeconnexion();
                }
                System.out.println("Wifi terminée");
            } catch (SocketException e) {
                System.out.println("Arrêt du socket serveur.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Notifie le contrôleur du message reçu par le thread
     */
    private void notifierPilote(String message) {
        this.pilote.notifierMessage(message);
    }

    /**
     * Notifie le contrôleur de la fermeture du thread, en lui ordonnant de déconnecter l'utilisateur
     */
    private void notifierDeconnexion() {
        JsonObject root = new JsonObject();
        root.addProperty("action", "deco");
        notifierPilote(new Gson().toJson(root));
    }

    /**
     * Envoie au socket connecté les informations sur l'état du robot
     */
    public void afficherEtatSysteme() {
    }

    /**
     * Envoie au socket connecté les derniers logs en date
     */
    public void afficherHistorique(ArrayList<String> rapports) {
    }

    /**
     * Envoie au socket connecté un message JSON stringifié contenant des informations
     *
     * @param message Message envoyé, chaéne en format JSON ou texte
     */
    public void envoyerMessage(String message) {
        try {
            if (client.isConnected()) {
                out.write(String.format("%s\n", message));
                out.flush();
            }
        } catch (Exception e) {
            System.out.printf("Impossible d'envoyer un message : %s%n", e.getMessage());
        }
    }

    /**
     * Arréte le thread d'écoute et le clét
     */
    public void stop() {
        this.wifiOn = false;
        fermerConnexion();
        this.execThread.cancel(true);
        this.execService.shutdownNow();
    }

    /**
     * Ferme la connexion avec le client, et ferme ensuite le socket d'écoute
     */
    public void fermerConnexion() {
        try {
            this.client.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affecte le "listener", l'objet qui sera notifié des messages reçus
     *
     * @param listener Listener qui écoutera
     */
    public void ajoutListener(IPilote listener) {
        this.pilote = listener;
    }
}
