 package com.RobotArm.business;
 
 import lejos.hardware.BrickFinder;
 import lejos.hardware.sensor.EV3TouchSensor;
 import lejos.hardware.sensor.SensorMode;
 
 
 public class CapteurContact
 {
   private EV3TouchSensor sensor;
   private SensorMode mode;
   private char port;
   
   public CapteurContact(char port) {
     this.sensor = new EV3TouchSensor(BrickFinder.getDefault().getPort("S" + port));
     this.mode = this.sensor.getTouchMode();
   }
 
 
   
   public int getMesure() {
     float[] sample = new float[this.mode.sampleSize()];
     this.mode.fetchSample(sample, 0);
     return Math.round(sample[0]);
   }
 }
