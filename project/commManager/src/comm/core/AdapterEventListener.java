package comm.core;

public interface AdapterEventListener {
	
	public void onDiscovered(AdapterEvent event);
	public void onRegistered(AdapterEvent event);
	public void onAddNode(AdapterEvent event);
}
