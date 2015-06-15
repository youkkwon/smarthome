package RuleManager.RM_Event;

import org.json.simple.JSONObject;

import RuleManager.RM_Core.Action;
import RuleManager.RM_Core.InstantAction;
import RuleManager.RM_Core.RuleSet;
import Utility.InvalidRuleException;

public class ActionEvent {

	private RuleSet 	ruleset;
	private static ActionEvent actEvent = new ActionEvent();
	
	private ActionEvent() { 
		ruleset = RuleSet.getInstance();
	}

	public static ActionEvent getInstance ()
	{
		return actEvent;
	}
	
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("[RM - Process] Handle ActionEvent : " + JSONMsg);
		
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
			System.out.println ("[RM - Process] Action is not allowed : " + JSONMsg);
	}
}
