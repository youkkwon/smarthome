package comm.core;

public abstract class Adapter {

	public abstract void addListener(AdapterEventListener listener);
	
	public abstract void discoverNode(int duration);
	public abstract void registerNode(String msg);;
	public abstract void disconnectNode(String mac);

}
