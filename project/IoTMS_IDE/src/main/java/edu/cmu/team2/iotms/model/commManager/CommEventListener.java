package edu.cmu.team2.iotms.model.commManager;

public interface CommEventListener {

	public void onAdapterEvent(CommEvent event);
	public void onLinkEvent(CommEvent event);
	public void onDiscoveryEvent(CommEvent event);
}
