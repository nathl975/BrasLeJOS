import lejos.hardware.sensor.*;

class CapteurContact
{
	private EV3ColorSensor sensor;
	private SensorMode mode;
	private char port;
	

	public CapteurContact(char port)
	{
		this.sensor = brick.getPort("S" + port);
		this.mode = sensor.getTouchMode();
	}
	
	
	public String getMesure()
	{
		return sensor.getMeasture()[0].toString();
	}
}