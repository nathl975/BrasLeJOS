 package com.RobotArm.business;
 
import com.RobotArm.interfaces.ICapteur;

import lejos.hardware.BrickFinder;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;
 
 
 public class CapteurCouleur implements ICapteur
 {
   private EV3ColorSensor sensor;
   private SensorMode mode;
   private char port;
   
   private static CapteurCouleur capteur;
   
   
   private CapteurCouleur(char port) {
	 this.port = port;
     this.sensor = new EV3ColorSensor(BrickFinder.getDefault().getPort("S" + this.port));
     this.mode = this.sensor.getColorIDMode();
   }
 
   
   public static CapteurCouleur getInstance()
   {
	   return capteur;
   }

   public static void initCapteur(char port)
   {
	   if(capteur != null)
		   return;
	   capteur = new CapteurCouleur(port);
   }

 
   @Override
   public int getMesure() {
     return this.sensor.getColorID() == Color.WHITE ? 1 : 0;
   }
 }
