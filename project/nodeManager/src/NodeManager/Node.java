package NodeManager;

import java.util.List;

import org.json.simple.JSONObject;

public class Node {
	private int nodeId;
	private String ipAddress;
	private String hostName;
	private List<Thing> Things;
	private Factory thingFactory = new ThingFactory();
	
	public Node(int id, String ipAddr, String hName, String things) {
		// Creator
		this.nodeId = id;
		this.ipAddress = ipAddr;
		this.hostName = hName;
		
		// loop for things
		/*
		for () {
			parse things
			AddThing(type);
		}
		*/
	}
	
	public String ShowInfo() {
		String ret = "";
		
		
		return ret; 
	}
		
	public void AddThing(Type type) {
		Thing thing = thingFactory.create(type, Things.size());
		Things.add(thing);
	}
	
	public void RemoveThing() {
		
	}
	
	public void setNodeID(int id) {
		this.nodeId = id;
	}
	
	public int getNodeID() {
		return nodeId;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public JSONObject getThingInfo(int thingId, JSONObject JSONMsg) {
		JSONObject ret = Things.get(thingId).GetValue(JSONMsg);
		// add node id
		ret.put("NodeID", Integer.toString(nodeId));
		
		return ret;
	}
	
}
