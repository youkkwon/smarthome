package RM_Event;

import org.json.simple.JSONObject;

import RM_Core.Action;
import RM_Core.InstantAction;
import RM_Exception.InvalidRuleException;

public class ActionEvent extends Event {

	public ActionEvent(JSONObject msg) { 
		super(msg);
	}

	public void run()
	{
		System.out.println("[Process] Handle ActionEvent : " + JSONMsg);
		
		boolean allow = false;
		String nodeID = (String) JSONMsg.get("NodeID");
		String thingID = (String) JSONMsg.get("ThingID");
		String type = (String) JSONMsg.get("Type");
		String value = (String) JSONMsg.get("Value");
		
		Action action = new InstantAction (nodeID, thingID, value, type);
		try {
			allow = ruleset.isAllow(action);
		} catch (InvalidRuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (allow)
			action.execute();
		else
			System.out.println ("Action is not allowed : " + JSONMsg);
	}
}
