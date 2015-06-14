package NodeManager;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.*;
import comm.Adapter;
import comm.AdapterEvent;
import comm.AdapterEventListener;
import comm.Link;

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
	
	NM_EventBusReceiver nm_rcv = NM_EventBusReceiver.getInstance();
	IoTMSEventBus ev_bus = IoTMSEventBus.getInstance();
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
		
		Node node = getNode(nodeId);
		if (node == null) {
			System.out.println ("[showNodeInfo] Error: Node is null");
			return;
		}
		
		for(int i=0; i<node.getThingCount(); i++) {
			JSONObject thingInfo = new JSONObject();
			thingInfo = node.getThingInfo(i, info);
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
		
		Node node = getNode(nodeId);
		if (node == null) {
			System.out.println ("[showThingInfo] Error: Node is null");
			return;
		}
		info = node.getThingInfo(thingId, info);
		
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
		Node node = getNode(nodeId); 
		if (node == null) {
			System.out.println ("[doCommand] Error: Node is null");
			return;
		}
		
		node.doThingCommand(thingId, JSONMsg);	
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
		
		ev_bus.postEvent(JSONMsg);
	}

	
	// Communicate with SA node
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
		// 등록이 안된 Device가 추가되거나 Node의 갯수가 50개를 초과한 경우
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
		// Store DB
		
	}

	@Override
	public void onAddNode(AdapterEvent event) {
		// TODO Auto-generated method stub
		if(event.getType().equals("Add_node"))
		{
			// 1. look-up DB
			// Thing을 DB에서 받아서 Thing을 추가한다.
			if (Nodes.size() > 50)
				rejectNode("reject node");
			
			
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
}
