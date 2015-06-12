package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.IoTMSEventBus;
import RM_Exception.InvalidRuleException;

public class RM_Tester {
	
	/*
	 * Thing ID				Action
	 * Door			1		Open/Close
	 * Light		2		On/Off
	 * Presense		3		AtHome/Away
	 * Temperature	4		number
	 * DoorSensor	5		Open/Close
	 * MailBox		6		Empty/Mail
	 * AlarmLamp	7		On/Off
	 * Alarm		8		Set/UnSet
	 * Message		9 		Confirm/Emergency/MalFunction/Post
	 */
	@SuppressWarnings("unchecked")
	public void testRuleEvent(String type, String nodeID, String[] conditions, String[] actions) 
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		
		JSONMsg.put("Job", "RuleCtrl");
		JSONMsg.put("Type", type);
		if (type.equalsIgnoreCase("Add") || type.equalsIgnoreCase("delete"))
		{
			JSONArray	condArray = new JSONArray();
			for (int i=0; i < conditions.length; i++)
				condArray.add(conditions[i]);
			JSONMsg.put("Conditions", condArray);
			
			JSONArray	actArray = new JSONArray();
			for (int i=0; i < actions.length; i++)
				actArray.add(actions[i]);
	
			JSONMsg.put("Actions",  actArray);
		}
		else if (type.equalsIgnoreCase("SearchNode"))
		{
			JSONMsg.put("Value", nodeID);
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
		JSONMsg.put("ThingID", thingID);
		JSONMsg.put("Type",  type);
		JSONMsg.put("Value", value);

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
	
	public void test () throws InterruptedException {
				
		System.out.println ("[TestModule] Create Rule.");
		// Alarm mode Set 시, AlarmLamp On 및 Door Open 불허
		String[] conditions = new String[1];
		String[] actions = new String[2];
		conditions[0] = "*@8==Set#Alarm";
		actions[0] = "!0@1=Open#Door";
		actions[1] = "0@7=On#AlarmLamp";
		testRuleEvent("Add", null, conditions, actions);
		
		// Alarm mode UnSet 시, AlarmLamp Off
		conditions = new String[1];
		actions = new String[1];
		conditions[0] = "*@8==UnSet#Alarm";
		actions[0] = "0@7=Off#AlarmLamp";	
		testRuleEvent("Add", null, conditions, actions);

		// If Away, close door & set alarm in 5min, lamp off in 10 min
		conditions = new String[1];
		actions = new String[4];
		conditions[0] = "0@3==Away#Presense";
		actions[0] = "0@1=CloseIn30#Door";
		actions[1] = "0@9=Confirm#Message";
		actions[2] = "*@8=SetIn30#Alarm";
		actions[3] = "0@2=OffIn60#Light";
		testRuleEvent("Add", null, conditions, actions);
		
		// If AtHome while Alarm, Send Emergency Msg
		conditions = new String[2];
		actions = new String[1];
		conditions[0] = "*@8==Set#Alarm";
		conditions[1] = "0@3==AtHome#Presense";
		actions[0] = "0@9=Emergency#Message";
		testRuleEvent("Add", null, conditions, actions);
		
		// If AtHome while Alarm, Send Emergency Msg
		conditions = new String[2];
		actions = new String[1];
		conditions[0] = "*@8==Set#Alarm";
		conditions[1] = "0@1==Open#Door";
		actions[0] = "0@9=Emergency#Message";
		testRuleEvent("Add", null, conditions, actions);
		
		/* TEST..
		// Close door if it open on node 0
		conditions = new String[1];
		actions = new String[1];
		conditions[0] = "0@5==Open#DoorSensor";
		actions[0] = "0@1=Close#Door";
		testRuleEvent("Add", null, conditions, actions);
		
		// Close door if it open on node 1
		conditions = new String[1];
		actions = new String[1];
		conditions[0] = "1@5==Open#DoorSensor";
		actions[0] = "1@1=Close#Door";
		testRuleEvent("Add", null, conditions, actions);
		
		// Close door if it open on node 0
		conditions = new String[1];
		actions = new String[1];
		conditions[0] = "0@5==Open#DoorSensor";
		actions[0] = "0@2=On#Light";
		testRuleEvent("Add", null, conditions, actions);
		
		conditions = new String[1];
		actions = new String[1];
		conditions[0] = "0@5==Open#DoorSensor";
		actions[0] = "0@1=Close#Door";
		testRuleEvent("Delete", null, conditions, actions);*/
	
		//testNodeEvent("1", "DisConn");
		//testNodeEvent("1", "Conn");
		testRuleEvent("Search", null, null, null);
		
		// State Test (Alarm mode)
		System.out.println ("[TestModule] Alarm mode set.");
		testStateEvent ("Set");		
		testRuleEvent("Search", null, null, null);
		
		// Thing Test (Node ID : + Thing ID)
		System.out.println ("[TestModule] open door.");
		testActionEvent ("0", "1", "Door", "Open");
		Thread.sleep(500);
		
		// State Test (Normal mode)
		System.out.println ("[TestModule] Alarm mode unset.");
		testStateEvent ("UnSet");	
			
		// Thing Test (Node ID + Thing ID)
		System.out.println ("[TestModule] open door.");
		testActionEvent ("0", "1", "Door", "Open");
			
		// Thing Test (Node ID + Thing ID)
		System.out.println ("[TestModule] Light on.");
		testThingEvent ("0", "2", "Light", "On");
		
		System.out.println ("[TestModule] Presense (Away)");
		testThingEvent ("0", "3", "Presense", "Away");
		
		System.out.println ("[TestModule] Alarm mode unset. (Cancel alarm mode expected)");
		testStateEvent ("UnSet");		

		testConfigEvent("Door", "3");
		testRuleEvent ("Search", null, null, null);
		
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		try {
			String msg = cin.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println ("Terminate test code");
	}

}
