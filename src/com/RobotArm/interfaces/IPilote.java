package com.RobotArm.interfaces;

/**
 * Interface permettant d'étre notifié par le systéme de pilotage
 * @author Alvin
 *
 */
public interface IPilote
{
	/**
	 * Appelée lorsqu'un message est reéu par le systéme de pilotage
	 * @param paramString le message
	 */
	void notifierMessage(String paramString);
}
