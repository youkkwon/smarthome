package comm;

public interface CommEventListener {

	public void onAdapterEvent(CommEvent event);
	public void onLinkEvent(CommEvent event);
	public void onDiscoveryEvent(CommEvent event);
}
