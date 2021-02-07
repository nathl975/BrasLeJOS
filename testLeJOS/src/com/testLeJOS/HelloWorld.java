package com.testLeJOS;

import lejos.hardware.*;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.*;
import lejos.hardware.port.*;
import lejos.utility.Delay;
import java.lang.Thread;

public class HelloWorld {

	public static void main(String[] args) {
	    Brick brick = BrickFinder.getDefault();
	    Port touchPort = brick.getPort("S1");	
	    EV3TouchSensor touchSensor = new EV3TouchSensor(touchPort);
	    SensorMode mode = touchSensor.getTouchMode();
	    float[] measures = new float[1];
	    
	    // Capteur 1 : toucher
	    // Capteur 2 : lumière
	    // Chaque capteur possède plusieurs modes de mesure
	    // Tous les modes sont actifs, il suffit en fait de récupérer les informations que l'on veut
	    // En utilisant le mode approprié.
		// Moteur A : pince
		// Moteur B : levée
		// Moteur C : pivot
		// Lorsqu'on fait tourner le moteur de 180°, le moteur tourne bien de 180°
		// Mais par rapport de puissance dû aux engrenages, le bras tourne beaucoup moins vite.
	    
		// Le rapport de puissance entre le moteur A et la pince serait d'environ 1/2 (12/12/24).
		// Le rapport de puissance entre le moteur B et le bras serait d'environ 1/5 (8/40).
		// Le rapport de puissance entre le moteur C et le bras serait d'environ 1/3 (12/36).
	    
	    // pour éviter que les moteurs ne fassent des jumpscares
		Motor.A.setSpeed(200);
		Motor.B.setSpeed(200);
		Motor.C.setSpeed(100);

		Motor.A.setAcceleration(200);
		Motor.B.setAcceleration(200);
		Motor.C.setAcceleration(200);

		// tourner le bras jusqu'à ce qu'il bute sur le capteur
		Motor.C.rotate(1080, true);
		do
		{
			mode.fetchSample(measures, 0);
		} while(measures[0] == 0);
		Motor.C.stop();
		// réinitialiser la position 0 du moteur
		Motor.C.resetTachoCount();
		
		// Message d'accueil
		LCD.drawString("Programme test", 0, 0);
		LCD.drawString("Ce programme permet", 0, 1);
		LCD.drawString("de tester les différents", 0, 2);
		LCD.drawString("capteurs et moteurs", 0, 3);
		LCD.drawString("du robot EV3.", 0, 4);
		
		Delay.msDelay(5000);
		// test de la pince
		LCD.clear();
		LCD.drawString("test de saisie", 0, 4);
		Motor.A.rotate(-45);
		Motor.A.rotate(45);		
		Delay.msDelay(1000);
		// test du lever du bras
		LCD.clear();
		LCD.drawString("Test de levée", 0, 4);
		Motor.B.rotate(-150);
		Motor.B.rotate(150);
		Delay.msDelay(1000);
		// test de rotation
		LCD.clear();
		LCD.drawString("Test de rotation", 0, 4);		
		Motor.C.rotate(-600);
		Motor.C.rotate(0);
		Delay.msDelay(1000);
		LCD.clear();
		LCD.drawString("Terminé", 0, 0);
		Delay.msDelay(3000);
	}
}
