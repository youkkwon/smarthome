package edu.cmu.team2.iotms.model.ruleManager.RM_Event;

import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Config;

public class ConfigEvent {

	private static ConfigEvent thingEvent = new ConfigEvent();
	
	private ConfigEvent() { 
	}

	public static ConfigEvent getInstance ()
	{
		return thingEvent;
	}
	
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("[RM - Process] Handle ConfigEvent : " + JSONMsg);
		
		String type = (String) JSONMsg.get("Type");
		String value = (String) JSONMsg.get("Value");
		
		Config.getInstance().setConfig(type, Integer.parseInt(value));
	}
}
