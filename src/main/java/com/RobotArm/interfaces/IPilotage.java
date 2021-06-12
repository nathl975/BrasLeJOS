package com.RobotArm.interfaces;

import java.util.ArrayList;

/**
 * Interface implémentée par le système de pilotage, afin de communiquer avec l'opérateur
 * @author Alvin
 *
 */
public interface IPilotage {
	/**
	 * Transmet l'état du robot à l'opérateur
	 */
	void afficherEtatSysteme();
	
	/**
	 * Transmet l'historique des logs à l'opérateur
	 */
	void afficherHistorique(ArrayList<String> paramArrayList);
	
	/**
	 * Lance la logique de réception de messages du système de pilotage
	 */
	void ecouter();
	
	/**
	 * Définit le module pilote à notifier lors de la réception d'un message
	 */
	void ajoutListener(IPilote paramIPilote);
	
	/**
	 * Envoie un message à l'opérateur
	 */
	void envoyerMessage(String paramString);
	
	/**
	 * Coupe la connexion entre le robot et l'opérateur
	 */
	void fermerConnexion();
	
	/**
	 * Arrête le système de pilotage, en vue d'un arrêt du robot
	 */
	void stop();
}
