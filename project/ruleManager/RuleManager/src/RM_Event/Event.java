package RM_Event;

import org.json.simple.JSONObject;

import RM_Core.RuleSet;

public class Event extends Thread {

	protected JSONObject 	JSONMsg;
	protected RuleSet 		ruleset;
	
	public Event(JSONObject msg)
	{
		JSONMsg = msg;
		ruleset = RuleSet.getInstance();
	}
	
	public void run()
	{
		System.out.println("[Process] Handle event : " + JSONMsg);
	}
}
