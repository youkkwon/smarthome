package edu.cmu.team2.iotms.model.ruleManager.RM_Event;

import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.ruleManager.RM_Core.RuleSet;
import edu.cmu.team2.iotms.model.utility.InvalidRuleException;

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
		System.out.println("[RM - Process] Handle StateEvent : " + JSONMsg);
		
		String value = (String) JSONMsg.get("Value");
		try {
			RuleSet.getInstance().setMode(value);
		} catch (InvalidRuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
