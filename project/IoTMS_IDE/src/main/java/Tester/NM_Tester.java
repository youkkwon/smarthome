package Tester;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.commManager.CommUtil;
import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;

public class NM_Tester {
	
	private String nodeID;
	
	public NM_Tester (String nodeID)
	{
		this.nodeID = nodeID;
	}
	
	/*
	 * Thing ID				Action
	 * Door			1		Open/Close
	 * Light		2		On/Off
	 * Presence		3		AtHome/Away
	 * Temperature	4		number
	 * Humidity		5		number
	 * DoorSensor	6		Open/Close
	 * AlarmLamp	7		On/Off
	 * MailBox		8		Empty/Mail
	 * 
	 * Alarm		10		Set/UnSet
	 * Message		11 		Confirm/Emergency/MalFunction/Post
	 * 
	 * Main Arduino			
	 * Mailbox Arduino		78:C4:E:2:5C:A3
	 */
	
	@SuppressWarnings("unchecked")
	public void testUINodeEvent(String nodeID)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("NodeManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "NodeMonitor");
		JSONMsg.put("NodeID", nodeID);

		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	@SuppressWarnings("unchecked")
	public void testUIDiscoverEvent()
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("NodeManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "Discover");
		JSONMsg.put("Duration", "20");
				
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	@SuppressWarnings("unchecked")
	public void testUIRegisterEvent()
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("NodeManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "Register");
		JSONMsg.put("NodeID", "78:c4:e:1:7f:f9");
		JSONMsg.put("URL", "192.168.1.143");
		JSONMsg.put("Port", (new Integer(CommUtil.getServerPort())).toString());
		JSONMsg.put("SerialNumber", "12345678");
		
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	public void test() 
	{
		//testUINodeEvent(nodeID);
			
		testUIDiscoverEvent();
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testUIRegisterEvent();
	}
}
