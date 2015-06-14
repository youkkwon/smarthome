package node;

import java.util.ArrayList;

import comm.Adapter;
import comm.AdapterEvent;
import comm.AdapterEventListener;
import comm.Link;

public class NodeManager implements AdapterEventListener {
	
	Adapter wifiAdapter;
	ArrayList<Node> nodeList;
	
	public NodeManager()
	{
		initialize();
	}
	
	public void initialize()
	{
		wifiAdapter = new Adapter();
		wifiAdapter.addListener(this);
		nodeList = new ArrayList<Node>();
	}

	public void discoverNode(int duration)
	{
		wifiAdapter.discoverNode(duration);
	}

	public void registerNode(String msg)
	{
		wifiAdapter.registerNode(msg);
	}

	public void rejectNode(String mac)
	{
		wifiAdapter.disconnectNode(mac);
	}
	
	private Node findNode(String mac)
	{
		for(Node node : nodeList)
		{
			if(node.getMACAddress().equals(mac))
				return node;
		}	
		return null;
	}
	
	public void send(String mac, String msg)
	{
		Node node = findNode(mac);
		if(node != null)
			node.send(msg);
	}
	
	public void disconnect(String mac)
	{
		Node node = findNode(mac);
		if(node != null)
			node.disconnect();
	}

	@Override
	public void onDiscovered(AdapterEvent event) {
		// TODO Auto-generated method stub

		System.out.println("NM Discovered Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
	}

	@Override
	public void onRegistered(AdapterEvent event) {
		// TODO Auto-generated method stub

		System.out.println("NM Registered Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
	}

	@Override
	public void onAddNode(AdapterEvent event) {
		// TODO Auto-generated method stub
		System.out.println("NM Adapter Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
		
		if(event.getType().equals("Add_node"))
		{
			// 1. look-up DB
			
			// 2. add new node or update the existing node
			Node node = findNode(((Link)event.getLink()).getMACAddress());
			if(node == null)
			{
				System.out.println("Add new node = " + ((Link)event.getLink()).getMACAddress());
				nodeList.add(new Node((Link)(event.getLink())));
			}
			else
			{
				System.out.println("Existing node = " + ((Link)event.getLink()).getMACAddress());
				node.updateLink((Link)(event.getLink()));
			}
		}
	}
}
