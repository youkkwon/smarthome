package RM_Event;

import org.json.simple.JSONObject;

import RM_Core.RuleSet;
import RM_Exception.InvalidRuleException;

public class StateEvent {

	private static StateEvent stateEvent = new StateEvent();
	
	private StateEvent() { 
	}

	public static StateEvent getInstance ()
	{
		return stateEvent;
	}
	
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("[Process] Handle StateEvent : " + JSONMsg);
		
		String value = (String) JSONMsg.get("Value");
		try {
			RuleSet.getInstance().setMode(value);
		} catch (InvalidRuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
