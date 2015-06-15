package edu.cmu.team2.iotms.model.ruleManager.RM_Event;

import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.ruleManager.RM_Core.RuleSet;

public class NodeEvent{
	
	private static NodeEvent nodeEvent = new NodeEvent();
	
	private NodeEvent() { 
	}

	public static NodeEvent getInstance ()
	{
		return nodeEvent;
	}
	
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("[RM - Process] Handle NodeEvent : " + JSONMsg);
		
		String nodeID = (String) JSONMsg.get("ID");
		String value = (String) JSONMsg.get("Value");
		if (value.equalsIgnoreCase("Conn"))
			RuleSet.getInstance().activeRules(nodeID);
		else if (value.equalsIgnoreCase("DisConn"))
			RuleSet.getInstance().deactivateRules(nodeID);;
	}
}
