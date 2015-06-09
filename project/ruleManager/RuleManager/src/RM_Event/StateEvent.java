package RM_Event;

import org.json.simple.JSONObject;

import RM_Exception.InvalidRuleException;

public class StateEvent extends Event {

	public StateEvent(JSONObject msg) {
		super(msg);
	}	
	
	public void run()
	{
		System.out.println("[Process] Handle StateEvent : " + JSONMsg);
		
		String value = (String) JSONMsg.get("Value");
		String mode = "Alarm" + "==" + value;			// Alarm==Set
		try {
			ruleset.setMode(mode);
		} catch (InvalidRuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
