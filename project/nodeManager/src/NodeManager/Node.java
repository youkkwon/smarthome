package NodeManager;

import java.util.ArrayList;
import comm.Link;
import comm.LinkEvent;
import comm.LinkEventListener;

import org.json.simple.JSONObject;

public class Node implements LinkEventListener {
	private String macAddress;
	private ArrayList<Thing> Things = new ArrayList<Thing>();
	private Factory thingFactory = new ThingFactory();
	
	public Node(Link link) {
		// Creator
		this.macAddress = link.getMACAddress();
		
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
	
	public String getMacAddress() {
		return this.macAddress;
	}
	
	public void setMacAddress(String macAddr) {
		this.macAddress = macAddr;
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

	@Override
	public void onData(LinkEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus(LinkEvent event) {
		// TODO Auto-generated method stub
		
	}

}
