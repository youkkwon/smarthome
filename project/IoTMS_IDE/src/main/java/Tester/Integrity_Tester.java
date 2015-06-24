package Tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.commManager.CommUtil;
import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;

public class Integrity_Tester {
	
	private String nodeID;
	
	public Integrity_Tester(String nodeID)
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
	
		
	public void testAction(String nodeID) throws InterruptedException
	{
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
	public void testRegistNode(String NodeID, String serialNumber)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("NodeManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "Register");
		JSONMsg.put("NodeID", NodeID);
		JSONMsg.put("URL", "192.168.1.143");
		JSONMsg.put("Port", (new Integer(CommUtil.getServerPort())).toString());
		JSONMsg.put("SerialNumber", serialNumber);
		
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
	
	public void test() throws InterruptedException 
	{
		String input = null;

	    // Defines the standard input stream
	    BufferedReader stdin = new BufferedReader (new InputStreamReader(System.in));

		System.out.println ("Initialize 	: Discover Node, Register First, Register Second");
		System.out.println ("Actin command 	: Open Door, Close Door, Turn on Lamp, Turn off Lamp, Set Alarm, UnSet Alarm");
		System.out.println ("Quit     		: Quit, Bye");
		
		while (true)
		{	
			System.out.print ("Enter Input "); 

		    try {
				input = stdin.readLine();
				switch (input)
				{
				case "Discover Node" :
					testUIDiscoverEvent();
					break;
				case "Register First" : 
					testRegistNode("78:c4:e:1:7f:f9", "12345678");
					break;
				case "Register Second" :
					testRegistNode("78:c4:e:2:5c:a3", "12345679");
					break;
				case "Open Door" :
					testActionEvent (nodeID, "0001", "Door", "Open");		
					break;
				case "Close Door" :
					testActionEvent (nodeID, "0001", "Door", "Close");		
					break;
				case "Turn on Lamp" :
					testActionEvent (nodeID, "0002", "Light", "On");		
					break;
				case "Turn off Lamp" :
					testActionEvent (nodeID, "0002", "Light", "Off");		
					break;
				case "Set Alarm" : 		// Set Alarm Mode
					testStateEvent ("Setting");	
					break;
				case "UnSet Alarm" : 		// Set Alarm Mode
					testStateEvent ("UnSet");	
					break;
				case "Pong" :
					JSONObject 	JSONMsg = new JSONObject();
					JSONArray 	targets = new JSONArray();
					
					targets.add("RuleManager");
					JSONMsg.put("Targets", targets);
					JSONMsg.put("Job", "Pong");
					JSONMsg.put("NodeID",  nodeID);
					JSONMsg.put("Value", Integer.toString(number++));	
										
					// post event.
					IoTMSEventBus.getInstance().postEvent(JSONMsg);
				case "Quit" :
				case "Bye" :
					return;
				}
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
}
