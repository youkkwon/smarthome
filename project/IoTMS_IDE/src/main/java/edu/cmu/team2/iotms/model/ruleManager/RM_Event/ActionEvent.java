package edu.cmu.team2.iotms.model.ruleManager.RM_Event;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Action;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.InstantAction;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.RuleSet;
import edu.cmu.team2.iotms.model.utility.InvalidRuleException;

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
	
	@SuppressWarnings("unchecked")
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
		{
			System.out.println ("[RM - Process] Action is not allowed : " + JSONMsg);
			
			JSONArray 	targets 	= new JSONArray();
			
			JSONMsg.remove("Targets");
			targets.add("UI");
			JSONMsg.put("Targets", targets);
			JSONMsg.put("Job", "ActionCtrl");
			JSONMsg.put("NodeID",  nodeID);
			JSONMsg.put("ThingID", thingID);
			JSONMsg.put("Type",  type);
			JSONMsg.put("Value", "Rejected by RuleManager");			
		}
	}
}
