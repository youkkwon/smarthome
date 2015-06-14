package NodeManager;

import java.util.ArrayList;

import comm.Link;
import comm.LinkEvent;
import comm.LinkEventListener;

import org.json.simple.JSONObject;

public class Node implements LinkEventListener {
	private ArrayList<Thing> Things = new ArrayList<Thing>();
	private Factory thingFactory = new ThingFactory();
	private Link link;
	
	public Node(Link l) {
		// Creator
		link = l;
		System.out.println("Create a new node (mac: " + getMacAddress() +")");
		link.addListener(this);
		
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
		return link.getMACAddress();
	}
	
	public JSONObject getThingInfo(String thingName, JSONObject JSONMsg) {
		Thing thing = getThing(thingName);
		if (thing == null) {
			System.out.println ("[getThingInfo] Error: Thing is null");
			return null;
		}
		JSONObject ret = thing.getValue(JSONMsg);
		// add node id
		ret.put("NodeID", getMacAddress());
		
		return ret;
	}
	
	public JSONObject getThingInfo(int index, JSONObject JSONMsg) {
		JSONObject ret = Things.get(index).getValue(JSONMsg);
		// add node id
		ret.put("NodeID", getMacAddress());
		
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
	public void doThingCommand(String thingId, JSONObject JSONMsg) {
		Thing thing = getThing(thingId);
		if (thing == null) {
			System.out.println ("[doThingCommand] Error: Thing is null");
			return;
		}
		thing.doCommand(JSONMsg);
	}

	@Override
	public void onData(LinkEvent event) {
		// TODO Auto-generated method stub
		System.out.println("# Node Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
	}

	@Override
	public void onStatus(LinkEvent event) {
		// TODO Auto-generated method stub
		// Disconnect일때 OnStatus가 날라옴
		System.out.println("# Node Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
	}

}
