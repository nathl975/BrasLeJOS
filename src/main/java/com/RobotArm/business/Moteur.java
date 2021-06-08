package com.RobotArm.business;
 
import com.RobotArm.interfaces.ICapteur;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;
 
 
 public class Moteur
 {
	private char port;
	private double ratio;
	private ICapteur capteur;
	private NXTRegulatedMotor NXTMotor;
	private int sensRotation; // Sens vers lequel tourner pour que le moteur arrive en butée de son capteur
		
	private static Moteur A, B, C, D;
	public static int SENS_NEGATIF = -1, SENS_POSITIF = 1;
	
	private Moteur(char port, double ratio, ICapteur capteur, float vitesse, int acceleration, int sensRotation) {
		this.port = port;
		this.ratio = ratio;
		this.capteur = capteur;
		
		this.setSensRotation(sensRotation);
		
		switch (this.port)
		{
			 case 'A':
				NXTMotor = Motor.A;
				NXTMotor.setStallThreshold(1, 50);				
				break;
			 case 'B':
				NXTMotor = Motor.B;
				NXTMotor.setStallThreshold(5, 250);
				break;
			 case 'C':
				NXTMotor = Motor.C;
				NXTMotor.setStallThreshold(5, 250);
				break;
			 case 'D':
				NXTMotor = Motor.D;
				NXTMotor.setStallThreshold(5, 250);
				break;
}
		NXTMotor.setSpeed(vitesse);
		NXTMotor.setAcceleration(acceleration);
		NXTMotor.setStallThreshold(5, 250);
	}
	
	
	public static void initMoteur(char port, float ratio, ICapteur capteur, float vitesse, int acceleration, int sensRotation)
	{
		switch(port)
		{
			case 'a':
			case 'A':
				if(A == null)
					A = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
				break;
			case 'b':
			case 'B':
				if(B == null)
					B = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
				break;				
			case 'c':
			case 'C':
				if(C == null)
					C = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
				break;				
			case 'd':
			case 'D':
				if(D == null)
					D = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
				break;				
		}
	}
	
	
	public static Moteur getInstance(char port)
	{
		switch(port)
		{
			case 'a':
			case 'A':
				return A;
			case 'b':
			case 'B':
				return B;
			case 'c':
			case 'C':
				return C;
			case 'd':
			case 'D':
				return D;				
			default:
				return null;
		}
	}
	
	
	public static void stopAll()
	{
		Motor.A.stop();
		Motor.B.stop();
		Motor.C.stop();
		Motor.D.stop();
		if(A != null) A.stop();
		if(B != null) B.stop();
		if(C != null) C.stop();
		if(D != null) D.stop();
	}
 

	public void tourner(int degres) {
		
		if(degres == 0 || degres == -0)
			return;
		
		boolean aBouge = false; // Vrai si le moteur a commencé à tourner
		float sens = Math.signum(degres);
		System.out.println(String.format("Moteur %s tourne sur %d", port, degres));			
		// On vérifie tout d'abord si le moteur peut tourner
		// Conditions pour que moteur tourne :
		// Si le moteur doit tourner vers un angle positif, alors il tourne vers le capteur.
		// Dans ce cas, on vérifie si le capteur détecte le moteur en butée.
		if(this.capteur.getMesure() == 1 && sens > 0.0f)
		{
			System.out.println(String.format("Moteur %s en butee", port));
			return;
		}
		// S'il peut tourner, alors on le fait tourner.
		this.NXTMotor.rotate((int)Math.round(degres * this.ratio * this.sensRotation), true);
		// Tant que les conditions pour s'arrêter ne sont pas remplies, on attend
		// Conditions pour s'arrêter : 
		// - Le capteur lance un signal, et le sens de rotation est vers le capteur
		// - OU le moteur s'est arrêté de tourner naturellement
		// - OU le moteur est bloqué (stalled) et force pour tourner
		Delay.msDelay(25);
		do
		{ } while ((this.capteur.getMesure() == 0 || sens < 0.0f) && (NXTMotor.isMoving()) && !NXTMotor.isStalled());
		this.NXTMotor.stop();
	}


	public void stop()
	{
		NXTMotor.stop();
	}


	private void setSensRotation(int sensRotation) {
		if(sensRotation != SENS_NEGATIF && sensRotation != SENS_POSITIF)
			sensRotation = SENS_POSITIF;
		this.sensRotation = sensRotation;
	}

 }
