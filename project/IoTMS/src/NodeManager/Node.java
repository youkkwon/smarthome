package NodeManager;

import java.util.ArrayList;

import CommManager.Link;
import CommManager.LinkEvent;
import CommManager.LinkEventListener;
import EventBus.IoTMSEventBus;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

public class Node implements LinkEventListener {
	private ArrayList<Thing> Things = new ArrayList<Thing>();
	private Factory thingFactory = new ThingFactory();
	IoTMSEventBus ev_bus = IoTMSEventBus.getInstance();
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
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
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
	
	/* from SA Node
	 * SA Node로부터 올라온 Data를 Update한다.
	*/
	@SuppressWarnings("unchecked")
	public void updateThingInfo(JSONObject JSONMsg) { 
		Node node = null;
		Thing thing = null;
		JSONObject thingObj = null;
		
		String nodeID = (String)JSONMsg.get("NodeID");
		
		if (nodeID.equalsIgnoreCase(getMacAddress())) {
			System.out.println ("[UpdateThingInfo] Error: cannot find Node...ignore it : " + JSONMsg);
			return;
		}
		
		JSONArray thingInfos = (JSONArray) JSONMsg.get("Status");
		for (int i=0; i < thingInfos.size(); i++)
		{
			thingObj = (JSONObject) thingInfos.get(i);
			String thingID = (String)thingObj.get("Id");
			thing = getThing(thingID); 
			if (thing != null) {
				if (thing.setValue((String)thingObj.get("Value")) == false) {
					// 값의 변경이 없음. 해당 Object 삭제
					thingInfos.remove(i);
				}
			}
			else {
				System.out.println ("[UpdateThingInfo] Error: cannot find Thing...ignore it : " + JSONMsg);
			}
		}
		
		// 바뀐 data만 정리해서 보냄
		JSONArray targets = new JSONArray();
		targets.add("RuleManager");
		targets.add("UI");
		
		JSONMsg.remove("Targets");
		JSONMsg.remove("Job");
		
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "ThingCtrl");
		
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	// To SA Node
	public void doThingCommand(String thingId, JSONObject JSONMsg) {
		Thing thing = getThing(thingId);
		if (thing == null) {
			System.out.println ("[doThingCommand] Error: Thing is null");
			return;
		}
		thing.doCommand(JSONMsg);
		send(JSONMsg.toString());
	}

	public void send(String msg)
	{
		link.send(msg);
	}

	public void disconnect()
	{
		link.disconnect();
		link = null;
	}
	
	public void updateLink(Link l)
	{
		link = l;
		System.out.println("Update the link (mac: " + getMacAddress() +")");
		link.addListener(this);
	}
	
	@Override
	public void onData(LinkEvent event) {
		// TODO Auto-generated method stub
		System.out.println("# Node Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
		JSONObject thingObj = null;
		thingObj = (JSONObject) JSONValue.parse(event.getMessage());
		if (thingObj == null) {
			System.out.println("JSON Object is null from SA node");
			return;
		}
		updateThingInfo(thingObj);
	}

	@Override
	public void onStatus(LinkEvent event) {
		// TODO Auto-generated method stub
		// Disconnect일때 OnStatus가 날라옴
		System.out.println("# Node Event: " + event.getType() + ":" + event.getStatus() + ":" + event.getMessage());
	}

}
