package comm.core;

public interface LinkEventListener {
	
	public void onData(LinkEvent event);
	public void onStatus(LinkEvent event);
}
