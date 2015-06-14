package NodeManager;

public abstract class Factory {	
	public Thing create(SensorType type) {
		return createThing(type);
	}
	protected abstract Thing createThing(SensorType type);
}
