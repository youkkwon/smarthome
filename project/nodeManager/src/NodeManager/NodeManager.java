package NodeManager;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.*;

enum Type {
	Door, Light, Presense, Temperature, Humidity, DoorSensor, AlarmLamp, MialBox, Unknown
}

enum SensorType {
	Actuator, Sensor, Unknown
}

public class NodeManager {
	// Singletone
	private static NodeManager uniqueInstance = new NodeManager();
	public static NodeManager getInstance() {
		return uniqueInstance;
	}
	
	NM_EventBusReceiver nm_rcv = NM_EventBusReceiver.getInstance();
	IoTMSEventBus ev_bus = IoTMSEventBus.getInstance();
	private List<Node> Nodes;
		
	private Node getNode(String macAddr) {
		int index = 0;
		for(index=0; index<Nodes.size(); index++) {
			if (macAddr.equals(Nodes.get(index)))
					return Nodes.get(index);
		}
		return null;
	}
	
	private void removeNode(int id) {
		Nodes.remove(id);
	}
	
	// from UI
	public void addNode(String macAddr, String ipAddr, String hostName, JSONObject JSONMsg) {
		Node node = new Node(macAddr, ipAddr, hostName, JSONMsg);
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
		
		// Need to loop
		node = getNode((String)JSONMsg.get("MacAddress"));
		if (node != null)
			thing = node.getThing((String)JSONMsg.get("ThingName"));
		else
			System.out.println ("[UpdateThingInfo] Error: cannot find Node...ignore it : " + JSONMsg);
		if (thing != null)
			if (thing.setValue((String)JSONMsg.get("NodeID")) == false) {
				// 값의 변경이 없음. 삭제
				
			}
		else
			System.out.println ("[UpdateThingInfo] Error: cannot find Thing...ignore it : " + JSONMsg);
		
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
		
		ev_bus.postEvent(JSONMsg);
	}
}
