package node;

import comm.Link;
import comm.LinkEvent;
import comm.LinkEventListener;

public class Node implements LinkEventListener {

	private Link link;
	
	public Node(Link l)
	{
		link = l;
		System.out.println("Create a new node (mac: " + getMACAddress() +")");
		link.addListener(this);
	}

	public void send(String msg)
	{
		link.send(msg);
	}

	public void disconnect()
	{
		link.disconnect();
	}
	
	public void updateLink(Link l)
	{
		link = l;
		System.out.println("Update the link (mac: " + getMACAddress() +")");
		link.addListener(this);
	}
	
	public String getMACAddress()
	{
		return link.getMACAddress();
	}

	@Override
	public void onData(LinkEvent event) {
		// TODO Auto-generated method stub

		System.out.println("# Node Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
	}

	@Override
	public void onStatus(LinkEvent event) {
		// TODO Auto-generated method stub

		System.out.println("# Node Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
	}
}
