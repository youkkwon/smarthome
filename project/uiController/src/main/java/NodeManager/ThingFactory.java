package NodeManager;

public class ThingFactory extends Factory {
 
	@Override
	protected Thing createThing(String sType) {
		// TODO Auto-generated method stub
		// Door, Light, Presense, Temperature, Humidity, DoorSensor, Alarm, MialBox, Unknown
		
		switch(sType) {
		case "Actuator":
			Thing actuator = new Actuator();
			return actuator;
		case "Sensor":
			Thing sensor = new Sensor();
			return sensor;
		case "Unknown":
			Thing defaultThing = new DefaultThing();
			return defaultThing;
		default:
			break;
		}
		return null;
	}
}
