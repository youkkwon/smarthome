package edu.cmu.team2.iotms.model.nodeManager;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import Database.NodeDao;
import edu.cmu.team2.iotms.model.commManager.Adapter;
import edu.cmu.team2.iotms.model.commManager.AdapterEvent;
import edu.cmu.team2.iotms.model.commManager.AdapterEventListener;
import edu.cmu.team2.iotms.model.commManager.Link;
import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;

/*
enum Type {
	Door, Light, Presense, Temperature, Humidity, DoorSensor, AlarmLamp, MialBox, Unknown
}
package edu.cmu.team2.iotms.model.commManager;
enum SensorType {
	Actuator, Sensor, Unknown
}
*/

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
	
	@SuppressWarnings("unused")
	private void removeNode(Node node) {
		Nodes.remove(node);
	}
	
	// from SA Node
	public void addNode(Link link, JSONObject JSONMsg) {
		Node node = new Node(link, JSONMsg);
		Nodes.add(node);
	}
	
	// from UI
	@SuppressWarnings("unchecked")
	public void showNodeInfo(JSONObject JSONMsg) {
		String nodeId = (String)JSONMsg.get("NodeID");
		
		JSONObject info = new JSONObject();
		JSONArray thingArray = new JSONArray();
		
		Node node = getNode(nodeId);
		if (node == null) {
			System.out.println ("[NM - Process] [showNodeInfo] Error: Node is null");
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
	@SuppressWarnings("unchecked")
	public void showThingInfo(JSONObject JSONMsg) {
		String nodeId = (String)JSONMsg.get("NodeID");
		String thingId = (String)JSONMsg.get("ThingID");
		JSONObject info = new JSONObject();
		
		Node node = getNode(nodeId);
		if (node == null) {
			System.out.println ("[NM - Process] [showThingInfo] Error: Node is null");
			return;
		}
		info = node.getThingInfo(thingId, info);
		
		// address target
		System.out.println ("[NM - Process] [EventBus] ShowThingInfo: " + info);
		
		JSONArray targets = new JSONArray();
		targets.add("RuleManager");					
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
			System.out.println ("[NM - Process] [doCommand] Error: Node is null");
			return;
		}
		
		node.doThingCommand(thingId, JSONMsg);	
	}
	
	
	/* sendEvent
	 * JSONMsg : 전달할 Object
	 * target : 받을 대상
	 * job : 할 일
	*/
	@SuppressWarnings("unchecked")
	public void sendEvent(JSONObject JSONMsg, JSONArray target, String job) {
		JSONMsg.remove("Targets");
		JSONMsg.remove("Job");
		
		JSONMsg.put("Targets", target);
		JSONMsg.put("Job", job);
		
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}

	
	// Communicate with SA node
	public void discoverNode(JSONObject JSONMsg)
	{
		int duration = 0;
		String strDuration = (String)JSONMsg.get("Duration");
		if (strDuration != null)
			duration = Integer.parseInt(strDuration);
		adapter.discoverNode(duration);
	}

	public void registerNode(JSONObject JSONMsg)
	{
		adapter.registerNode(JSONMsg.toString());
	}

	public void rejectNode(String mac)
	{
		// 등록이 안된 Device가 추가되거나 Node의 갯수가 50개를 초과한 경우
		adapter.disconnectNode(mac);
	}
	
	public void removeNode(JSONObject JSONMsg)
	{
		String nodeId = (String)JSONMsg.get("NodeID");
		Node node = getNode(nodeId);
		if(node != null)
			node.disconnect();
		
		JSONMsg.remove("Targets");
		JSONMsg.remove("Job");
		node.send(JSONMsg.toString());
		node.disconnect();
		
		removeNode(node);
	}

	@Override
	public void onDiscovered(AdapterEvent event) {
		// TODO Auto-generated method stub
		System.out.println("[NM - Process] " + event.getMessage());
		JSONObject JSONMsg = null;
		JSONMsg = (JSONObject) JSONValue.parse(event.getMessage());
		if (JSONMsg == null) {
			System.out.println("[NM - Process] JSON Object is null from SA node");
			return;
		}
		JSONArray targets = new JSONArray();
		targets.add("UI");
		sendEvent(JSONMsg, targets, "Discovered");
	}

	@Override
	public void onRegistered(AdapterEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.getMessage());
		
		// Store DB or UI에 던진다.	
		JSONObject JSONMsg = null;
		JSONMsg = (JSONObject) JSONValue.parse(event.getMessage());
		if (JSONMsg == null) {
			System.out.println("[NM - Process] JSON Object is null from SA node");
			return;
		}
		
		JSONArray targets = new JSONArray();
		targets.add("UI");
		sendEvent(JSONMsg, targets, "Registered");
	}

	@Override
	public void onAddNode(AdapterEvent event) {
		// TODO Auto-generated method stub
		if(event.getType().equals("Add_node"))
		{
			// 1. look-up DB
			// Thing을 DB에서 받아서 Thing을 추가한다.
			if (Nodes.size() > 50) {
				System.out.println("Node is more 50. reject to add new node!!!");
				rejectNode("reject node");
			}
			
			// DB에서 읽은 놈을 JSONObj로 생성한다.
			JSONObject nodeObj = null;
			nodeObj = (JSONObject) JSONValue.parse(event.getMessage());
			if (nodeObj == null) {
				System.out.println("[NM - Process] JSON Object is null from SA node");
				rejectNode("reject node");
				return;
			}
			String nodeID = (String)nodeObj.get("NodeID");
			System.out.println("[NM - Process] Create NodeID: " + nodeID);
			
			String db = NodeDao.getInstance().getJsonOfNode(nodeID);
			if (db == null) {
				System.out.println("DB is null. reject to add new node!!!");
				rejectNode("reject node");
			}
			
			//String db = "{\"Job\":\"Registered\",\"NodeID\":\"12:23:34:45:56:67\",\"ThingList\":[{\"Id\":\"0001\",\"Type\":\"Door\",\"SType\":\"Actuator\",\"VType\":\"String\"\"VMin\":\"Open\"\"VMax\":\"Close\"},{\"Id\":\"0002\",\"Type\":\"Light\",\"SType\":\"Actuator\",\"VType\":\"String\"\"VMin\":\"On\"\"VMax\":\"Off\"},{\"Id\":\"0003\",\"Type\":\"Presence\",\"SType\":\"Sensor\",\"VType\":\"String\"\"VMin\":\"AtHome\"\"VMax\":\"Away\"},{\"Id\":\"0004\",\"Type\":\"Temperature\",\"SType\":\"Sensor\",\"VType\":\"Number\"\"VMin\":\"-50\"\"VMax\":\"50\"},{\"Id\":\"0005\",\"Type\":\"Humidity\",\"SType\":\"Sensor\",\"VType\":\"Number\"\"VMin\":\"0\"\"VMax\":\"100\"},{\"Id\":\"0006\",\"Type\":\"DoorSensor\",\"SType\":\"Sensor\",\"VType\":\"String\"\"VMin\":\"Open\"\"VMax\":\"Close\"},{\"Id\":\"0007\",\"Type\":\"MailBox\",\"SType\":\"Sensor\",\"VType\":\"String\"\"VMin\":\"Empty\"\"VMax\":\"Mail\"},{\"Id\":\"0008\",\"Type\":\"Alarm\",\"SType\":\"Actuator\",\"VType\":\"String\"\"VMin\":\"Set\"\"VMax\":\"Unset\"}]}\n";
			JSONObject JSONMsg = (JSONObject) JSONValue.parse(db);
			
			if (JSONMsg == null) {
				System.out.println("DB is null. reject to add new node!!!");
				rejectNode("reject node");
			}
			
			// 2. add new node or update the existing node
			Node node = getNode(((Link)event.getLink()).getMACAddress());
			if(node == null)
			{
				System.out.println("[NM - Process] Add new node = " + ((Link)event.getLink()).getMACAddress());
				addNode((Link)(event.getLink()), JSONMsg);
			}
			else
			{
				System.out.println("[NM - Process] Existing node = " + ((Link)event.getLink()).getMACAddress());
				node.updateLink((Link)(event.getLink()));
			}
		}
	}

}
