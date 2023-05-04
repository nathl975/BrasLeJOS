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
	
	/**
	* Constructeur de la classe Singleton CapteurContact
	* @param port Port sur lequel le capteur est branché
	*/
	private CapteurContact(char port) {
	this.port = port;
		this.sensor = new EV3TouchSensor(BrickFinder.getDefault().getPort("S" + this.port));
		this.mode = this.sensor.getTouchMode();
	}

	/**
	*
	* @return Instance unique de la classe
	*/
	public static CapteurContact getInstance()
	{
		return capteur;
	}
	
	
	/**
	* Initialise l'instance unique de la classe
	* @param port Port sur lequel le capteur est branché
	*/
	public static void initCapteur(char port)
	{
		if(capteur != null)
			return;
		capteur = new CapteurContact(port);
	}
	
	/**
	 * @return Valeur mesurée par le capteur : 1 si enfoncé, 0 sinon
	 */
	@Override
	public int getMesure() {
		float[] sample = new float[this.mode.sampleSize()];
		this.mode.fetchSample(sample, 0);
		return Math.round(sample[0]);
	}
}
