package NodeManager;

public abstract class Factory {	
	public Thing create(Type type, int id) {
		return createThing(type, id);
	}
	protected abstract Thing createThing(Type type, int id);
}
