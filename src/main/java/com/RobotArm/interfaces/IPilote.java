package com.RobotArm.interfaces;

/**
 * Interface permettant d'être notifié par le système de pilotage
 * @author Alvin
 *
 */
public interface IPilote
{
	/**
	 * Appelée lorsqu'un message est reçu par le système de pilotage
	 * @param paramString
	 */
	void notifierMessage(String paramString);
}
