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
	public void testThingBulkEvent(String nodeID, String[] thingID, String[] type, String[] value)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		JSONArray	things	= new JSONArray();
		
		targets.add("NodeManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "ThingCtrl");
		JSONMsg.put("NodeID", nodeID);		
		if (thingID.length == type.length && thingID.length == value.length)
		{
			for (int i=0; i < thingID.length; i++)
			{
				JSONObject thing = new JSONObject();
				thing.put ("ThingID", thingID[i]);
				thing.put ("Type", type[i]);
				thing.put ("Value", value[i]);
				things.add(thing);
			}
		}
		JSONMsg.put("ThingsInfo", things);

		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	
	@SuppressWarnings("unchecked")
	public void testUINodeEvent1(String nodeID)
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
		/*String[] IDs = new String[4];
		String[] types = new String[4];
		String[] values = new String[4];
		
		IDs[0] = "3";	types[0] = "Presence";		values[0] = "AtHome";		
		IDs[1] = "4";	types[1] = "Temperature";	values[1] = "32";
		IDs[2] = "5";	types[2] = "Humidity";		values[2] = "53";
		IDs[3] = "5";	types[3] = "DoorSensor";	values[3] = "Open";

		testThingBulkEvent("0", IDs, types, values);
		
		values[0] = "AtHome";		
		values[1] = "38";
		values[2] = "12";
		values[3] = "Close";
		testThingBulkEvent("0", IDs, types, values);
		
		values[0] = "Away";		
		values[1] = "38";
		values[2] = "12";
		values[3] = "Open";
		testThingBulkEvent("0", IDs, types, values);
		
		testUINodeEvent(nodeID);
		testUIThingEvent(nodeID, "1");
		*/
		
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
