package Tester;

import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.IoTMSEventBus;
import RuleManager.RM_Storage.RuleSetFileStorage;

public class RM_Tester {
	
	/*
	 * Thing ID				Action
	 * Door			1		Open/Close
	 * Light		2		On/Off
	 * Presense		3		AtHome/Away
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
		thing.put ("ThingID", thingID);
		thing.put ("Type", type);
		thing.put ("Value", value);
		things.add(thing);
		JSONMsg.put("ThingsInfo", things);
		
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
				
		/*
		String input = new String();
		ListIterator<String> raw_rules = RuleSetFileStorage.getInstance().loadRuleSet();
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
		testRuleEvent("Search", null, null);
		
		// State Test (Alarm mode)
		testStateEvent ("Set");		
		testRuleEvent("Search", null, null);
	
		// Thing Test (Node ID : + Thing ID)
		testActionEvent ("0", "1", "Door", "Open");			// Door open on Alarm mode
		
		// State Test (Normal mode)
		testStateEvent ("UnSet");	
			
		// Thing Test (Node ID + Thing ID)
		testActionEvent ("0", "1", "Door", "Open");			// Door open on Normal mode
			
		// Thing Test (Node ID + Thing ID)
		testThingEvent ("0", "2", "Light", "On");			// Test no rule event
		
		testThingEvent ("0", "3", "Presence", "Away");		// Test schedule event
		testStateEvent ("UnSet");							// Test cancel scheduled event

		testConfigEvent("Alarm", "3");						// Test config 
		testConfigEvent("Light", "9");
		//testRuleEvent ("Search", null, null);
	
		testThingEvent ("0", "3", "Presence", "Away");		// Check schedule job on changed value		
		
		String[] IDs = new String[3];
		String[] types = new String[3];
		String[] values = new String[3];
		IDs[0] = "5";	types[0] = "DoorSensor";		values[0] = "Open";
		IDs[1] = "3";	types[1] = "Presense";			values[1] = "Away";
		IDs[2] = "4";	types[2] = "Temperature";		values[2] = "32";		
		testThingBulkEvent("0", IDs, types, values);
	}
}
