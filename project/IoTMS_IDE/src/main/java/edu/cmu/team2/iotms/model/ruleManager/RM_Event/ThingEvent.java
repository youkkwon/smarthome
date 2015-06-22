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
	
	public void execute_single(JSONObject JSONMsg)
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
	}
	
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("[RM - Process] Handle ThingEvent : " + JSONMsg);
		
		String job = (String) JSONMsg.get("Job");
		if (job.equalsIgnoreCase("ThingMonitor"))
		{
			// Handle actuator mal-function
			System.out.println ("JSONMsg: " + JSONMsg);
			execute_single(JSONMsg);
		}
		else
		{
			execute_things(JSONMsg);			// Ver 2 - handle multi condition
			//execute_thing(JSONMsg);			// Ver 1 - handle only one condition
		}
	}
	
	public void execute_things(JSONObject JSONMsg)
	{
		String nodeID = (String) JSONMsg.get("NodeID");
		JSONArray thingsInfo = (JSONArray) JSONMsg.get("Status");
		
		String[] conditions = new String[thingsInfo.size()];
		for (int i=0; i < thingsInfo.size(); i++)
		{
			JSONObject thing = (JSONObject) thingsInfo.get(i);
			String thingID = (String) thing.get("Id");
			String value = (String) thing.get("Value");
			String type = (String) thing.get("Type");
			
			Condition condition = new Condition (nodeID, thingID, value, type);			
			conditions[i] = condition.getStatement();
		}
		execute_complex_condition(conditions);
	}
	
	private void execute_complex_condition(String[] conditions)
	{
		LinkedList<Action> actions = RuleSet.getInstance().getActions(conditions);
		if (actions == null || actions.isEmpty())
		{
			System.out.println("[RM - Process] No rule for this condition.");
			return;
		}
		Iterator<Action> iterator = actions.iterator();
		while (iterator.hasNext())
		{
			Action action = iterator.next();
			/*
			if (condition.isMinMaxCond())
			{
				action.setDesc(condition.getType() + " is malfunction. value - " + condition.getValue());
			}
			*/
			System.out.println ("[RM - Process] actions : "+ action.getStatement());
			action.execute();
		}
	}
	
	public void execute_thing(JSONObject JSONMsg)
	{
		String nodeID = (String) JSONMsg.get("NodeID");
		JSONArray thingsInfo = (JSONArray) JSONMsg.get("Status");
		
		for (int i=0; i < thingsInfo.size(); i++)
		{
			JSONObject thing = (JSONObject) thingsInfo.get(i);
			String thingID = (String) thing.get("Id");
			String value = (String) thing.get("Value");
			String type = (String) thing.get("Type");
			
			Condition condition = new Condition (nodeID, thingID, value, type);
			
			execute_single_condition(condition);
		}
	}
	
	private void execute_single_condition(Condition condition)
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
			System.out.println ("[RM - Process] actions : "+ action.getStatement());
			action.execute();
		}
	}
}
