package Tester;

import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;
import edu.cmu.team2.iotms.model.ruleManager.RM_Storage.RuleManagerDBStorage;

public class RM_Tester {
	
	private String nodeID;
	
	public RM_Tester(String nodeID)
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
	public void testRuleEvent (String type, String nodeID, String rule)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		
		JSONMsg.put("Job", "RuleCtrl");
		JSONMsg.put("Type", type);				
		if (type.equalsIgnoreCase("SearchNode"))
		{
			JSONMsg.put("Value", nodeID);
		}
		else if (type.equalsIgnoreCase("Search"))
		{
		}
		else if (type.equalsIgnoreCase("Add") || type.equalsIgnoreCase("delete"))
		{
			JSONMsg.put("Rule", rule);
		}
		else 
		{
			System.out.println ("Wrong type.");
			return;
		}
		
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	@SuppressWarnings("unchecked")
	public void testNodeEvent(String nodeID, String alive)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("ID",  nodeID);
		JSONMsg.put("Job", "NodeCtrl");
		JSONMsg.put("Value", alive);

		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	@SuppressWarnings("unchecked")
	public void testStateEvent (String mode)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "Alarm");
		JSONMsg.put("Value", mode);

		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	@SuppressWarnings("unchecked")
	public void testActionEvent(String nodeID, String thingID, String type, String value)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "ActionCtrl");
		JSONMsg.put("NodeID", nodeID);
		JSONMsg.put("ThingID", thingID);
		JSONMsg.put("Type",  type);
		JSONMsg.put("Value", value);

		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	@SuppressWarnings("unchecked")
	public void testThingEvent(String nodeID, String thingID, String type, String value)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "ThingCtrl");
		JSONMsg.put("NodeID", nodeID);

		JSONArray	things	= new JSONArray();
		JSONObject thing = new JSONObject();
		thing.put ("Id", thingID);
		thing.put ("Type", type);
		thing.put ("Value", value);
		things.add(thing);
		JSONMsg.put("Status", things);
		
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	@SuppressWarnings("unchecked")
	public void testThingBulkEvent(String nodeID, String[] thingID, String[] type, String[] value)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		JSONArray	things	= new JSONArray();
		
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "ThingCtrl");
		JSONMsg.put("NodeID", nodeID);		
		if (thingID.length == type.length && thingID.length == value.length)
		{
			for (int i=0; i < thingID.length; i++)
			{
				JSONObject thing = new JSONObject();
				thing.put ("Id", thingID[i]);
				thing.put ("Type", type[i]);
				thing.put ("Value", value[i]);
				things.add(thing);
			}
		}
		JSONMsg.put("Status", things);

		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	@SuppressWarnings("unchecked")
	public void testConfigEvent(String type, String value)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "ConfigCtrl");
		JSONMsg.put("Type",  type);
		JSONMsg.put("Value", value);

		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	public void test() throws InterruptedException {
				
		///*
		/*String input = new String();
		ListIterator<String> raw_rules = RuleManagerDBStorage.getInstance().loadRuleSet();
		while (raw_rules.hasNext())
		{
			input = raw_rules.next();
			 
			testRuleEvent("Add", null, input);
		}			
		
		//testNodeEvent("1", "DisConn");
		//testNodeEvent("1", "Conn");
		testRuleEvent("Search", null, null);
		testRuleEvent("Delete", null, input);
		*/
		
	    testConfigEvent("Alarm", "0300");							// Test config 
		testConfigEvent("Light", "0600");
	
		// State Test (Alarm mode)
		testStateEvent ("Setting");		

		Thread.sleep(3000);
		// Thing Test (Node ID + Thing ID)
		testThingEvent (nodeID, "0006", "DoorSensor", "Close");	    // Test no rule event
			
		Thread.sleep(3000);
		// Thing Test (Node ID : + Thing ID)
		testActionEvent (nodeID, "0001", "Door", "Open");			// Door open on Alarm mode
		
		// State Test (Alarm mode)
		testStateEvent ("Setting");	
		
		Thread.sleep(12000);                                        // Alarm setting will fail.
        // Thing Test (Node ID + Thing ID)
		testActionEvent (nodeID, "0001", "Door", "Open");			// Door open on Normal mode
			
		// Thing Test (Node ID + Thing ID)
		testThingEvent (nodeID, "0002", "Light", "On");			    // Test no rule event
		
		testThingEvent (nodeID, "0003", "Presence", "Away");		// Test schedule event
		testStateEvent ("UnSet");								    // Test cancel scheduled event

		testConfigEvent("Alarm", "0003");							// Test config 
		testConfigEvent("Light", "0009");
	
		testThingEvent (nodeID, "0003", "Presence", "Away");		// Check schedule job on changed value	
		
        String[] IDs = new String[3];
		String[] types = new String[3];
		String[] values = new String[3];
		IDs[0] = "0005";	types[0] = "DoorSensor";		values[0] = "Open";
		IDs[1] = "0003";	types[1] = "Presence";			values[1] = "Away";
		IDs[2] = "0004";	types[2] = "Temperature";		values[2] = "32.00";		
		testThingBulkEvent(nodeID, IDs, types, values);
		
		testThingEvent (nodeID, "0003", "Temperature", "32.00");		// Check schedule job on changed value	
		//*/
		
		/*while (true)
		{
		    // Thing Test (Node ID + Thing ID)
			testActionEvent (nodeID, "0001", "Door", "Open");			// Door open on Normal mode
			Thread.sleep(3000);
		
			// Thing Test (Node ID + Thing ID)
			testActionEvent (nodeID, "0002", "Light", "On");			// Test no rule event
			Thread.sleep(3000);
		
			// Thing Test (Node ID + Thing ID)
			testActionEvent (nodeID, "0001", "Door", "Close");			// Door open on Normal mode
			Thread.sleep(3000);
		
			// Thing Test (Node ID + Thing ID)
			testActionEvent (nodeID, "0002", "Light", "Off");			// Test no rule event
			Thread.sleep(3000);
						
			// Thing Test (Node ID + Thing ID)
			testActionEvent (nodeID, "0007", "AlarmLamp", "On");			// Test no rule event
			Thread.sleep(3000);
			
			// Thing Test (Node ID + Thing ID)
			testActionEvent (nodeID, "0007", "AlarmLamp", "Off");			// Test no rule event
			Thread.sleep(3000);
			
			// State Test (Alarm mode)
			//testStateEvent ("Setting");	
			//Thread.sleep(15000);
		}*/	
	}
}
