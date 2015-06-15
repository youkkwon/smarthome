package edu.cmu.team2.iotms.model.commManager;

public interface AdapterEventListener {
	
	public void onDiscovered(AdapterEvent event);
	public void onRegistered(AdapterEvent event);
	public void onAddNode(AdapterEvent event);
}
