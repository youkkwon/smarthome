package NodeManager;


public class ThingFactory extends Factory {
 
	@Override
	protected Thing createThing(Type type, int id) {
		// TODO Auto-generated method stub
		switch(type) {
		case Door :
			Thing door = new Door(type, id);
			return door;
		case Light:
			Thing light = new Light(type, id);
			return light;
		case Temperature:
			Thing temperature = new Temperature(type, id);
			return temperature;
		case Unknown:
			break;
		}
		return null;
	}

}
