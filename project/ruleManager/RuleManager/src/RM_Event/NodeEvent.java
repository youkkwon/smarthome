package RM_Event;

import org.json.simple.JSONObject;

public class NodeEvent extends Event {

	public NodeEvent(JSONObject msg) {
		super(msg);
	}

	public void run()
	{
		System.out.println("[Process] Handle NodeEvent : " + JSONMsg);
		
		String nodeID = (String) JSONMsg.get("ID");
		String value = (String) JSONMsg.get("Value");
		if (value.equalsIgnoreCase("Conn"))
			ruleset.activeRules(nodeID);
		else if (value.equalsIgnoreCase("DisConn"))
			ruleset.deactivateRules(nodeID);;
	}
}
