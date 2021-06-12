 package com.RobotArm.business;

import com.RobotArm.interfaces.ICapteur;

import lejos.hardware.BrickFinder;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;

/**
 * Capteur relié à la levée du bras
 * @author Alvin
 *
 */
public class CapteurCouleur implements ICapteur
{
	private EV3ColorSensor sensor;
	private SensorMode mode;
	private char port;
	
	private static CapteurCouleur capteur;
	
	
	private CapteurCouleur(char port){
	this.port = port;
		this.sensor = new EV3ColorSensor(BrickFinder.getDefault().getPort("S" + this.port));
		this.mode = this.sensor.getRedMode();
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
	public int getMesure(){
		float[] mesures = new float[this.mode.sampleSize()];
		this.mode.fetchSample(mesures, 0);		
		return mesures[0] >= 0.2f ? 1 : 0;
	}
}
