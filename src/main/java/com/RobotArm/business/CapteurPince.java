package com.RobotArm.business;

import com.RobotArm.interfaces.ICapteur;

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
		//TODO : implémenter la logique pour déterminer si la pince doit s'arrêter.
		return 0;
	}

}
