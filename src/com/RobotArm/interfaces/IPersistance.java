package com.RobotArm.interfaces;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Utilisateur;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface implémentée par le systéme de persistance, pour garder en mémoire les données
 * @author Alvin
 *
 */
public interface IPersistance {
	/**
	* Sauvegarde une gamme dans la persistance
	* @param paramGamme Gamme é sauvegarder
	*
	*/
	void creerGamme(Gamme paramGamme);
	
	/**
	* Modifie une gamme dans la persistance
	* @param paramGamme Gamme é modifier
	*
	*/
	void modifierGamme(Gamme paramGamme);
	
	/**
	* Supprime une gamme dans la persistance
	* @param paramString Gamme é supprimer
	*/
	void supprimerGamme(String paramString);
	
	/**
	* @return Liste des gammes sauvegardées
	*/
	HashMap<String, Gamme> recupererGammes();
	
	/**
	* 
	* @return Retourne la gamme définie par défaut, utilisée pour le mode automatique
	*/
	Gamme recupererGammeDefaut();
	
	/**
	* Sauvegarde un log dans la persistance
	* @param paramString
	*
	*/
	void sauverLog(String paramString);
	
	/**
	* @return Liste des logs récents
	*
	*/
	ArrayList<String> recupererLogs();
	
	/**
	*  Retourne la valeur d'un paramétre sauvegardé en persistance
	* @param paramString Nom du paramétre é lire
	* @return Valeur du paramétre passé en paramétre, ou null si le paramétre n'existe pas
	*
	*/
	String getConfig(String paramString);
	
	/**
	* Crée un compte dans la persistance
	* @param login Identifiant de l'utilisateur
	* @param pwd Mot de passe chiffré de l'utilisateur
	*
	*/
	void creerCompte(String login, String pwd);

	/**
	* @param l Identifiant de l'utilisateur
	* @param p Mot de passe
	* @return Utilisateur trouvé ou null
	*/
	Utilisateur trouverCompte(String l, String p);
	
	/**
	* Supprime un compte utilisateur
	* @param login Identifiant du compte é supprimer
	*
	*/
	void supprimerCompte(String login);
}
