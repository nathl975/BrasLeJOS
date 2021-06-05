 package com.RobotArm.business;
 
 import com.RobotArm.interfaces.ICapteur;

import lejos.hardware.BrickFinder;
 import lejos.hardware.sensor.EV3TouchSensor;
 import lejos.hardware.sensor.SensorMode;
 
 
 public class CapteurContact implements ICapteur
 {
   private EV3TouchSensor sensor;
   private SensorMode mode;
   private char port;
   
   private static CapteurContact capteur;
   
     
   private CapteurContact(char port) {
	 this.port = port;
     this.sensor = new EV3TouchSensor(BrickFinder.getDefault().getPort("S" + this.port));
     this.mode = this.sensor.getTouchMode();
   }

   
   public static CapteurContact getInstance()
   {
	   return capteur;
   }

   public static void initCapteur(char port)
   {
	   if(capteur != null)
		   return;
	   capteur = new CapteurContact(port);
   }
   
   @Override
   public int getMesure() {
     float[] sample = new float[this.mode.sampleSize()];
     this.mode.fetchSample(sample, 0);
     return Math.round(sample[0]);
   }
 }
