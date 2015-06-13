package NodeManager;

public class ThingFactory extends Factory {
 
	@Override
	protected Thing createThing(Type type, int id) {
		// TODO Auto-generated method stub
		// Door, Light, Presense, Temperature, DoorSensor, Alarm, MialBox, Unknown
		switch(type) {
		case Door :
		case Light:
		case AlarmLamp:
			Thing actuator = new Actuator(type, id);
			return actuator;
		case Presense:
		case Temperature:
		case Humidity:
		case DoorSensor:
		case MialBox:
			Thing sensor = new Sensor(type, id);
			return sensor;
		case Unknown:
			Thing defaultThing = new DefaultThing(type, id);
			return defaultThing;
		default:
			break;
		}
		return null;
	}

}
