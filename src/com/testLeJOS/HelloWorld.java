/*    */ package com.testLeJOS;
/*    */ 
/*    */

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;
/*    */ 
/*    */ public class HelloWorld {
/*    */   public static void main(String[] args) {
/* 14 */     Brick brick = BrickFinder.getDefault();
/* 15 */     Port touchPort = brick.getPort("S1");
/* 16 */     EV3TouchSensor touchSensor = new EV3TouchSensor(touchPort);
/* 17 */     SensorMode mode = touchSensor.getTouchMode();
/* 18 */     float[] measures = new float[1];
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 36 */     Motor.A.setSpeed(200);
/* 37 */     Motor.B.setSpeed(200);
/* 38 */     Motor.C.setSpeed(100);
/*    */     
/* 40 */     Motor.A.setAcceleration(200);
/* 41 */     Motor.B.setAcceleration(200);
/* 42 */     Motor.C.setAcceleration(200);
/*    */ 
/*    */     
/* 45 */     Motor.C.rotate(1080, true);
/*    */     
/*    */     while (true) {
/* 48 */       mode.fetchSample(measures, 0);
/* 49 */       if (measures[0] != 0.0F) {
/* 50 */         Motor.C.stop();
/*    */         
/* 52 */         Motor.C.resetTachoCount();
/*    */ 
/*    */         
/* 55 */         LCD.drawString("Programme test", 0, 0);
/* 56 */         LCD.drawString("Ce programme permet", 0, 1);
/* 57 */         LCD.drawString("de tester les différents", 0, 2);
/* 58 */         LCD.drawString("capteurs et moteurs", 0, 3);
/* 59 */         LCD.drawString("du robot EV3.", 0, 4);
/*    */         
/* 61 */         Delay.msDelay(5000L);
/*    */         
/* 63 */         LCD.clear();
/* 64 */         LCD.drawString("test de saisie", 0, 4);
/* 65 */         Motor.A.rotate(-45);
/* 66 */         Motor.A.rotate(45);
/* 67 */         Delay.msDelay(1000L);
/*    */         
/* 69 */         LCD.clear();
/* 70 */         LCD.drawString("Test de levée", 0, 4);
/* 71 */         Motor.B.rotate(-150);
/* 72 */         Motor.B.rotate(150);
/* 73 */         Delay.msDelay(1000L);
/*    */         
/* 75 */         LCD.clear();
/* 76 */         LCD.drawString("Test de rotation", 0, 4);
/* 77 */         Motor.C.rotate(-600);
/* 78 */         Motor.C.rotate(0);
/* 79 */         Delay.msDelay(1000L);
/* 80 */         LCD.clear();
/* 81 */         LCD.drawString("Terminé", 0, 0);
/* 82 */         Delay.msDelay(3000L);
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Alvin\eclipse-workspace\backup\RobotArm.jar!\com\testLeJOS\HelloWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */