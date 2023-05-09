package com.RobotArm.interfaces;

/**
 * Interface implémentée par le système de persistance, pour garder en mémoire les données
 * @author Alvin
 *
 */
public interface IPersistance extends IGestionLogs, IGestionGammes, IGestionUtilisateurs {
	/**
	*  Retourne la valeur d'un paramètre sauvegardé en persistance
	* @param paramString Nom du paramètre à lire
	* @return Valeur du paramètre passé en paramètre, ou null si le paramètre n'existe pas
	*
	*/
	String getConfig(String paramString);
}
