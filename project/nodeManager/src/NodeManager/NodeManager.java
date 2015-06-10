package NodeManager;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.*;

public class NodeManager {
	// Singletone
	private static NodeManager uniqueInstance = new NodeManager();
	public static NodeManager getInstance() {
		return uniqueInstance;
	}
	
	NM_EventBusReceiver nm_rcv = NM_EventBusReceiver.getInstance();
	IoTMSEventBus ev_bus = IoTMSEventBus.getInstance();
	private List<Node> Nodes;
	
	// This method is called when command by UI via Event-Bus
	public void AddNode(int nodeId, String ipAddr, String hostName, String things) {
		Node node = new Node(nodeId, ipAddr, hostName, things);
		Nodes.add(node);
	}
	
	public void RemoveNode(int id) {
		Nodes.remove(id);
	}
	
	public Node GetNode(int id) {
		if (id <= Nodes.size() && id >= 0)
			return Nodes.get(id);
		return null;
	}
	
	public void FindNode() {
		// Invoke Event for discoverNode
	}
	
	public void ShowNodesInfo() { 
		String info = "{";
		for(int i=0; i<Nodes.size(); i++) {
			info += Nodes.get(i).getNodeID();
			info += ":";
			info += Nodes.get(i).getHostName();
			info += ":";
			info += Nodes.get(i).getIpAddress();
		}
		info = "}";
		//EventBus.push(info);
	}
	
	public void ShowThingsInfo(int id) { 
		String info = Nodes.get(id).ShowInfo();
		//EventBus.push(info);
	}
	
	public void ShowThingInfo(JSONObject JSONMsg) {
		int nodeId = Integer.parseInt((String)JSONMsg.get("NodeID"));
		int thingId = Integer.parseInt((String)JSONMsg.get("ThingID"));
		JSONObject info = new JSONObject(); 
		info = Nodes.get(nodeId).getThingInfo(thingId, info);
		
		// address target
		JSONArray targets = new JSONArray();
		targets.add("UI");
		info.put("Targets", targets);
		
		System.out.println ("[EventBus] ShowThingInfo: " + info);
		
		ev_bus.postEvent(info);
	}
}
