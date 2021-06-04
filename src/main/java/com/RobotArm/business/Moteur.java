package com.RobotArm.business;

import com.RobotArm.interfaces.ICapteur;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;


public class Moteur
{
	private char port;
	private double ratio;
	private ICapteur capteur;
	private NXTRegulatedMotor NXTMotor;
	
	private static Moteur A, B, C;
	
	
	private Moteur(char port, double ratio, ICapteur capteur) {
		this.port = port;
		this.ratio = ratio;
		this.capteur = capteur;
		
		switch (this.port)
		{		 
			 case 'A':
				NXTMotor = Motor.A;
				break;
			 case 'B':
				NXTMotor = Motor.B;
				break;
			 case 'C':
				NXTMotor = Motor.C;
				break;
		}
	}
	
	
	public static void initMoteur(char port, float ratio, ICapteur capteur)
	{
		switch(port)
		{
			case 'a':
			case 'A':
				if(A == null)
					A = new Moteur(port, ratio, capteur);
			case 'b':
			case 'B':
				if(B == null)
					B = new Moteur(port, ratio, capteur);
			case 'c':
			case 'C':
				if(C == null)
					C = new Moteur(port, ratio, capteur);
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
			default:
				return null;
		}
	}

	
	public void tourner(int degres) {
		this.NXTMotor.rotate((int)Math.round(degres * this.ratio), true);
		do { } while (this.capteur.getMesure() == 0 && NXTMotor.isMoving() && !NXTMotor.isStalled());
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
