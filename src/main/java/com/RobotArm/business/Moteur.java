 package com.RobotArm.business;
 
 import com.RobotArm.interfaces.ICapteur;
 import lejos.hardware.motor.Motor;
 
 
 
 
 public class Moteur
 {
   private char port;
   private double ratio;
   private ICapteur capteur;
   
   public Moteur(char port, double ratio) {
     this.port = port;
     this.ratio = ratio;
   }
 
 
   
   public void tourner(int degres) {
     switch (this.port) {
       
       case 'A':
         Motor.A.rotate((int)Math.round(degres * this.ratio), true);
         break;
       case 'B':
         Motor.B.rotate((int)Math.round(degres * this.ratio), true);
         break;
       case 'C':
         Motor.C.rotate((int)Math.round(degres * this.ratio), true); break;
     } 
     do {
     
     } while (this.capteur.getMesure() == 0);
   }
 
 
   
   public void stop() {
     switch (this.port) {
       
       case 'A':
         Motor.A.stop();
         break;
       case 'B':
         Motor.B.stop();
         break;
       case 'C':
         Motor.C.stop();
         break;
     } 
   }
 }
