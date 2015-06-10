package NodeManager;

import NodeManager.Things.Alarm;
import NodeManager.Things.Door;
import NodeManager.Things.DoorSensor;
import NodeManager.Things.Humidity;
import NodeManager.Things.Light;
import NodeManager.Things.MialBox;
import NodeManager.Things.Presense;
import NodeManager.Things.Temperature;
import NodeManager.Things.Unknown;

public class ThingFactory extends Factory {
 
	@Override
	protected Thing createThing(Type type, int id) {
		// TODO Auto-generated method stub
		// Door, Light, Presense, Temperature, DoorSensor, Alarm, MialBox, Unknown
		switch(type) {
		case Door :
			Thing door = new Door(type, id);
			return door;
		case Light:
			Thing light = new Light(type, id);
			return light;
		case Presense:
			Thing presense = new Presense(type, id);
			return presense;
		case Temperature:
			Thing temperature = new Temperature(type, id);
			return temperature;
		case Humidity:
			Thing humidity = new Humidity(type, id);
			return humidity;
		case DoorSensor:
			Thing doorSensor = new DoorSensor(type, id);
			return doorSensor;
		case AlarmLamp:
			Thing alarm = new Alarm(type, id);
			return alarm;
		case MialBox:
			Thing mialBox = new MialBox(type, id);
			return mialBox;
		case Unknown:
			Thing unknown = new Unknown(type, id);
			return unknown;
		default:
			break;
		}
		return null;
	}

}
