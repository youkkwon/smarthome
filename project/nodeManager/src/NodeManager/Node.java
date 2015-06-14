package NodeManager;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class Node {
	private String macAddress;
	private String ipAddress;
	private String hostName;
	private ArrayList<Thing> Things = new ArrayList();
	private Factory thingFactory = new ThingFactory();
	
	public Node(String macAddr, String ipAddr, String hName, JSONObject JSONMsg) {
		// Creator
		this.ipAddress = ipAddr;
		this.hostName = hName;
		this.macAddress = macAddr;
		
		// loop for things
		/*
		for () {
			parse things about JSONMsg
			AddThing(type, name);
		}
		*/
	}
	
	public String showInfo() {
		String ret = "";
		
		
		return ret; 
	}
		
	public void addThing(SensorType sType, Type type, String id) {
		Thing thing = thingFactory.create(sType);
		thing.setType(type);
		thing.setId(id);
		Things.add(thing);
	}
	
	public void removeThing() {
		
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public String getMacAddress() {
		return macAddress;
	}
	
	public void setMacAddress(String macAddr) {
		this.macAddress = macAddr;
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public JSONObject getThingInfo(String thingName, JSONObject JSONMsg) {
		JSONObject ret = getThing(thingName).getValue(JSONMsg);
		// add node id
		ret.put("NodeID", this.macAddress);
		
		return ret;
	}
	
	public JSONObject getThingInfo(int index, JSONObject JSONMsg) {
		JSONObject ret = Things.get(index).getValue(JSONMsg);
		// add node id
		ret.put("NodeID", this.macAddress);
		
		return ret;
	}
	
	public Thing getThing(String thingId) {
		int index = 0;
		for(index=0; index<Things.size(); index++) {
			if (thingId.equals(Things.get(index).getId()))
					return Things.get(index);
		}
		return null;
	}
	
	public int getThingCount() {
		return Things.size();
	}
	
	// To SA Node
	public JSONObject getThingCommand(String thingId, JSONObject JSONMsg) {
		getThing(thingId).doCommand(JSONMsg);
		
		return JSONMsg;
	}

}
