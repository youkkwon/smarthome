package edu.cmu.team2.iotms.model.ruleManager.RM_Event;

import java.util.Iterator;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Action;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Condition;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.RuleSet;

public class ThingEvent {

	private static ThingEvent thingEvent = new ThingEvent();

	private ThingEvent() { 
	}

	public static ThingEvent getInstance ()
	{
		return thingEvent;
	}
	
	/*public void execute_single(JSONObject JSONMsg)
	{
		System.out.println("[RM - Process] Handle ThingEvent : " + JSONMsg);
		
		String nodeID = (String) JSONMsg.get("NodeID");
		String thingID = (String) JSONMsg.get("ThingID");
		String type = (String) JSONMsg.get("Type");
		String value = (String) JSONMsg.get("Value");
		
		Condition condition = new Condition (nodeID, thingID, value, type);
	
		LinkedList<Action> actions = RuleSet.getInstance().getActions(condition.getStatement());
		if (actions == null || actions.isEmpty())
		{
			System.out.println("[RM - Process] No rule for this condition.");
			return;
		}
		Iterator<Action> iterator = actions.iterator();
		while (iterator.hasNext())
			iterator.next().execute();		
	}*/
	
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("[RM - Process] Handle ThingEvent : " + JSONMsg);
		
		String nodeID = (String) JSONMsg.get("NodeID");
		JSONArray thingsInfo = (JSONArray) JSONMsg.get("Status");
		
		for (int i=0; i < thingsInfo.size(); i++)
		{
			JSONObject thing = (JSONObject) thingsInfo.get(i);
			String thingID = (String) thing.get("Id");
			String value = (String) thing.get("Value");
			String type = (String) thing.get("Type");
			
			Condition condition = new Condition (nodeID, thingID, value, type);
			
			execute_thing(condition);
		}
	}
	
	private void execute_thing(Condition condition)
	{
		LinkedList<Action> actions = RuleSet.getInstance().getActions(condition.getStatement());
		if (actions == null || actions.isEmpty())
		{
			System.out.println("[RM - Process] No rule for this condition." + condition.getStatement());
			return;
		}
		Iterator<Action> iterator = actions.iterator();
		while (iterator.hasNext())
		{
			Action action = iterator.next();
			if (condition.isMinMaxCond())
			{
				action.setDesc(condition.getType() + " is malfunction. value - " + condition.getValue());
			}
			action.execute();
		}
	}
}
