package edu.cmu.team2.iotms.model.nodeManager;

public abstract class Factory {	
	public Thing create(String type) {
		return createThing(type);
	}
	protected abstract Thing createThing(String type);
}
