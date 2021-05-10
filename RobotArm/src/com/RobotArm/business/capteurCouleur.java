import lejos.hardware.sensor.*;

class CapteurCouleur
{
	private EV3ColorSensor sensor;
	private SensorMode mode;
	private char port;
	
	
	public CapteurCouleur(char port)
	{
		this.sensor = brick.getPort("S" + port);
		this.mode = sensor.getColorMode();
	}
	
	
	public String getMesure()
	{
		return sensor.getMeasture()[0].toString();
	}	
}