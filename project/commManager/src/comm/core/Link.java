package comm.core;

public abstract class Link {
	
	public abstract void send(String message);
	public abstract void disconnect();
	public abstract String getMACAddress();
	public abstract void addListener(LinkEventListener l);
	
}
