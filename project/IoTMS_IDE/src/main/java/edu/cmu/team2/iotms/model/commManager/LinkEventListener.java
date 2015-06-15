package edu.cmu.team2.iotms.model.commManager;

public interface LinkEventListener {
	
	public void onData(LinkEvent event);
	public void onStatus(LinkEvent event);
}
