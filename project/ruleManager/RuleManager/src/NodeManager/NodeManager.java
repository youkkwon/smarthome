package NodeManager;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.IoTMSEventBus;
import CommManager.Adapter;
import CommManager.AdapterEvent;
import CommManager.AdapterEventListener;
import CommManager.Link;

enum Type {
	Door, Light, Presense, Temperature, Humidity, DoorSensor, AlarmLamp, MialBox, Unknown
}

enum SensorType {
	Actuator, Sensor, Unknown
}

public class NodeManager implements AdapterEventListener {
	// Singletone
	private static NodeManager uniqueInstance = new NodeManager();
	public static NodeManager getInstance() {
		return uniqueInstance;
	}
	
	private ArrayList<Node> Nodes = new ArrayList<Node>();
	private Adapter adapter = new Adapter();
	
	public NodeManager() {
		adapter.addListener(this);
	}
		
	private Node getNode(String macAddr) {
		int index = 0;
		for(index=0; index<Nodes.size(); index++) {
			if (macAddr.equals(Nodes.get(index).getMacAddress()))
					return Nodes.get(index);
		}
		return null;
	}
	
	private void removeNode(int id) {
		Nodes.remove(id);
	}
	
	// from SA Node
	public void addNode(Link link) {
		Node node = new Node(link);
		Nodes.add(node);
	}
	
	// from UI
	public void showNodeInfo(JSONObject JSONMsg) {
		String nodeId = (String)JSONMsg.get("NodeID");
		
		JSONObject info = new JSONObject();
		JSONArray thingArray = new JSONArray();
		
		for(int i=0; i<getNode(nodeId).getThingCount(); i++) {
			JSONObject thingInfo = new JSONObject();
			thingInfo = getNode(nodeId).getThingInfo(i, info);
			thingArray.add(thingInfo);
		}

		JSONArray targets = new JSONArray();
		targets.add("UI");
		sendEvent(info, targets, "ThingMonitor");
	}
	
	// from UI
	public void showThingInfo(JSONObject JSONMsg) {
		String nodeId = (String)JSONMsg.get("NodeID");
		String thingId = (String)JSONMsg.get("ThingID");
		JSONObject info = new JSONObject();
		if (getNode(nodeId) != null)
			info = getNode(nodeId).getThingInfo(thingId, info);
		
		// address target
		System.out.println ("[EventBus] ShowThingInfo: " + info);
		
		JSONArray targets = new JSONArray();
		targets.add("UI");
		sendEvent(info, targets, "ThingMonitor");
	}

	/* from RuleManager
	 * RuleManager로부터 낼온 명령을 SA Node로 전달한다.
	 */
	public void doCommand(JSONObject JSONMsg) {
		String nodeId = (String)JSONMsg.get("NodeID");
		String thingId = (String)JSONMsg.get("ThingID");
		getNode(nodeId).getThingCommand(thingId, JSONMsg);
		
		JSONMsg.remove("Targets");
		JSONMsg.remove("Job");
		
		// To Do Send to SA Node
		
	}
	
	/* from SA Node
	 * SA Node로부터 올라온 Data를 Update한다.
	*/
	public void updateThingInfo(JSONObject JSONMsg) { 
		Node node = null;
		Thing thing = null;
		JSONObject thingObj = null;
		
		node = getNode((String)JSONMsg.get("MacAddress"));
		if (node == null) {
			System.out.println ("[UpdateThingInfo] Error: cannot find Node...ignore it : " + JSONMsg);
			return;
		}
		
		JSONArray thingInfos = (JSONArray) JSONMsg.get("ThingsInfo");
		for (int i=0; i < thingInfos.size(); i++)
		{
			thingObj = (JSONObject) thingInfos.get(i);
			String thingID = (String)thingObj.get("ThingID");
			thing = node.getThing(thingID); 
			if (thing != null)
				if (thing.setValue((String)thingObj.get("Value")) == false) {
					// 값의 변경이 없음. 해당 Object 삭제
					thingInfos.remove(i);
				}
			else
				System.out.println ("[UpdateThingInfo] Error: cannot find Thing...ignore it : " + JSONMsg);
		}
		
		// 바뀐 data만 정리해서 보냄
		JSONArray targets = new JSONArray();
		targets.add("RuleManager");
		targets.add("UI");
		sendEvent(JSONMsg, targets, "ThingCtrl");
	}
	
	/* sendEvent
	 * JSONMsg : 전달할 Object
	 * target : 받을 대상
	 * job : 할 일
	*/
	public void sendEvent(JSONObject JSONMsg, JSONArray target, String job) {
		JSONMsg.remove("Targets");
		JSONMsg.remove("Job");
		
		JSONMsg.put("Targets", target);
		JSONMsg.put("Job", job);
		
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}

	
	
	public void discoverNode(int duration)
	{
		adapter.discoverNode(duration);
	}

	public void registerNode(String msg)
	{
		adapter.registerNode(msg);
	}

	public void rejectNode(String mac)
	{
		adapter.disconnectNode(mac);
	}
	
	@Override
	public void onDiscovered(AdapterEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.getMessage());
	}

	@Override
	public void onRegistered(AdapterEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.getMessage());
	}

	@Override
	public void onAddNode(AdapterEvent event) {
		// TODO Auto-generated method stub
		if(event.getType().equals("Add_node"))
		{
			// 1. look-up DB
			
			// 2. add new node or update the existing node
			/*
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
			*/
		}
	}

	public void send(String string, String msg) {
		// TODO Auto-generated method stub
		
	}

	public void disconnect(String string) {
		// TODO Auto-generated method stub
		
	}
}
