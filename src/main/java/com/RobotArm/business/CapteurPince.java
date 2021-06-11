package com.RobotArm.business;

import com.RobotArm.interfaces.ICapteur;

/**
 * Capteur relié à la pince. La pince n'a pas de capteur, cette classe est factice
 * @author Alvin
 *
 */
public class CapteurPince implements ICapteur {

	private static CapteurPince capteur;
	
   public static CapteurPince getInstance()
   {
	   return capteur;
   }

   public static void initCapteur()
   {
	   if(capteur != null)
		   return;
	   capteur = new CapteurPince();
   }
	 	
	
	@Override
	public int getMesure()
	{
		//TODO trouver une logique plus intéressante pour que la pince soit arrêtée lorsqu'elle force
		return 0;
	}

}
